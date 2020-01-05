package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.action.ActionAttackEntity;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.action.ActionWait;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class BehaviorSmartZombie extends Behavior   {
    private transient Entity entity;

    private transient BehaviorWander wander;

    public BehaviorSmartZombie(Entity entity){

        this.entity = entity;
        wander = new BehaviorWander(entity);
    }

    public BehaviorSmartZombie() {
        wander = new BehaviorWander();
    }

    @Override
    public Optional<Action> execute(Floor floor, Game game) {
        int x = entity.loc.x;
        int y = entity.loc.y;
        Point playerLoc = GameState.instance().player.loc;
        CombatStats stats = entity.getComponent(CombatStats.class).get();
        if (stats == null){
            //I have no stats, how can i fight??
            return Optional.empty();
        }

        if (entity.loc.distance(playerLoc) > 4) {
            if (GameState.instance().rnd.nextFloat()>.5 && stats.getHp() < stats.getMaxHp()){
                return Optional.of(new ActionWait(entity));
            }
            return wander.execute(floor, game);
        }
        else if (entity.loc.distance(playerLoc) == 1) {
            return Optional.of(new ActionAttackEntity(entity,GameState.instance().player));
        }
        else{

            for (int i = 0; i < 4; i ++) {
                Point point = generateMove(x, y, GameState.instance().player);
                if (floor.canMove(entity, point.x, point.y))
                    return Optional.of(new ActionMoveEntity(entity, point));
            }

        }

        return Optional.empty();
    }
    private Point generateMove(int x, int y, Entity targ){

        int localX=x;
        int localY =y;

        Random rnd = new Random();
        if (rnd.nextInt(2) > 0) {
            if (entity.loc.x < targ.loc.x) {
                localX += 1;
            } else if (entity.loc.x > targ.loc.x) {
                localX -= 1;
            }
        }
        else {
            if (entity.loc.y < targ.loc.y) {
                localY += 1;
            } else if (entity.loc.y > targ.loc.y) {
                localY -= 1;
            }
        }
        return new Point(localX,localY);
    }

}
