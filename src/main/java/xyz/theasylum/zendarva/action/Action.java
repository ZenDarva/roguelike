package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;
import xyz.theasylum.zendarva.domain.Floor;

public interface Action {

    public boolean performAction(Game game, Floor floor);

    public Entity performedBy();
}
