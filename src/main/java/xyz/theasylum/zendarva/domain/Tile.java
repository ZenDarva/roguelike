package xyz.theasylum.zendarva.domain;

import java.awt.*;
import java.util.List;

public class Tile{
    public int tileNum;
    public Tile(int tileNum) {
        this.tileNum = tileNum;
    }
    public boolean visited = false;
    public List<Point> fov;

}