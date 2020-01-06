package xyz.theasylum.zendarva.ai.map;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BehaviorMap {
    protected int[][] tiles;
    int width;
    int height;

    public int getValue(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return -1;
        return tiles[x][y];
    }

    public Optional<Point> getBestMoveFrom(Point loc) {
        if (loc.x < 0 || loc.x >= width || loc.y < 0 || loc.y >= height)
            return Optional.empty();
        List<Point> neighbors = getNeighbors(loc);
        return neighbors.stream().sorted(pointCompare).findFirst();

    }

    private List<Point> getNeighbors(Point loc) {
        List<Point> neighbors = new ArrayList<Point>();
        if (!(loc.x+1 < 0 || loc.x+1 >= width || loc.y < 0 || loc.y >= height))
            neighbors.add(new Point(loc.x + 1, loc.y));
        if (!(loc.x-1 < 0 || loc.x-1 >= width || loc.y < 0 || loc.y >= height))
            neighbors.add(new Point(loc.x - 1, loc.y));
        if (!(loc.x < 0 || loc.x >= width || loc.y+1 < 0 || loc.y+1 >= height))
            neighbors.add(new Point(loc.x, loc.y + 1));
        if (!(loc.x < 0 || loc.x >= width || loc.y-1 < 0 || loc.y-1 >= height))
            neighbors.add(new Point(loc.x, loc.y - 1));
        return neighbors;

    }

    private Comparator<Point> pointCompare = new Comparator<Point>() {
        @Override
        public int compare(Point o1, Point o2) {
            int first = getValue(o1.x,o1.y);
            int second = getValue(o2.x,o2.y);
            return Integer.compare(first, second);
        }
    };
}
