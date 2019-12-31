package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.Map;

public class ActionWait implements Action {

    private Entity entity;

    public ActionWait(Entity entity){

        this.entity = entity;
    }

    @Override
    public boolean performAction(Game game, Map map) {
        if (!map.entities.stream().filter(f->f!=entity).anyMatch(f->f.loc.distance(entity.loc) <5)){
            entity.hp+=1;
            if (entity.hp > entity.maxHp){
                entity.hp = entity.maxHp;
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public Entity performedBy() {
        return null;
    }
}
