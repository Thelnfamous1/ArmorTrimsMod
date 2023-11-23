
package com.marwinekk.armortrims.entity;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTArrowEntity extends AbstractArrow implements ItemSupplier {


	public TNTArrowEntity(EntityType<? extends AbstractArrow> type, Level world) {
		super(type, world);
	}



	public TNTArrowEntity(EntityType<? extends TNTArrowEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public TNTArrowEntity(Level world, LivingEntity entity) {
		super(ArmorTrimsModEntities.TNT_ARROW, entity, world);
	}


	@Override
	public ItemStack getItem() {
		return new ItemStack(Blocks.TNT);
	}

	@Override
	protected ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		Vec3 hitPos = entityHitResult.getLocation();
		level().explode(null, hitPos.x, hitPos.y, hitPos.z, 3, Level.ExplosionInteraction.NONE);
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);

		if (!level().isClientSide) {
			Vec3 hitPos = blockHitResult.getLocation();
			level().explode(null, hitPos.x, hitPos.y, hitPos.z, 3, Level.ExplosionInteraction.NONE);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.inGround)
			this.discard();
	}

}
