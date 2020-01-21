package xyz.theasylum.zendarva.drawable.widget;

import java.util.Optional;

public interface IWidgetContainer {

    public void addWidget(Widget widget);
    public void removeWidget(Widget widget);
    public void markDirty();
    public Optional<IWidgetContainer> getParent();
}
