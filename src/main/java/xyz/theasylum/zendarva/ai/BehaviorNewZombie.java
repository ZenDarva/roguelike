package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.domain.Floor;

import java.util.Optional;

public class BehaviorNewZombie extends Behavior {
    @Override
    public Optional<Action> execute(Floor floor, Game game) {
        return Optional.empty();
    }

    public BehaviorNewZombie() {
        this.los=7;
    }
}
