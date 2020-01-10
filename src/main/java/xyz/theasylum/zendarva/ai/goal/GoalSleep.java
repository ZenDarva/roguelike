package xyz.theasylum.zendarva.ai.goal;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.ActionWait;
import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.GameState;

import java.util.Optional;

public class GoalSleep extends Goal {
    @Override
    public int calculatePriority(Entity entity) {
        Optional<CombatStats> oStats = entity.getComponent(CombatStats.class);

        if (!oStats.isPresent())
           return 1;

        CombatStats stats = oStats.get();
        float percent = (float)stats.getHp()/(float)stats.getMaxHp();
        percent = percent * 100;
        int priority = (int) (10 - (percent * 11f)/100);

        if (GameState.instance().getCurFloor().getEntitiesWithinRange(entity.loc,ActionWait.SAFE_DISTANCE).stream().filter(f->f!=entity).findFirst().isPresent()){
            priority=priority-5;
        }
        return Math.max(1,priority);
    }

    @Override
    public void processAction(Entity entity) {
        Game.actionQueue.add(new ActionWait(entity));
    }
}
