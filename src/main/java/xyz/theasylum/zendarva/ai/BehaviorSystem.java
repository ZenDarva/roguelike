package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.ai.map.BehaviorMap;
import xyz.theasylum.zendarva.ai.map.BehaviorMapManager;
import xyz.theasylum.zendarva.component.Carryable;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class BehaviorSystem implements ITickable {
    BehaviorMapManager mapManager = new BehaviorMapManager();

    @Override
    public void update() {
        mapManager.generateFromPoints("player", GameState.instance().getCurFloor(), Collections.singletonList(GameState.instance().player.loc));
        mapManager.generateFromEntities("keys",GameState.instance().getCurFloor(), GameState.instance().getCurFloor().getEntities().stream().filter(f -> f.hasComponent(Carryable.class)).collect(Collectors.toList()));

        for (Entity entity : GameState.instance().getCurFloor().getEntities()) {
            //TODO: refactor this to take advantage of optional.
            if (!entity.hasComponent(Behavior.class)) {
                continue;
            }
            Behavior behavior = entity.getComponent(Behavior.class).get();
            BehaviorMap map = mapManager.getMap(behavior.targMap);
            if (map.getValue(entity.loc.x, entity.loc.y) <= behavior.getLos()&& map.getValue(entity.loc.x,entity.loc.y) > behavior.desiredDistance)  {
                Optional<Point> newLoc = map.getBestMoveFrom(entity.loc);
                newLoc.ifPresent(f -> Game.actionQueue.add(new ActionMoveEntity(entity, f)));
            } else {
                if (behavior.wanders) {
                    Random rnd =GameState.instance().rnd;
                    List<Point> walkables = GameState.instance().getCurFloor().getWalkableNeighbors(entity.loc);
                    if (walkables.isEmpty()){
                        return;
                    }
                    int index = rnd.nextInt(walkables.size());

                    Game.actionQueue.add(new ActionMoveEntity(entity, walkables.get(index)));
                }


            }
        }
    }
}
