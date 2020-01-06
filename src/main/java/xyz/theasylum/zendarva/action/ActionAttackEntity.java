package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;

import java.util.Optional;

public class ActionAttackEntity implements Action{

    private final Entity from;
    private final Entity targ;

    public ActionAttackEntity(Entity from, Entity targ){

        this.from = from;
        this.targ = targ;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        CombatStats targStats = targ.getComponent(CombatStats.class).get();
        CombatStats fromStats = from.getComponent(CombatStats.class).get();
        if (targStats == null || fromStats == null|| !fromStats.isActive() || !targStats.isActive()){
            return false;
        }
        int dmg = GameState.instance().rnd.nextInt(fromStats.getDamage());
        targStats.doDamage(dmg);
        EventBus.instance().raiseEvent(new EventEntity.EventDamageEntity(targ,dmg));
        if (targStats.getHp() <= 0){
            targStats.setActive(false);
            EventBus.instance().raiseEvent(new EventEntity.EventEntityDie(targ,from));
        }
        return true;
    }

    @Override
    public Entity performedBy() {
        return from;
    }
}
