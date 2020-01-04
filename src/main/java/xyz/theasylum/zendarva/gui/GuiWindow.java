package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.drawable.IDrawable;
import xyz.theasylum.zendarva.drawable.widget.Widget;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GuiWindow implements IDrawable, ITickable {
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


    public GuiWindow(int width, int height) {
        this.width = width;
        this.height = height;

        texture = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        loc = new Point(0,0);
        widgets = new LinkedList<>();
        deadWidgets = new LinkedList<>();
    }
    public void addWidget(Widget widget){
        widgets.add(widget);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }
    public void removeWidget(Widget widget) {
        deadWidgets.add(widget);
        widget.setVisible(false);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }
    @Override
    public void update() {
        widgets.remove(deadWidgets);
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

    public boolean isDirty() {
        return dirty;
    }
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
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


}
