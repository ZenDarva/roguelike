package xyz.theasylum.zendarva.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TilesetData {
    public HashMap<String,Integer> namedTiles;
    public List<Integer> walkableTiles;
    public HashMap<Integer,String> neededTrim;


    public TilesetData() {
        namedTiles= new HashMap<>();
        walkableTiles = new ArrayList<>();
        neededTrim = new HashMap<>();
    }
}
