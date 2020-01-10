package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.action.ActionWait;
import xyz.theasylum.zendarva.ai.goal.Goal;
import xyz.theasylum.zendarva.ai.map.BehaviorMap;
import xyz.theasylum.zendarva.ai.map.BehaviorMapManager;
import xyz.theasylum.zendarva.component.Carryable;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BehaviorSystem implements ITickable {


    @Override
    public void update() {
        BehaviorMapManager.instance().generateFromPoints("player", GameState.instance().getCurFloor(), Collections.singletonList(GameState.instance().player.loc));
        BehaviorMapManager.instance().generateFromEntities("keys",GameState.instance().getCurFloor(), GameState.instance().getCurFloor().getEntities().stream().filter(f -> f.hasComponent(Carryable.class)).collect(Collectors.toList()));

        for (Entity entity : GameState.instance().getCurFloor().getEntities().stream().filter(f->f.hasComponent(Behavior.class)).collect(Collectors.toList())){
            Behavior behavior = entity.getComponent(Behavior.class).get();
            Optional<Goal> goal = behavior.getGoals().stream().sorted((o1,o2)->Integer.compare(o2.calculatePriority(entity),o1.calculatePriority(entity))).findFirst();
                if (!goal.isPresent()){ //indecisive little bugger aren't ya?
                    Game.actionQueue.add(new ActionWait(entity));
                    continue;
                }
                //The goal has a special action it wants to take.  Let it.
                if(!goal.get().getDesiredMap().isPresent()){
                           goal.get().processAction(entity);
                    continue;
                }
                BehaviorMap map = BehaviorMapManager.instance().getMap(goal.get().getDesiredMap().get());
                Optional<Point> point = map.getBestMoveFrom(entity.loc);
                point.ifPresent(f->Game.actionQueue.add(new ActionMoveEntity(entity,f)));
        }

    }


}
