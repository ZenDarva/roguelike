package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;
import xyz.theasylum.zendarva.actions.Action;
import xyz.theasylum.zendarva.actions.ActionAttackEntity;
import xyz.theasylum.zendarva.actions.ActionMoveEntity;
import xyz.theasylum.zendarva.component.Component;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class BehaviorZombie implements Behavior, Component {

    private Entity entity;

    private BehaviorWander wander;

    public BehaviorZombie(Entity entity){

        this.entity = entity;
        wander = new BehaviorWander(entity);
    }

    @Override
    public Optional<Action> execute(Map map, Game game) {
        int x = entity.loc.x;
        int y = entity.loc.y;
        if (entity.loc.distance(game.player.loc) > 4)
            return wander.execute(map,game);
        else if (entity.loc.distance(game.player.loc) == 1) {
            return Optional.of(new ActionAttackEntity(entity,game.player));
        }
        else{
            Random rnd = new Random();
            if (rnd.nextInt(2) > 0) {
                if (entity.loc.x < game.player.loc.x) {
                    x += 1;
                } else if (entity.loc.x > game.player.loc.x) {
                    x -= 1;
                }
            }
            else {
                if (entity.loc.y < game.player.loc.y) {
                    y += 1;
                } else if (entity.loc.y > game.player.loc.y) {
                    y -= 1;
                }
            }
                return Optional.of(new ActionMoveEntity(entity,new Point(x,y)));

        }
    }
}
