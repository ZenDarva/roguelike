package xyz.theasylum.zendarva.domain;

import com.google.gson.annotations.Expose;
import xyz.theasylum.zendarva.component.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Optional;

public class Entity {
    public Point loc;
    public String name;

    private HashMap<String, Component> components = new HashMap<>();

    public <T extends Component> Optional<T>  getComponent(Class<T> type){
        if (components.containsKey(type.getCanonicalName())){
            return (Optional<T>) Optional.of(components.get(type.getCanonicalName()));
        }
        return Optional.empty();
    }

    public void addComponent(Class type, Component component){
        components.put(type.getCanonicalName(), component);
    }

    public boolean hasComponent(Class<? extends Component> type){
        return components.containsKey(type.getCanonicalName());
    }


}
