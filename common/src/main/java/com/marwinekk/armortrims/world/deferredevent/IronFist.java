package com.marwinekk.armortrims.world.deferredevent;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class IronFist extends DeferredEvent {

    private UUID uuid;

    public IronFist() {
        super(DeferredEventTypes.IRON_FIST);
    }

    public IronFist(LivingEntity livingEntity) {
        this();
        this.uuid = livingEntity.getUUID();
    }

    @Override
    public boolean attemptRun(ServerLevel level) {
        Entity entity = level.getEntity(uuid);
        if (entity != null) {
            entity.setDeltaMovement(new Vec3(0, 3, 0));
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (entity.getX()), (entity.getY() + 0.5), (entity.getZ()), 50, 0.15, 0.15, 0.15, 0.1);
        }
        return true;
    }

    @Override
    public void writeWithoutMetaData(CompoundTag tag) {
        tag.putUUID("player", uuid);
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        uuid = tag.getUUID("player");
    }
}
