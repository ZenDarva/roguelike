package xyz.theasylum.zendarva;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image implements IDrawable{
    private BufferedImage buf;

    public Image(){
        try {
            buf =ImageIO.read(Image.class.getResourceAsStream("/tiles.png"));
            System.out.println("Yay!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.drawImage(buf,10,10,null);
    }

    public void load(){

    }
}
