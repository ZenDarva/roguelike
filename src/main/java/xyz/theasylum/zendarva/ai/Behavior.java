package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;

import java.util.Optional;

public abstract class Behavior implements Component {


    boolean wanders = true;
    String targMap;
    protected int los;
    protected int desiredDistance =0 ;

    public int getDesiredDistance() {
        return desiredDistance;
    }

    public void setDesiredDistance(int desiredDistance) {
        this.desiredDistance = desiredDistance;
    }

    public String getTargMap() {
        return targMap;
    }

    public void setTargMap(String targMap) {
        this.targMap = targMap;
    }

    public int getLos() {
        return los;
    }

    public void setLos(int los) {
        this.los = los;
    }
}
