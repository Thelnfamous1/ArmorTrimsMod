package com.marwinekk.armortrims.world.deferredevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class DespawnLater extends DeferredEvent {
    private UUID uuid;
    public DespawnLater() {
        super(DeferredEventTypes.DESPAWN_LATER);
    }

    public DespawnLater(Entity entity) {
        this();
        this.uuid = entity.getUUID();
    }

    @Override
    public boolean attemptRun(ServerLevel level) {
        Entity entity = level.getEntity(uuid);
        if (entity != null) {
            entity.discard();
        }
        return true;
    }

    @Override
    public void writeWithoutMetaData(CompoundTag tag) {
        tag.putUUID("entity",uuid);
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        uuid = tag.getUUID("entity");
    }
}
