package xyz.theasylum.zendarva.domain;

import java.util.HashMap;
import java.util.List;

public class DynamicTilesetData {
    public HashMap<String,int[]> namedMetatiles;
    public HashMap<String,Integer> tileData;
    public HashMap<Integer,String> neededTiles;
    public List<String> walkableTiles;
}
