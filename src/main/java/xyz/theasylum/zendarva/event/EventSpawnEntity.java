package xyz.theasylum.zendarva.event;

import xyz.theasylum.zendarva.Entity;

public class EventSpawnEntity extends Event {

    private Entity entity;

    public EventSpawnEntity(Entity entity) {

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
