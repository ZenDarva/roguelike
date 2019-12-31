package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;

public interface Action {

    public boolean performAction(Game game, Map map);

    public Entity performedBy();
}
