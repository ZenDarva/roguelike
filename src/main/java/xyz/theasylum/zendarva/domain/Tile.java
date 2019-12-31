package xyz.theasylum.zendarva.domain;

public class Tile{
    public int tileNum;

    public Tile(int tileNum) {
        this.tileNum = tileNum;
    }
    public boolean walkable(){
        return tileNum==62;

    }
}