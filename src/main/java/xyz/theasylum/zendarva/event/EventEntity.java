package xyz.theasylum.zendarva.event;

import xyz.theasylum.zendarva.domain.Entity;

public class EventEntity extends Event {

    protected Entity entity;

    public EventEntity(Entity entity) {

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public static class EventSpawnEntity extends EventEntity {


        public EventSpawnEntity(Entity entity) {
            super(entity);
        }
    }

    public static class EventEntityDie  extends EventEntity{

        private Entity killer;

        public EventEntityDie(Entity entity, Entity killer) {
            super(entity);
            this.killer = killer;
        }

        public Entity getKiller() {
            return killer;
        }
    }

    public static class EventDamageEntity extends EventEntity{

        private final int amount;

        public EventDamageEntity(Entity entity, int amount) {
            super(entity);
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }
    }
}
