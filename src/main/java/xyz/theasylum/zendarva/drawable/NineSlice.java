package xyz.theasylum.zendarva.drawable;

import xyz.theasylum.zendarva.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NineSlice implements IDrawable {

    private final int sliceHeight;
    private final int sliceWidth;
    private int drawWidth;
    private int drawHeight;
    private BufferedImage image;

    public NineSlice(String imageName, int sliceHeight, int sliceWidth, int drawWidth, int drawHeight) {

        this.sliceHeight = sliceHeight;
        this.sliceWidth = sliceWidth;
        this.drawWidth = drawWidth;
        this.drawHeight = drawHeight;
        try {
            image = ImageIO.read(Image.class.getResourceAsStream(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {

    }

    public int getDrawWidth() {
        return drawWidth;
    }

    public void setDrawWidth(int drawWidth) {
        this.drawWidth = drawWidth;
    }

    public int getDrawHeight() {
        return drawHeight;
    }

    public void setDrawHeight(int drawHeight) {
        this.drawHeight = drawHeight;
    }
}
