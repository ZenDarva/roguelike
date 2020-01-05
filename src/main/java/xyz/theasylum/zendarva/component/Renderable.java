package xyz.theasylum.zendarva.component;

import com.google.gson.annotations.Expose;
import xyz.theasylum.zendarva.Tileset;



public class Renderable implements Component {


    private int tilesetIndex;
    private int tileNum;

    public Renderable(int tilesetIndex, int tileNum) {
        this.tilesetIndex = tilesetIndex;
        this.tileNum = tileNum;
    }

    public Renderable() {
        tilesetIndex=0;
        this.tileNum=0;
    }

    public int getTileNum() {
        return tileNum;
    }

    public void setTileNum(int tileNum) {
        this.tileNum = tileNum;
    }

    public int getTilesetIndex() {
        return tilesetIndex;
    }
}
