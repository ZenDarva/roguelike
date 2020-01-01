package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Floor;

public class ActionWait implements Action {

    private Entity entity;

    public ActionWait(Entity entity){

        this.entity = entity;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {
        if (!floor.getEntities().stream().filter(f->f!=entity).anyMatch(f->f.loc.distance(entity.loc) <5)){
            if (entity !=game.player)
                System.out.println("Sleeping.");
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
        return entity;
    }
}
