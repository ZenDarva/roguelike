package xyz.theasylum.zendarva;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Tileset {
    public final int tileWidth;
    public final int tileHeight;
    BufferedImage image;
    int tileNum=0;
    ArrayList<Rectangle> tiles;

    public Tileset(String filename, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        tiles = new ArrayList<Rectangle>();
        try {
            image = ImageIO.read(Image.class.getResourceAsStream("/tiles.png"));
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
    }


    public void draw(Graphics g, int x, int y) {
        Rectangle rect = tiles.get(tileNum);
        g.drawImage(image,x,y,tileWidth+x,tileHeight+y,rect.x,rect.y,rect.x+rect.width,rect.y+rect.height,null);
    }

    public void setTileNum(int num){
        if (num > tiles.size())
            return;
        tileNum=num;
    }
}
