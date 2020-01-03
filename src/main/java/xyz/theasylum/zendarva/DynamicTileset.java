package xyz.theasylum.zendarva;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import xyz.theasylum.zendarva.domain.DynamicTilesetData;
import xyz.theasylum.zendarva.domain.TilesetData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DynamicTileset {

    public final int internalTileWidth;
    public final int internalTileHeight;
    public final int tileWidth;
    public final int tileHeight;
    BufferedImage image;
    BufferedImage curTile;
    DynamicTilesetData data;
    ArrayList<Rectangle> tiles;

    public DynamicTileset(String filename){

        String jsonName=filename.substring(0,filename.indexOf("."))+".json";
        Gson gson = new Gson();
        InputStream is = Image.class.getResourceAsStream(jsonName);
        if (is == null)
            throw new RuntimeException("Missing " + jsonName);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        data = gson.fromJson(reader, DynamicTilesetData.class);

        this.internalTileWidth = data.tileData.get("internalTileWidth");
        this.internalTileHeight = data.tileData.get("internalTileHeight");
        this.tileWidth=data.tileData.get("tileWidth");
        this.tileHeight=data.tileData.get("tileHeight");

        tiles = new ArrayList<Rectangle>();

        curTile = new BufferedImage(tileWidth,tileHeight,BufferedImage.TYPE_4BYTE_ABGR);

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
    }

    public void setTileByName(String tileName){
        if (!data.namedMetatiles.containsKey(tileName))
            return;
        int[] tiles = data.namedMetatiles.get(tileName);

        Graphics g = curTile.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0,tileWidth,tileHeight);

        drawTile(g,tiles[0],0,0);
        drawTile(g,tiles[1],internalTileWidth,0);
        drawTile(g,tiles[2],0,internalTileHeight);
        drawTile(g,tiles[3], internalTileWidth,internalTileHeight);
    }

    private void drawTile(Graphics g, int num, int x, int y){

        Rectangle tileRect = tiles.get(num);
        g.drawImage(image,x,y,x+internalTileWidth,y+internalTileHeight,tileRect.x,tileRect.y,tileRect.x+tileRect.width,tileRect.y+tileRect.height,null);
    }

    public void drawTile(Graphics g, int x, int y){
        g.drawImage(curTile,x,y, tileWidth*2,tileHeight*2,null);
    }
}
