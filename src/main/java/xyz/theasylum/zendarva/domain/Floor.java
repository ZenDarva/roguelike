package xyz.theasylum.zendarva.domain;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;
import xyz.theasylum.zendarva.generator.map.BabysFirstGenerator;
import xyz.theasylum.zendarva.generator.map.MapGenerator;
import xyz.theasylum.zendarva.generator.map.PrettyBabysFirstTileGenerator;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Floor {
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private final int width;
    private final int height;

    private final Tile[][] tiles;
    private final Tileset tileset;

    private final List<Entity> entities = new LinkedList<>();

    public Floor(int width, int height,Tileset tileset) {
        this.width = width;
        this.height = height;
        this.tileset = tileset;
        MapGenerator generator = new PrettyBabysFirstTileGenerator();
        tiles=generator.generate(Game.rnd,width,height,tileset);

        EventBus.instance().registerHandler(this);
    }

    public Tile[][] getTiles(){
        return tiles;
    }
    public void addEntity(Entity entity){
        entities.add(entity);
    }
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
    public List<Entity> getEntities(){
        return Collections.unmodifiableList(entities);
    }


    public Optional<Entity> getEntity(int x, int y){
        Point point = new Point(x,y);
        Optional<Entity> optEnt = entities.stream().filter(f->f.loc.distance(point)==0).findFirst();
        return optEnt;

    }
    public Optional<Entity> getEntity(Point point){
        return getEntity(point.x,point.y);
    }

    private void handleMobDeath(EventEntity.EventEntityDie e){
        entities.remove(e.getEntity());
    }

    public Point getSpawn(){
        while (true){
            int x = Game.rnd.nextInt(width);
            int y = Game.rnd.nextInt(height);
            if (tileset.tileWalkable(tiles[x][y]))
                return new Point(x,y);
        }
    }

    public Tileset getTileset() {
        return tileset;
    }

    public boolean moveEntity(Entity entity, int x, int y){
        if (tileset.tileWalkable(tiles[x][y]) && ! entities.stream().anyMatch(f->f.loc.distance(x,y)==0)) {
            entity.loc = new Point(x, y);
            return true;
        }

        return false;
    }

    public boolean canMove(Entity entity, int x, int y){
        if (x < 0 || y < 0 || x > width-1 || y > height-1)
            return false;
        if (tileset.tileWalkable(tiles[x][y]) && ! entities.stream().anyMatch(f->f.loc.distance(x,y)==0)) {
            return true;
        }

        return false;
    }
}
