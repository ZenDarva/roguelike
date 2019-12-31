package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.domain.Floor;

import java.util.Optional;

public interface Behavior {

    public Optional<Action> execute(Floor floor, Game game);
}
