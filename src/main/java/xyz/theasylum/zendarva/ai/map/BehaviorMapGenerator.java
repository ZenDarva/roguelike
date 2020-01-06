package xyz.theasylum.zendarva.ai.map;

import xyz.theasylum.zendarva.Utility.Profile;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.Floor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BehaviorMapGenerator {

    public BehaviorMap generateFromPoints(Floor floor, List<Point> goals){
       //Object obj = new Object();
        //Profile.start(obj);
        BehaviorMap map = new BehaviorMap();
        int width = floor.getWidth();
        int height = floor.getHeight();
        map.width=width;
        map.height=height;
        int[][] tiles = new int[floor.getWidth()][floor.getHeight()];

        for (int x = 0; x<width;x++){
            for (int y = 0; y<height;y++){
                tiles[x][y]=9001;
            }
        }

        goals.stream().forEach(f->tiles[f.x][f.y]=0);

        Queue<Point> open = new ArrayDeque<>();

        open.addAll(goals);

        while (!open.isEmpty()){
            Point point = open.poll();
            int myNum = safeRead(tiles,point.x,point.y);
            for (Point neighbor : getNeighbors(point)) {
                int targPoint = safeRead(tiles,neighbor.x,neighbor.y);
                if (targPoint == 9001 && floor.isWalkable(neighbor.x,neighbor.y)){
                    safeWrite(tiles,neighbor.x,neighbor.y,myNum+1);
                    open.add(neighbor);
                }
                if (targPoint >0 && targPoint<9000){

                }
            }
        }
        map.tiles=tiles;
        return map;

    }

    public BehaviorMap generateFromEntities(Floor floor, List<Entity> entities){
        return generateFromPoints(floor, entities.stream().map(f->f.loc).collect(Collectors.toList()));
    }

    private List<Point> getNeighbors(Point point){
        List<Point> neighbors = new ArrayList<Point>();
        neighbors.add(new Point(point.x+1,point.y));
        neighbors.add(new Point(point.x-1,point.y));
        neighbors.add(new Point(point.x,point.y+1));
        neighbors.add(new Point(point.x,point.y-1));
        return neighbors;

    }


    private int safeRead(int[][] tiles, int x, int y){

        int width = tiles.length;
        int height = tiles[0].length;

        if (x <0 || x>= width || y <0 || y >=height)
            return -1;
        return tiles[x][y];
    }

    private void safeWrite(int[][] tiles, int x, int y, int newValue){
        int width = tiles.length;
        int height = tiles[0].length;

        if (x <0 || x>= width || y <0 || y >=height)
            return;
        tiles[x][y]=newValue;
    }
}
