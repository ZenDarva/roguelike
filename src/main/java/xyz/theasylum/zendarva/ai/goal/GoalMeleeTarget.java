package xyz.theasylum.zendarva.ai.goal;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.action.ActionWait;
import xyz.theasylum.zendarva.ai.map.BehaviorMap;
import xyz.theasylum.zendarva.ai.map.BehaviorMapManager;
import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GoalMeleeTarget extends Goal {
    public double ferocity= 1.0;
    public int siteRange =5;

    private transient Entity target;

    Function<Entity, Action> blah;

    public GoalMeleeTarget() {
        this.setDesiredMap("player");
    }

    @Override
    public int calculatePriority(Entity entity) {
        Floor floor = GameState.instance().getCurFloor();

        Optional<CombatStats> stats = entity.getComponent(CombatStats.class);
        //Why... would we want to attack an enemy if we have no combat stats??
        if (stats.isPresent()==false){
            return -10;
        }

        Optional<Entity> targ = getAttackableEntity(entity);
        if (!targ.isPresent()){
            return -10;
        }
        target = targ.get();
        return (int) (10 * ferocity);
    }

    private Optional<Entity> getAttackableEntity(Entity entity){
        Set<Entity> entities = GameState.instance().getCurFloor().getEntitiesWithinRange(entity.loc, siteRange);
        if (entities.isEmpty())
                return Optional.empty();
        Optional<CombatStats> stats = entity.getComponent(CombatStats.class);
        if (!stats.isPresent()){
            return Optional.empty();
        }
        List<Entity> possibleTargets = new ArrayList<>();
        for (Entity targ : entities.stream().filter(f->f.hasComponent(CombatStats.class)).collect(Collectors.toList())) {
            CombatStats targStats = targ.getComponent(CombatStats.class).get();

            if (targStats.getTeam() != stats.get().getTeam()){
                possibleTargets.add(targ);
            }
        }
        if (possibleTargets.isEmpty())
                return Optional.empty();
        return Optional.of(possibleTargets.get(GameState.instance().rnd.nextInt(possibleTargets.size())));
    }

    @Override
    public Optional<String> getDesiredMap() {
        if (target== GameState.instance().player)
            return super.getDesiredMap();
        return Optional.empty();
    }

    @Override
    public void processAction(Entity entity) {
        if (target == null){
            //what are we doing here?
            return;
        }
        BehaviorMap map = BehaviorMapManager.instance().generateFromEntities("temporary", GameState.instance().getCurFloor(),Collections.singletonList(target));
        Optional<Point> point = map.getBestMoveFrom(entity.loc);
        point.ifPresentOrElse(f-> Game.actionQueue.add(new ActionMoveEntity(entity,f)), ()->Game.actionQueue.add(new ActionWait(entity)));
    }
}
