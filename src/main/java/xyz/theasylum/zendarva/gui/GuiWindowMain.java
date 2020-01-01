package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.drawable.widget.WidgetScrollingMap;
import xyz.theasylum.zendarva.drawable.widget.WidgetStat;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;

import java.awt.*;

public class GuiWindowMain extends GuiWindow {
    private Floor floor;
    private WidgetScrollingMap map;
    public GuiWindowMain(int width, int height, int mapWidth, int mapHeight, int mapDisplayWidth, int mapDisplayHeight, Tileset tileset) {
        super(width, height);
        floor = new Floor(mapWidth, mapHeight, tileset);

        map = new WidgetScrollingMap(this, mapDisplayWidth,mapDisplayHeight,2);
        map.setFloor(floor);
        addWidget(map);
        map.setVisible(true);

        WidgetStat stat = new WidgetStat(this, Game.player);
        addWidget(stat);
        stat.setLocation(new Point(10,500));
        stat.setVisible(true);

        EventBus.instance().registerHandler(this);
    }

    @Override
    public void drawForeground(Graphics g) {

    }


    private void handleSpawnEnemey(EventEntity.EventSpawnEntity e){
        floor.addEntity(e.getEntity());
    }

    public Floor getCurrentFloor(){
        return floor;
    }

}
