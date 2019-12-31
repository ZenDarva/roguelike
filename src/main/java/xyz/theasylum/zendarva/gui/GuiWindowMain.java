package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.drawable.widget.WidgetMap;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventSpawnEntity;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;

public class GuiWindowMain extends GuiWindow {
    private Floor floor;
    private WidgetMap map;
    public GuiWindowMain(int width, int height, int mapWidth, int mapHeight, Tileset tileset) {
        super(width, height);
        floor = new Floor(mapWidth, mapHeight, tileset);

        map = new WidgetMap(mapWidth,mapHeight);
        map.setFloor(floor);
        addWidget(map);
        map.setVisible(true);

        EventBus.instance().registerHandler(this);
    }

    @Override
    public void drawForeground(Graphics g) {

    }


    private void handleSpawnEnemey(EventSpawnEntity e){
        floor.addEntity(e.getEntity());
    }

    public Floor getCurrentFloor(){
        return floor;
    }

}
