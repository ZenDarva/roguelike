package xyz.theasylum.zendarva;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import xyz.theasylum.zendarva.domain.Tile;
import xyz.theasylum.zendarva.domain.TilesetData;
import xyz.theasylum.zendarva.generator.TilesetGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Tileset {
    public static int NORTH =1;
    public static int EAST = 2;
    public static int WEST = 4;
    public static int SOUTH = 8;
    public static int NORTHEAST=16;
    public static int NORTHWEST=32;
    public static int SOUTHEAST=64;
    public static int SOUTHWEST=128;


    public final String filename;
    public final int tileWidth;
    public final int tileHeight;
    @Expose(serialize = false,deserialize  =false)
    private BufferedImage image;
    int tileNum=0;
    ArrayList<Rectangle> tiles;
    TilesetData data;

    public Tileset(String filename, int tileWidth, int tileHeight) {
        this.filename = filename;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        tiles = new ArrayList<Rectangle>();
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(filename));
            int index=0;
            for (int y = 0; y< image.getHeight();y+=tileHeight){
                for (int x = 0;x<image.getWidth();x+=tileWidth){
                    Rectangle rect = new Rectangle(x,y,tileWidth,tileHeight);
                    tiles.add(index,rect);
                    index++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonName=filename.substring(0,filename.indexOf("."))+".json";
        Gson gson = new Gson();
        InputStream is = Image.class.getResourceAsStream(jsonName);
        if (is == null)
            return;
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        data = gson.fromJson(reader, TilesetData.class);



    }

    public Tileset(String filename){
        this.filename=filename;
        TilesetGenerator generator = new TilesetGenerator(filename);
        image = generator.generate();
        data = generator.getNewData();
        this.tileWidth=generator.getTileWidth();
        this.tileHeight=generator.getTileHeight();

        int index=0;

        tiles = new ArrayList<Rectangle>();

        for (int y = 0; y< image.getHeight();y+=tileHeight){
            for (int x = 0;x<image.getWidth();x+=tileWidth){
                Rectangle rect = new Rectangle(x,y,tileWidth,tileHeight);
                tiles.add(index,rect);
                index++;
            }
        }
    }


    public void draw(Graphics g, Point point) {
        drawScaled(g,point.x,point.y,1);
    }

    public void draw(Graphics g, int x, int y) {
        drawScaled(g,x,y,1);
    }

    public void drawScaled(Graphics g, Point point, int scale){
        drawScaled(g,point.x,point.y,scale);
    }

    public void drawScaled(Graphics g, int x ,int y, int scale) {
        Rectangle rect = tiles.get(tileNum);
        g.drawImage(image,x,y,tileWidth*scale+x,tileHeight*scale+y,rect.x,rect.y,rect.x+rect.width,rect.y+rect.height,null);

    }

    public void setTileNum(int num){
        if (num > tiles.size())
            return;
        tileNum=num;
    }

    public void setTileByName(String name) {
        if (data.namedTiles.containsKey(name)){
            setTileNum(data.namedTiles.get(name));
        }
    }

    public int getNamedTilenum(String name){
        if (data.namedTiles.containsKey(name)){
            return data.namedTiles.get(name);
        }
        return 0; //cannonically empty tile.
    }


    public boolean tileWalkable(Tile tile){
        return data.walkableTiles.contains(tile.tileNum);
    }
}
