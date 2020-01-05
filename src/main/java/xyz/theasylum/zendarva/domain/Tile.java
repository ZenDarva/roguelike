package xyz.theasylum.zendarva.domain;

import com.google.gson.annotations.Expose;

import java.awt.*;
import java.util.List;

public class Tile{
    @Expose
    public int tileNum;
    @Expose
    public boolean visited = false;
    @Expose
    public List<Point> fov;

    public Tile(int tileNum) {
        this.tileNum = tileNum;
    }

}