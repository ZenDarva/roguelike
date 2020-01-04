package xyz.theasylum.zendarva.domain;

import xyz.theasylum.zendarva.component.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Optional;

public class Entity {

    public Point loc;
    public int tileNum;
    public String name;

    private HashMap<Class, Component> components = new HashMap<>();

    public <T extends Component> Optional<T>  getComponent(Class<T> type){
        if (components.containsKey(type)){
            return (Optional<T>) Optional.of(components.get(type));
        }
        return Optional.empty();
    }

    public void addComponent(Class type, Component component){
        components.put(type, component);
    }

    public boolean hasComponent(Class<? extends Component> type){
        return components.containsKey(type);
    }


}
