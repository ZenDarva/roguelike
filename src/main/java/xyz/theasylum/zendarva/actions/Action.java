package xyz.theasylum.zendarva.actions;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;

public interface Action {

    public boolean performAction(Game game, Map map);
}
