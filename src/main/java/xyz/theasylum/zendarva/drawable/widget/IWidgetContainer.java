package xyz.theasylum.zendarva.drawable.widget;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Optional;

public interface IWidgetContainer {

    public void addWidget(Widget widget);
    public void removeWidget(Widget widget);
    public void markDirty();
    public Optional<IWidgetContainer> getParent();
    public Rectangle getRect();
    public void move(int x, int y);

    boolean processMouseClick(Point pos, MouseEvent e);

}
