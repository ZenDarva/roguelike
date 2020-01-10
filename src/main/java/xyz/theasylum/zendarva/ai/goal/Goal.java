package xyz.theasylum.zendarva.ai.goal;

import xyz.theasylum.zendarva.domain.Entity;

import java.util.Optional;

public abstract class Goal {
    protected String desiredMap;
    public abstract int calculatePriority(Entity entity);

    public Optional<String> getDesiredMap() {
        return Optional.ofNullable(desiredMap);
    }

    public void setDesiredMap(String desiredMap) {
        this.desiredMap = desiredMap;
    }


    /**
     * This function is only used if desiredMap is null.  Otherwise, the Behavior system will grab the desiredMap
     * and move the entity to the best available position.
     *
     * @param entity
     */
    public void processAction(Entity entity){

    }
}
