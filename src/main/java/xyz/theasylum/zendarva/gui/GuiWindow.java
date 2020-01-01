package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.drawable.IDrawable;
import xyz.theasylum.zendarva.drawable.widget.Widget;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GuiWindow implements IDrawable, ITickable {
    private final int width;

    @Override
    public void update() {
        widgets.forEach(Widget::update);
    }

    private final int height;
    private BufferedImage texture;
    private List<Widget> widgets;
    private Widget focusedWidget = null;

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    private boolean dirty = true;

    private boolean visible = false;

    private Point loc;


    public void addWidget(Widget widget){
        widgets.add(widget);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }
    public void removeWidget(Widget widget) {
        widgets.remove(widget);
        Collections.sort(widgets, Comparator.comparingInt(Widget::getzDepth));
    }

    public GuiWindow(int width, int height) {
        this.width = width;
        this.height = height;

        texture = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR_PRE);
        loc = new Point(0,0);
        widgets = new LinkedList<>();
    }

    @Override
    public void draw(Graphics g) {
        if (dirty){
            Graphics lg = texture.createGraphics();
            lg.setColor(Color.black);

            lg.fillRect(0,0,width,height);

            widgets.stream().filter(Widget::getVisible).forEach(f->f.draw(lg));

            drawForeground(lg);
            //dirty = false;
        }
        g.drawImage(texture,loc.x,loc.y,null);

    }



    public abstract void drawForeground(Graphics g);

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }





}
