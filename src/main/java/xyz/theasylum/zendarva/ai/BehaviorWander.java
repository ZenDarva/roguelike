package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.Optional;

public class BehaviorWander extends Behavior  {

    private transient Entity entity;

    public BehaviorWander() {
    }

    public BehaviorWander(Entity entity){

        this.entity = entity;
    }

    @Override
    public Optional<Action> execute(Floor floor, Game game) {
        int rndMove = GameState.instance().rnd.nextInt(4);
        int x = entity.loc.x;
        int y = entity.loc.y;

        switch (rndMove){
            case 0:
                x=x-1;
                break;
            case 1:
                x=x+1;
                break;
            case 2:
                y=y-1;
                break;
            case 3:
                y=y+1;

        }
        if (floor.canMove(this.entity,x,y)){
            return Optional.of(new ActionMoveEntity(entity, new Point(x,y)));
        }
        return Optional.empty();
    }
}
