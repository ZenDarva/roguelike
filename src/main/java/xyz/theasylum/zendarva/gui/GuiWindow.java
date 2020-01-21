package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.drawable.IDrawable;
import xyz.theasylum.zendarva.drawable.widget.IWidgetContainer;
import xyz.theasylum.zendarva.drawable.widget.Widget;

import java.awt.event.KeyEvent;
import java.util.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class GuiWindow implements IDrawable, ITickable, IWidgetContainer {
    protected final int width;
    protected final int height;
    private BufferedImage texture;
    private List<Widget> widgets;
    private Widget focusedWidget = null;
    private List<Widget> deadWidgets;
    private boolean dirty = true;
    private boolean visible = false;
    private Point loc;
    private int z;

    private static final Font mediumFont;

    static {
        Font mediumFont1;
        try {
            mediumFont1 = Font.createFont(Font.TRUETYPE_FONT, Widget.class.getResourceAsStream("/square.ttf"));
            mediumFont1=mediumFont1.deriveFont(14F).deriveFont(Font.BOLD);
        } catch (Exception e) {
            mediumFont1 = Font.getFont("System");
            mediumFont1=mediumFont1.deriveFont(14F).deriveFont(Font.BOLD);
        }

        mediumFont = mediumFont1;
    }


    public GuiWindow(int width, int height) {
        this.width = width;
        this.height = height;

        texture = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        loc = new Point(0,0);
        widgets = new LinkedList<>();
        deadWidgets = new LinkedList<>();
    }
    @Override
    public void addWidget(Widget widget){
        widgets.add(widget);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }
    @Override
    public void removeWidget(Widget widget) {
        deadWidgets.add(widget);
        widget.setVisible(false);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }
    @Override
    public void update() {
        widgets.removeAll(deadWidgets);
        widgets.forEach(Widget::update);
    }


    @Override
    public void draw(Graphics g) {
        if (dirty){
            Graphics lg = texture.createGraphics();
            lg.setColor(Color.black);

            lg.fillRect(0,0,width,height);
            drawBackground(lg);

            widgets.stream().filter(Widget::getVisible).forEach(f->f.draw(lg));

            drawForeground(lg);
            //dirty = false;
            lg.dispose();
        }
        g.drawImage(texture,loc.x,loc.y,null);

    }

    public void move(int x, int y){
        loc.x+=x;
        loc.y+=y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void processKeystroke(KeyEvent e){

    }

    public abstract void drawBackground(Graphics g);

    public abstract void drawForeground(Graphics g);

    @Override
    public void markDirty() {
        this.dirty=true;
    }

    @Override
    public Optional<IWidgetContainer> getParent() {
        return Optional.empty();
    }
}
