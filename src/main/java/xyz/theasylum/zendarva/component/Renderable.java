package xyz.theasylum.zendarva.component;

import xyz.theasylum.zendarva.Tileset;



public class Renderable implements Component {
    private Tileset tileset;
    private int tileNum;

    public Renderable(Tileset tileset, int tileNum) {
        this.tileset = tileset;
        this.tileNum = tileNum;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public int getTileNum() {
        return tileNum;
    }

    public void setTileNum(int tileNum) {
        this.tileNum = tileNum;
    }
}
