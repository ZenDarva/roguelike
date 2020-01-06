package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;

import java.util.Optional;

public abstract class Behavior implements Component {

    public abstract Optional<Action> execute(Floor floor, Game game);

    protected int los;

    public int getLos() {
        return los;
    }

    public void setLos(int los) {
        this.los = los;
    }
}
