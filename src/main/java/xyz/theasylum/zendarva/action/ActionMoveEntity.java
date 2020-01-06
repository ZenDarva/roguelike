package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.Optional;

public class ActionMoveEntity implements Action {

    private final Entity entity;
    private final Point targLoc;

    public ActionMoveEntity(Entity entity, Point targLoc){
        this.entity = entity;
        this.targLoc = targLoc;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        Optional<Entity> targEnemy = floor.getEntity(targLoc);

        targEnemy.ifPresentOrElse(this::attackEnemy, ()->floor.moveEntity(entity,targLoc.x,targLoc.y));

        return true;

    }

    private void attackEnemy(Entity targEntity){
        if (targEntity.hasComponent(CombatStats.class)){
            Game.actionQueue.add(new ActionAttackEntity(entity,targEntity));
        }
        else
            GameState.instance().getCurFloor().moveEntity(entity,targLoc.x,targLoc.y);
    }

    @Override
    public Entity performedBy() {
        return entity;
    }
}
