package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;

import java.awt.*;

public class ActionMoveEntity implements Action {

    private final Entity entity;
    private final Point targLoc;

    public ActionMoveEntity(Entity entity, Point targLoc){
        this.entity = entity;
        this.targLoc = targLoc;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        return floor.moveEntity(entity,targLoc.x,targLoc.y);

    }

    @Override
    public Entity performedBy() {
        return entity;
    }
}
