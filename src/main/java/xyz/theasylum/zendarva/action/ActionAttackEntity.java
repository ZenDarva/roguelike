package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;
import xyz.theasylum.zendarva.domain.Floor;

public class ActionAttackEntity implements Action{

    private final Entity from;
    private final Entity targ;

    public ActionAttackEntity(Entity from, Entity targ){

        this.from = from;
        this.targ = targ;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        targ.hp-=Game.rnd.nextInt(2);
        return true;
    }

    @Override
    public Entity performedBy() {
        return from;
    }
}
