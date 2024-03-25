package com.marwinekk.armortrims.entity;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import com.marwinekk.armortrims.ModTags;
import com.marwinekk.armortrims.ducks.PhysicsCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class BlockBreakerArrow extends AbstractArrow implements PhysicsCheck {
    private static final EntityDataAccessor<Byte> BLOCK_BREAKS_LEFT = SynchedEntityData.defineId(BlockBreakerArrow.class, EntityDataSerializers.BYTE);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BLOCK_BREAKS_LEFT, (byte)3);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("BlockBreaksLeft", this.getBlockBreaksLeft());
    }

    public byte getBlockBreaksLeft() {
        return this.entityData.get(BLOCK_BREAKS_LEFT);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setBlockBreaksLeft(tag.getByte("BlockBreaksLeft"));
    }

    public void setBlockBreaksLeft(byte blockBreaksLeft) {
        this.entityData.set(BLOCK_BREAKS_LEFT, blockBreaksLeft);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        BlockPos hitPos = hitResult.getBlockPos();
        byte blockBreaksLeft = this.getBlockBreaksLeft();
        if(blockBreaksLeft > 0 && !this.level().getBlockState(hitPos).is(ModTags.DIAMOND_ARROW_IMMUNE)){
            if(!this.level().isClientSide){
                this.level().destroyBlock(hitPos, true, this);
                this.setBlockBreaksLeft((byte) (blockBreaksLeft - 1));
            }
        } else{
            super.onHitBlock(hitResult);
            if(!this.level().isClientSide){
                this.discard();
            }
        }
    }

    @Override
    public boolean canBypassInGround() {
        return this.getBlockBreaksLeft() > 0;
    }
}
