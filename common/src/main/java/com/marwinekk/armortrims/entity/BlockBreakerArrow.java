package com.marwinekk.armortrims.entity;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class BlockBreakerArrow extends AbstractArrow {
    public BlockBreakerArrow(EntityType<? extends AbstractArrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    public BlockBreakerArrow(EntityType<? extends BlockBreakerArrow> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public BlockBreakerArrow(Level world, LivingEntity entity) {
        super(ArmorTrimsModEntities.BLOCK_BREAKER_ARROW, entity, world);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if(!this.level().isClientSide){
            this.level().destroyBlock($$0.getBlockPos(), true, this);
            this.discard();
        }
    }
}
