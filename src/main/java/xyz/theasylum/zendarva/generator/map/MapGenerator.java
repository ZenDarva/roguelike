package xyz.theasylum.zendarva.generator.map;

import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.domain.Tile;

import java.util.Random;

public interface MapGenerator {

    Tile[][] generate(Random rnd, int width, int height, int tilesetIndex);
}
