package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;
import java.io.IOException;

public abstract class Widget implements ITickable {

    protected boolean visible;
    protected Point loc;
    protected GuiWindow parent;
    protected int zDepth;


    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public Widget(GuiWindow parent){
        this.parent = parent;
        this.loc = new Point(0,0);
    }


    protected static final Font smallFont;


    static {
        Font smallFont1;
        try {
            smallFont1 = Font.createFont(Font.TRUETYPE_FONT, Widget.class.getResourceAsStream("/square.ttf"));
            smallFont1=smallFont1.deriveFont(8F);
        } catch (Exception e) {
            smallFont1 = Font.getFont("System");
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
