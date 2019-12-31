package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class WidgetMap extends Widget {

    private final int width;
    private final int height;
    private Floor floor;


    public WidgetMap(int width, int height) {
        this.width = width;
        this.height = height;


    }

    @Override
    public void draw(Graphics g) {
        for (int x = 0; x< width;x++){
            for (int y = 0; y <height;y++){
                floor.getTileset().setTileNum(floor.getTiles()[x][y].tileNum);
                floor.getTileset().draw(g,x*floor.getTileset().tileWidth,y*floor.getTileset().tileHeight);
            }
        }
        for (Entity entity : floor.getEntities()) {
            floor.getTileset().setTileNum(entity.tileNum);
            floor.getTileset().draw(g,entity.loc.x*floor.getTileset().tileWidth,entity.loc.y *floor.getTileset().tileHeight);
        }
    }

    public void setFloor(Floor floor) {
        this.floor=floor;
    }

    @Override
    public void update() {
        List<Entity> dead = new LinkedList<>();
        for (Entity entity : floor.getEntities()) {
            if (entity.hp <=0){
                dead.add(entity);
            }
        }
        floor.getEntities().removeAll(dead);
    }
}
