package xyz.theasylum.zendarva.domain;

import com.google.gson.annotations.Expose;
import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.component.BlocksMovement;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.generator.map.MapGenerator;
import xyz.theasylum.zendarva.generator.map.PrettyBabysFirstTileGenerator;

import java.awt.*;
import java.beans.ConstructorProperties;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Floor {
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Expose
    private final int width;
    @Expose
    private final int height;
    @Expose
    private int tilesetIndex;

    @Expose
    private final Tile[][] tiles;




    public Floor(int width, int height,int tilesetIndex) {
        this.width = width;
        this.height = height;
        this.tilesetIndex = tilesetIndex;

        MapGenerator generator = new PrettyBabysFirstTileGenerator();
        tiles=generator.generate(GameState.instance().rnd,width,height,tilesetIndex);

        EventBus.instance().registerHandler(this);
    }

    public Tile[][] getTiles(){
        return tiles;
    }

    public Optional<Entity> getEntity(int x, int y){
        Point point = new Point(x,y);

        return getEntities().stream().filter(f->f.loc.distance(point)==0).findFirst();

    }
    public int getTile(int x, int y){
        return tiles[x][y].tileNum;
    }
    public Optional<Entity> getEntity(Point point){
        return getEntity(point.x,point.y);
    }

    public List<Entity> getEntities(Point point){
        return getEntities().stream().filter(f->f.loc.distance(point) == 0).collect(Collectors.toList());
    }


    public Optional<Entity> getEntityWithComponent(Point point, Class<? extends Component> type){
        Optional<Entity> e = getEntity(point.x,point.y);
        return e.filter(f->f.hasComponent(type));

    }

    public Set<Entity> getEntities(){
        return GameState.instance().getEntitiesForFloor(this);
    }

    public Set<Entity> getEntitiesWithinRange(Point point, int range){
        return getEntities().stream().filter(f->f.loc.distance(point) <=range).collect(Collectors.toSet());
    }

    public Point getSpawn(){
        Tileset tileset = GameState.instance().getTilest(tilesetIndex);
        while (true){
            int x = GameState.instance().rnd.nextInt(width);
            int y = GameState.instance().rnd.nextInt(height);
            if (tileset.tileWalkable(tiles[x][y]))
                return new Point(x,y);
        }
    }

    public Tileset getTileset() {
        return GameState.instance().getTilest(tilesetIndex);
    }

    public boolean moveEntity(Entity entity, int x, int y){
        if (getTileset().tileWalkable(tiles[x][y]) && ! getEntities().stream().filter(f->f.hasComponent(BlocksMovement.class)).anyMatch(f->f.loc.distance(x,y)==0)) {
            entity.loc = new Point(x, y);
            return true;
        }

        return false;
    }

    public boolean canMove(Entity entity, int x, int y){
        if (x < 0 || y < 0 || x > width-1 || y > height-1)
            return false;
        return getTileset().tileWalkable(tiles[x][y]) && !getEntities().stream().filter(f -> f.hasComponent(BlocksMovement.class)).anyMatch(f -> f.loc.distance(x, y) == 0);
    }

    public List<Point> getFOV(Entity entity){
        List<Point> fovPoints = new LinkedList<>();
        if (tiles[entity.loc.x][entity.loc.y].fov !=null){
            return tiles[entity.loc.x][entity.loc.y].fov;
        }
        double x,y;

        for (int i=0;i<360;i++){
            x=Math.cos((float)i*0.01745f);
            y=Math.sin((float)i*0.01745f);
            getFOV(entity, x,y,fovPoints);
        }
        tiles[entity.loc.x][entity.loc.y].fov =fovPoints;
        return fovPoints;
    }
    private void getFOV(Entity entity, double x, double y, List<Point> fovPoints){
        float lx = (float) (entity.loc.x+.5);
        float ly = (float) (entity.loc.y+.5);

        for (int i =0;i<6;i++) {
            Point point = new Point((int)lx,(int)ly);
            if (!fovPoints.contains(point))
                fovPoints.add(point);
            if (!isWalkable((int)lx,(int)ly))
                return;
            lx+=x;
            ly+=y;
        }

    }

    public void updateFOV(Entity entity){
        List<Point> fov = getFOV(entity);
        fov.stream().forEach(f->setVisible(f.x,f.y));
    }

    private void setVisible(int x, int y){
        if (x < 0 || y < 0 || x > width-1 || y > height-1)
            return;

        tiles[x][y].visited=true;
    }

    public boolean isWalkable(int x, int y){
        if (x < 0 || y < 0 || x > width-1 || y > height-1)
            return false;
        return getTileset().tileWalkable(tiles[x][y]);
    }

    public List<Point> getWalkableNeighbors(Point loc) {
        List<Point> neighbors = new ArrayList<Point>();
        if (isWalkable(loc.x+1,loc.y))
            neighbors.add(new Point(loc.x + 1, loc.y));
        if (isWalkable(loc.x-1,loc.y))
            neighbors.add(new Point(loc.x - 1, loc.y));
        if (isWalkable(loc.x,loc.y+1))
            neighbors.add(new Point(loc.x, loc.y + 1));
        if (isWalkable(loc.x,loc.y-1))
            neighbors.add(new Point(loc.x, loc.y - 1));
        return neighbors;

    }
}
