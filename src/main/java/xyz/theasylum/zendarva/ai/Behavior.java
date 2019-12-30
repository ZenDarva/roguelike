package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;
import xyz.theasylum.zendarva.actions.Action;

import java.util.Optional;

public interface Behavior {

    public Optional<Action> execute(Map map, Game game);
}
