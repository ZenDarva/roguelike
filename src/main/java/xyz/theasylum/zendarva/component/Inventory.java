package xyz.theasylum.zendarva.component;

import com.google.gson.annotations.Expose;
import xyz.theasylum.zendarva.domain.Entity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory implements Component {
    @Expose
    List<Entity> inventory;



    public Inventory() {
        this.inventory = new ArrayList<>();

    }

    public void addItem(Entity item){
        inventory.add(item);
    }

    public void removeItem(Entity item){
        inventory.remove(item);
    }

    public List<Entity> getItems(){
        return Collections.unmodifiableList(inventory);
    }

    public int getWeight(){
        return inventory.stream().filter(f->f.hasComponent(Carryable.class)).map(f->f.getComponent(Carryable.class).get()).map(f->f.weight).collect(Collectors.summingInt(Integer::intValue));
    }


}
