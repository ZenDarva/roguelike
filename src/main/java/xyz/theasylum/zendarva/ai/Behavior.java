package xyz.theasylum.zendarva.ai;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.ai.goal.Goal;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.domain.Floor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Behavior implements Component {
    private List<Goal> goals = new ArrayList<>();

    public void addGoal(Goal goal){
        goals.add(goal);
    }

    public void removeGoal(Goal goal){
        goals.remove(goal);
    }
    public List<Goal> getGoals(){
        return Collections.unmodifiableList(goals);
    }

}
