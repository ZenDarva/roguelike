package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;

public class ActionAttackEntity implements Action{

    private final Entity from;
    private final Entity targ;

    public ActionAttackEntity(Entity from, Entity targ){

        this.from = from;
        this.targ = targ;
    }

    @Override
    public boolean performAction(Game game, Map map) {
        targ.hp--;
        System.out.println("Have at you!");
        return true;
    }

    @Override
    public Entity performedBy() {
        return from;
    }
}
