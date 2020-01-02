package xyz.theasylum.zendarva.generator.map;

import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.domain.Tile;

import java.util.Random;

public interface MapGenerator {

    public Tile[][] generate(Random rnd, int width, int height, Tileset tileset);
}
