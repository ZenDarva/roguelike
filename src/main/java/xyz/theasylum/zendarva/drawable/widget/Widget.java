package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;
import java.io.IOException;

public abstract class Widget {

    protected boolean visible;
    protected Point loc;
    protected GuiWindow parent;


    protected static final Font smallFont;


    static {
        Font smallFont1;
        try {
            smallFont1 = Font.createFont(Font.TRUETYPE_FONT, Widget.class.getResourceAsStream("/square.ttf"));
            smallFont1=smallFont1.deriveFont(8F);
        } catch (FontFormatException e) {
            smallFont1 = Font.getFont("Monospaced");
            smallFont1=smallFont1.deriveFont(8F);
        } catch (IOException e) {
            smallFont1 = Font.getFont("Monospaced");
            smallFont1=smallFont1.deriveFont(8F);
        }

        smallFont = smallFont1;
    }


    public abstract void draw(Graphics g);

    public void setLocation(Point point){
        loc = point;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean getVisible(){
        return visible;
    }

    protected void drawSmallString(Graphics g, int x, int y, String text){
        g.setColor(Color.red);
        g.setFont(smallFont);
        g.drawString(text,loc.x+x,loc.y+y);
    }

    protected void drawRect(Graphics g, int x, int y, int width, int height, boolean filled){
        if (!filled)
            g.drawRect(x+loc.x,y+loc.y,width,height);
        else
            g.fillRect(x+loc.x,y+loc.y,width,height);

    }
}
