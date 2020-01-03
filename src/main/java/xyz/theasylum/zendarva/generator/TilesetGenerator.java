package xyz.theasylum.zendarva.generator;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import xyz.theasylum.zendarva.Image;
import xyz.theasylum.zendarva.domain.DynamicTilesetData;
import xyz.theasylum.zendarva.domain.TilesetData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TilesetGenerator {

    public final int internalTileWidth;
    public final int internalTileHeight;
    public final int tileWidth;
    public final int tileHeight;
    BufferedImage image;
    BufferedImage newTileset;
    DynamicTilesetData data;
    ArrayList<Rectangle> tiles;
    TilesetData newData;

    public TilesetGenerator(String filename){
        String jsonName=filename.substring(0,filename.indexOf("."))+".json";
        Gson gson = new Gson();
        InputStream is = Image.class.getResourceAsStream(jsonName);
        if (is == null)
            throw new RuntimeException("Missing " + jsonName);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        data = gson.fromJson(reader, DynamicTilesetData.class);
        newData = new TilesetData();

        this.internalTileWidth = data.tileData.get("internalTileWidth");
        this.internalTileHeight = data.tileData.get("internalTileHeight");
        this.tileWidth=data.tileData.get("tileWidth");
        this.tileHeight=data.tileData.get("tileHeight");
        tiles = new ArrayList<Rectangle>();
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(filename));
            int index=0;
            for (int y = 0; y< image.getHeight();y+= internalTileHeight){
                for (int x = 0;x<image.getWidth();x+= internalTileWidth){
                    Rectangle rect = new Rectangle(x,y, internalTileWidth, internalTileHeight);
                    tiles.add(index,rect);
                    index++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        newTileset = new BufferedImage(image.getWidth()*2,image.getHeight()*2,BufferedImage.TYPE_4BYTE_ABGR);
    }

    public BufferedImage generate(){
        Graphics g = newTileset.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,tileWidth,tileHeight);

        for (int i=1;i<=192; i++){
        int[] tile = {99,99,99,99};

        for(int flag = 1; flag <=128;flag*=2)
            if (checkFlag(i,flag)){
                addTiles(tile,getTileArray(flag) );
            }
            int x = (i % 20) * tileWidth;
            int y = (i / 20)*tileHeight;

            createTileByArray(g,x,y,tile);

        }
        setTileNumByName(g,0,"roomFloor");
        setTileNumByName(g,193, "floorShadow");

        return newTileset;

    }
    private void setTileNumByName(Graphics g, int num, String name){
        int x = (num % 20) * tileWidth;
        int y = (num / 20) * tileHeight;

        createTileByName(g,x,y,name);
        newData.namedTiles.put(name,num);
    }

    private boolean checkFlag(int flag, int value){
        return (flag & value) == value;
    }

    private int[] getTileArray(int i){
        return data.namedMetatiles.get(data.neededTiles.get(i));
    }

    private void addTiles(int[] toArray, int[] fromArray){
        for (int i = 0; i<4;i++) {
            if (fromArray[i] != 99)
            toArray[i] = checkMerge(fromArray[i],toArray[i]);
        }
    }

    //This whole function is evil.
    private int checkMerge(int from, int to) {
        if ((from == 0 && to == 2) || (from == 2 && to == 0))
            return 4;
        if ((from == 1 && to == 3) || (from == 3 && to == 1))
            return 5;
        if ((from == 20 && to ==22) || (from==22 && to ==20))
            return 24;
        if ((from == 21 && to ==23) || (from==23 && to ==21))
            return 25;

        if ((from ==6 || from ==7 || from ==26 || from ==27) && to !=99)
            return to;
        return from;
    }

    public void createTileByArray(Graphics g, int x, int y, int[] tiles){
        drawTile(g,tiles[0],x,y);
        drawTile(g,tiles[1],x+internalTileWidth,y);
        drawTile(g,tiles[2],x,y+internalTileHeight);
        drawTile(g,tiles[3], x+internalTileWidth,y+internalTileHeight);
    }

    public void createTileByName(Graphics g,int x, int y, String tileName){
        if (!data.namedMetatiles.containsKey(tileName))
            return;
        int[] tiles = data.namedMetatiles.get(tileName);

        drawTile(g,tiles[0],x,y);
        drawTile(g,tiles[1],x+internalTileWidth,y);
        drawTile(g,tiles[2],x,y+internalTileHeight);
        drawTile(g,tiles[3], x+internalTileWidth,y+internalTileHeight);
    }

    private void drawTile(Graphics g, int num, int x, int y){
        Rectangle tileRect = tiles.get(num);
        g.drawImage(image,x,y,x+internalTileWidth,y+internalTileHeight,tileRect.x,tileRect.y,tileRect.x+tileRect.width,tileRect.y+tileRect.height,null);
    }
}
