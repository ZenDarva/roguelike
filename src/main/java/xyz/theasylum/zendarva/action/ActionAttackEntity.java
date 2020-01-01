package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;

public class ActionAttackEntity implements Action{

    private final Entity from;
    private final Entity targ;

    public ActionAttackEntity(Entity from, Entity targ){

        this.from = from;
        this.targ = targ;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        int dmg = Game.rnd.nextInt(2);
        targ.hp-=dmg;
        EventBus.instance().raiseEvent(new EventEntity.EventDamageEntity(targ,dmg));
        if (targ.hp <= 0){
            EventBus.instance().raiseEvent(new EventEntity.EventEntityDie(targ,from));
        }
        return true;
    }

    @Override
    public Entity performedBy() {
        return from;
    }
}
