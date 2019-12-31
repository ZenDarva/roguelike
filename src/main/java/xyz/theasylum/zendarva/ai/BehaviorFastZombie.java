package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.action.ActionAttackEntity;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.action.ActionWait;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class BehaviorFastZombie implements Behavior, Component {
    private Entity entity;

    private BehaviorWander wander;

    public BehaviorFastZombie(Entity entity){

        this.entity = entity;
        wander = new BehaviorWander(entity);
    }

    @Override
    public Optional<Action> execute(Floor floor, Game game) {
        int x = entity.loc.x;
        int y = entity.loc.y;
        if (entity.loc.distance(game.player.loc) > 4) {
            if (Game.rnd.nextFloat()>.5 && entity.hp < entity.maxHp){
                return Optional.of(new ActionWait(entity));
            }
            return wander.execute(floor, game);
        }
        else if (entity.loc.distance(game.player.loc) == 1) {
            return Optional.of(new ActionAttackEntity(entity,game.player));
        }
        else{

            for (int i = 0; i < 4; i ++) {
                Point point = generateMove(x, y, game.player);
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
