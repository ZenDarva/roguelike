package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;

public interface Action {

    boolean performAction(Game game, Floor floor);

    Entity performedBy();
}
