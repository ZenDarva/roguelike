package xyz.theasylum.zendarva;

import xyz.theasylum.zendarva.drawable.IDrawable;
import xyz.theasylum.zendarva.generator.TilesetGenerator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image implements IDrawable {
    private BufferedImage buf;

    public Image(){
            buf = new TilesetGenerator("/tiles3.png").generate2();
            File outputFile = new File("/temp/generatedTiles.png");
        try {
            ImageIO.write(buf,"png",outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.drawImage(buf,10,10,buf.getWidth()*2,buf.getHeight()*2,null);
    }

    public void load(){

    }
}
