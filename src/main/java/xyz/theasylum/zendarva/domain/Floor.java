package xyz.theasylum.zendarva.domain;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Tileset;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Floor {
    private final int width;
    private final int height;

    private final Tile[][] tiles;
    private final Tileset tileset;

    private final List<Entity> entities = new LinkedList<>();

    public Floor(int width, int height,Tileset tileset) {
        this.width = width;
        this.height = height;
        this.tileset = tileset;
        tiles = new Tile[width][height];

        generateRooms();
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

    public Point getSpawn(){
        while (true){
            int x = Game.rnd.nextInt(width);
            int y = Game.rnd.nextInt(height);
            if (tiles[x][y].walkable())
                return new Point(x,y);
        }
    }

    public Tileset getTileset() {
        return tileset;
    }



    public void generateRooms() {
        int numRooms = Game.rnd.nextInt(15)+10;

        List<Rectangle> rooms = new LinkedList<>();

        Point center=null;

        for (int x = 0; x< width;x++){
            for (int y = 0; y <height;y++){
                tiles[x][y]= new Tile(72);
            }
        }

        for (int i = 0; i < numRooms; i++) {

            int x = Game.rnd.nextInt(width);
            int y = Game.rnd.nextInt(height);
            int width = Game.rnd.nextInt(8)+3;
            int height = Game.rnd.nextInt(8)+3;
            Rectangle rect = new Rectangle(x, y, width, height);

            if (rect.x+rect.width >= this.width || rect.y + rect.height >= this.height){
                i--;
                continue;
            }


            if (rooms.stream().anyMatch(f -> f.intersects(rect))) {
                continue;
            }

            if (center != null){
                horizontalCorridor((int)rect.getCenterX(),center.x,(int)rect.getCenterY());
                verticalCorridor((int)rect.getCenterY(),center.y,center.x);
                center = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
            }
            else
                center = new Point((int)rect.getCenterX(),(int)rect.getCenterY());

            rooms.add(rect);
        }
        rooms.stream().forEach(f->setTiles(f,62));



    }

    private void setTiles(Rectangle rect, int tileNum){
        for (int x = rect.x; x< rect.width+rect.x;x++){
            for (int y = rect.y; y <rect.height+rect.y;y++) {
                tiles[x][y].tileNum=tileNum;
            }
        }
    }

    private void horizontalCorridor(int x1, int x2, int y){
        int min = Math.min(x1,x2);
        int max = Math.max(x1,x2);

        for (int x = min; x<max;x++){
            tiles[x][y].tileNum=62;
        }
    }
    private void verticalCorridor(int y1, int y2, int x){
        int min = Math.min(y1,y2);
        int max = Math.max(y1,y2);

        for (int y = min; y<max;y++){
            tiles[x][y].tileNum=62;
        }
    }
}
