package xyz.theasylum.zendarva.domain;

import xyz.theasylum.zendarva.component.Component;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Entity {

    public Point loc;
    public int tileNum;

    public List<Component> components = new ArrayList<>();
    public int hp;
    public int maxHp;
}
