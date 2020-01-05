package xyz.theasylum.zendarva.action;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.component.Carryable;
import xyz.theasylum.zendarva.component.Inventory;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;

import java.awt.*;

public class ActionPickupItem implements Action {

    private Entity item, entity;

    public ActionPickupItem(Entity entity, Entity item) {
        this.item = item;
        this.entity = entity;
    }

    @Override
    public boolean performAction(Game game, Floor floor) {

        if (item.hasComponent(Carryable.class) && entity.hasComponent(Inventory.class)){
            Inventory i = entity.getComponent(Inventory.class).get();
            i.addItem(item);
            item.loc = new Point(-1,-1);
            EventBus.instance().raiseEvent(new EventEntity.EventEntityTakeItem(entity,item));
            return true;
        }
        return false;
    }

    @Override
    public Entity performedBy() {
        return null;
    }
}
