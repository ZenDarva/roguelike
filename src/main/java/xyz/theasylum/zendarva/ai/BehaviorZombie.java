package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.action.ActionAttackEntity;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class BehaviorZombie extends Behavior {

    private transient Entity entity;

    private transient BehaviorWander wander;

    public BehaviorZombie(Entity entity){

        this.entity = entity;
        wander = new BehaviorWander(entity);
    }

    public BehaviorZombie() {
    }

    @Override
    public Optional<Action> execute(Floor floor, Game game) {
        int x = entity.loc.x;
        int y = entity.loc.y;
        Point playerLoc = GameState.instance().player.loc;
        if (entity.loc.distance(playerLoc) > 4)
            return wander.execute(floor,game);
        else if (entity.loc.distance(playerLoc) == 1) {
            return Optional.of(new ActionAttackEntity(entity,GameState.instance().player));
        }
        else{
            Random rnd = new Random();
            if (rnd.nextInt(2) > 0) {
                if (entity.loc.x < playerLoc.x) {
                    x += 1;
                } else if (entity.loc.x > playerLoc.x) {
                    x -= 1;
                }
            }
            else {
                if (entity.loc.y < playerLoc.y) {
                    y += 1;
                } else if (entity.loc.y > playerLoc.y) {
                    y -= 1;
                }
            }
                return Optional.of(new ActionMoveEntity(entity,new Point(x,y)));

        }
    }
}
