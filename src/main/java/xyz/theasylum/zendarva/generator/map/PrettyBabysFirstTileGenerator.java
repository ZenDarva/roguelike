package xyz.theasylum.zendarva.generator.map;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.domain.Tile;
import xyz.theasylum.zendarva.domain.TilesetData;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PrettyBabysFirstTileGenerator implements MapGenerator {
    Tile[][] tiles;
    private int width;
    private int height;
    private Tileset tileset;

    @Override
    public Tile[][] generate(Random rnd, int width, int height, Tileset tileset) {
        this.width = width;
        this.height = height;
        this.tileset = tileset;
        tiles = new Tile[width][height];
        generateRooms();
        return tiles;
    }

    public void generateRooms() {
        int numRooms = Game.rnd.nextInt(15)+10;
        //numRooms=3;
        List<Rectangle> rooms = new LinkedList<>();

        Point center=null;

        for (int x = 0; x< width;x++){
            for (int y = 0; y <height;y++){
                tiles[x][y]= new Tile(4);
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

            //rooms need to be at least 2 tiles apart.
            Rectangle collisionRect = new Rectangle(rect.x-3,rect.y-3,rect.width+3,rect.height+3);
            if (rooms.stream().anyMatch(f -> f.intersects(collisionRect))) {
                continue;
            }

            if (center != null){
                horizontalCorridor((int)rect.getCenterX(),center.x,(int)rect.getCenterY());
                verticalCorridor((int)rect.getCenterY(),center.y,center.x);
            }

            center = new Point((int)rect.getCenterX(),(int)rect.getCenterY());

            rooms.add(rect);
        }
        rooms.stream().forEach(f->setTiles(f,114));

        applyTrim();

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

        Rectangle rect = new Rectangle(min,y,max-min,1);
        setTiles(rect, 114);
    }
    private void verticalCorridor(int y1, int y2, int x){
        int min = Math.min(y1,y2);
        int max = Math.max(y1,y2);

        Rectangle rect = new Rectangle(x,min,1,max-min);
        setTiles(rect,114);
    }

    private void applyTrim(){
        int flags=0;
        int floorTile = tileset.getNamedTilenum("roomFloor");
        int empty = tileset.getNamedTilenum("nothing");

        for (int x = 0; x<width;x++){
            for (int y = 0; y<width;y++) {
                flags=0;

                if (getTile(x,y) != empty)
                    continue;

                if (getTile(x-1,y) == floorTile)
                    flags |= Tileset.WEST;
                if (getTile(x+1,y) == floorTile)
                    flags |= Tileset.EAST;
                if (getTile(x,y-1) == floorTile)
                    flags |= Tileset.NORTH;
                if (getTile(x,y+1)== floorTile)
                    flags |= Tileset.SOUTH;
                if (flags==0) {
                    if (getTile(x + 1, y + 1) == floorTile)
                        flags |= Tileset.NORTHWEST;
                    if (getTile(x - 1, y + 1) == floorTile)
                        flags |= Tileset.NORTHEAST;
                    if (getTile(x + 1, y - 1) == floorTile)
                        flags |= Tileset.SOUTHWEST;
                    if (getTile(x - 1, y - 1) == floorTile)
                        flags |= Tileset.SOUTHEAST;
                }
                if (flags !=0)
                    setTile(x,y,tileset.getNeededTrim(flags));
            }
        }
    }

    private int getTile(int x, int y){
        if (x <0 || x >= width || y <0 || y >= height)
                return 4;
        return tiles[x][y].tileNum;
    }

    private void setTileByName(int x, int y, String name){
        if (x <0 || x >= width || y <0 || y >= height)
            return ;
        tiles[x][y].tileNum=tileset.getNamedTilenum(name);

    }
    private void setTile(int x, int y, int num){
        if (x <0 || x >= width || y <0 || y >= height)
            return ;
        tiles[x][y].tileNum=num;

    }

}
