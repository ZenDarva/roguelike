package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.ITickable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public abstract class Widget implements ITickable, IWidgetContainer {

    protected boolean visible = true;
    protected Point loc;
    protected IWidgetContainer parent;
    protected int zDepth;
    protected List<Widget> widgets;
    protected int width;
    protected int height;
    protected boolean isDirty=true;
    protected BufferedImage texture;
    private boolean dirty = true;
    private Graphics g;
    private boolean opaque = true;
    private Rectangle rect;



    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }


    public Widget(IWidgetContainer parent, int width, int height){
        this.parent = parent;
        this.width = width;
        this.height = height;
        texture = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        this.loc = new Point(0,0);
        widgets = new LinkedList<>();
        rect = new Rectangle(0,0,width,height);
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

    @Override
    public Rectangle getRect() {
        return rect;
    }

    public void draw(Graphics g){

        if (dirty){
            Graphics lg = texture.createGraphics();
            this.g=lg;
            lg.setColor(Color.black);
            if (opaque)
                lg.fillRect(0,0,width,height);
            drawBackground(lg);

            widgets.stream().filter(Widget::getVisible).forEach(f->f.draw(lg));

            drawForeground(lg);
            //dirty = false;
            lg.dispose();
            this.g=null;
        }
        g.drawImage(texture,loc.x,loc.y,null);
    }

    protected void drawForeground(Graphics g){};

    protected void drawBackground(Graphics g){};

    public void setLocation(Point point){
        loc = point;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public boolean getVisible(){
        return visible;
    }

    protected void drawSmallString(int x, int y, String text){
        if (g==null){
            System.out.println("Null graphics.");
            return;
        }
        g.setColor(Color.red);
        g.setFont(smallFont);
        g.drawString(text,x,y);
    }

    protected void drawRect(int x, int y, int width, int height, boolean filled){
        if (g==null){
            System.out.println("Null graphics.");
            return;
        }
        if (!filled)
            g.drawRect(x,y,width,height);
        else
            g.fillRect(x,y,width,height);

    }

    @Override
    public void addWidget(Widget widget) {
        widgets.add(widget);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }

    @Override
    public void removeWidget(Widget widget) {
        widgets.remove(widget);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }

    @Override
    public void markDirty() {
        dirty=true;
    }

    @Override
    public Optional<IWidgetContainer> getParent() {
        return Optional.of(parent);
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    public void move(int x, int y){
        loc.x+=x;
        loc.y+=y;
        rect.x+=x;
        rect.y+=y;
    }

    @Override
    public boolean processMouseClick(Point pos, MouseEvent e) {
        return false;
    }

}
