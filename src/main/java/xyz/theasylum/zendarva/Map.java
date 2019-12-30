package xyz.theasylum.zendarva;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Map implements IDrawable {

    private final int width;
    private final int height;
    //bad.
    public List<Entity> entities;
    Tile[][] tiles;
    Tileset tileset;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        //50,30
        tiles = new Tile[width][height];
        tileset = new Tileset("tiles.png",16,16);
        entities = new LinkedList<>();

        generateRooms();
    }

    @Override
    public void draw(Graphics g) {
        for (int x = 0; x< width;x++){
            for (int y = 0; y <height;y++){
                tileset.setTileNum(tiles[x][y].tileNum);
                tileset.draw(g,x*tileset.tileWidth,y*tileset.tileHeight);
            }
        }
        for (Entity entity : entities) {
            tileset.setTileNum(entity.tileNum);
            tileset.draw(g,entity.loc.x*tileset.tileWidth,entity.loc.y *tileset.tileHeight);
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public boolean moveEntity(Entity entity, int x, int y){
        if (tiles[x][y].walkable() && ! entities.stream().anyMatch(f->f.loc.distance(x,y)==0)) {
            entity.loc = new Point(x, y);
            return true;
        }

            return false;
    }

    public boolean canMove(Entity entity, int x, int y){
        if (x < 0 || y < 0 || x > width || y > height)
            return false;
        if (tiles[x][y].walkable() && ! entities.stream().anyMatch(f->f.loc.distance(x,y)==0)) {
            return true;
        }

        return false;
    }


    private class Tile{
        public int tileNum;

        public Tile(int tileNum) {
            this.tileNum = tileNum;
        }
        public boolean walkable(){
            return tileNum==62;

        }
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

    public Point getSpawn(){
        while (true){
            int x = Game.rnd.nextInt(width);
            int y = Game.rnd.nextInt(height);
            if (tiles[x][y].walkable())
                return new Point(x,y);
        }
    }


}
