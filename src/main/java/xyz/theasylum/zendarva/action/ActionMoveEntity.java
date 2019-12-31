package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;

import java.awt.*;

public class ActionMoveEntity implements Action {

    private final Entity entity;
    private final Point targLoc;

    public ActionMoveEntity(Entity entity, Point targLoc){

        this.entity = entity;
        this.targLoc = targLoc;
    }

    @Override
    public boolean performAction(Game game, Map map) {
        return map.moveEntity(entity,targLoc.x,targLoc.y);

    }

    @Override
    public Entity performedBy() {
        return entity;
    }
}
