package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;

import java.util.Optional;

public class ActionWait implements Action {

    public static final int SAFE_DISTANCE = 5;
    private Entity entity;

    public ActionWait(Entity entity) {

        this.entity = entity;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        Optional<CombatStats> stats = entity.getComponent(CombatStats.class);
        if (!stats.isPresent()) {
            return false;
        }
        if (!floor.getEntities().stream().filter(f -> f.hasComponent(CombatStats.class)).filter(f->f!= entity).anyMatch(f -> f.loc.distance(entity.loc) < SAFE_DISTANCE)) {
            stats.ifPresent(f -> f.heal(1));
            return true;
        }
        return false;
    }

    @Override
    public Entity performedBy() {
        return entity;
    }
}
