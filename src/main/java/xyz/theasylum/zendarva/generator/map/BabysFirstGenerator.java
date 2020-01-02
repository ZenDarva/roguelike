package xyz.theasylum.zendarva.generator.map;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.domain.Tile;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BabysFirstGenerator implements MapGenerator {
    Tile[][] tiles;
    private int width;
    private int height;

    @Override
    public Tile[][] generate(Random rnd, int width, int height, Tileset tileset) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        generateRooms();
        return tiles;
    }

    public void generateRooms() {
        int numRooms = Game.rnd.nextInt(15)+10;

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


            if (rooms.stream().anyMatch(f -> f.intersects(rect))) {
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
            tiles[x][y].tileNum=114;
        }
    }
    private void verticalCorridor(int y1, int y2, int x){
        int min = Math.min(y1,y2);
        int max = Math.max(y1,y2);

        for (int y = min; y<max+1;y++){
            tiles[x][y].tileNum=114;
        }
    }

}
