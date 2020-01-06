package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.ai.map.BehaviorMap;
import xyz.theasylum.zendarva.ai.map.BehaviorMapGenerator;
import xyz.theasylum.zendarva.component.Carryable;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class BehaviorSystem implements ITickable {
    BehaviorMapGenerator generator = new BehaviorMapGenerator();

    private BehaviorMap playerMap;
    private BehaviorMap keyMap;

    @Override
    public void update() {
        playerMap = generator.generateFromPoints(GameState.instance().getCurFloor(), Collections.singletonList(GameState.instance().player.loc));
        keyMap = generator.generateFromEntities(GameState.instance().getCurFloor(),GameState.instance().getCurFloor().getEntities().stream().filter(f->f.hasComponent(Carryable.class)).collect(Collectors.toList()));

        for (Entity entity : GameState.instance().getCurFloor().getEntities()) {
            //TODO: refactor this to take advantage of optional.
            if (!entity.hasComponent(Behavior.class)){
                continue;
            }
            Behavior behavior = entity.getComponent(Behavior.class).get();
            if (playerMap.getValue(entity.loc.x,entity.loc.y) <= behavior.getLos()){
                Optional<Point> newLoc = playerMap.getBestMoveFrom(entity.loc);
                newLoc.ifPresent(f-> Game.actionQueue.add(new ActionMoveEntity(entity,f)));
            }
        }
    }
}
