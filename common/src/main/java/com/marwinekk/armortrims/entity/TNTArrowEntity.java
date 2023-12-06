
package com.marwinekk.armortrims.entity;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import com.marwinekk.armortrims.ducks.PhysicsCheck;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

public class TNTArrowEntity extends AbstractArrow implements ItemSupplier, PhysicsCheck {
	private static final EntityDataAccessor<OptionalInt> DATA_HOMING_TARGET_ID = SynchedEntityData.defineId(TNTArrowEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
	private static final EntityDataAccessor<Float> DATA_SHOT_VELOCITY = SynchedEntityData.defineId(TNTArrowEntity.class, EntityDataSerializers.FLOAT);
	private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(1024.0D);
	private static final float EXPLOSION_RADIUS = 1.0F;
	@Nullable
	private Entity homingTarget;
	private ItemStack tntItem;


	public TNTArrowEntity(EntityType<? extends AbstractArrow> type, Level world) {
		super(type, world);
		this.tntItem = new ItemStack(Blocks.TNT);
	}



	public TNTArrowEntity(EntityType<? extends TNTArrowEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public TNTArrowEntity(Level world, LivingEntity entity) {
		super(ArmorTrimsModEntities.TNT_ARROW, entity, world);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_HOMING_TARGET_ID, OptionalInt.empty());
		this.entityData.define(DATA_SHOT_VELOCITY, 0.0F);
	}

	@Override
	public ItemStack getItem() {
		return this.tntItem;
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
	protected void onHit(HitResult $$0) {
		super.onHit($$0);
		this.discard();
	}

	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		if(!this.level().isClientSide){
			Vec3 hitPos = entityHitResult.getLocation();
			level().explode(null, hitPos.x, hitPos.y, hitPos.z, EXPLOSION_RADIUS, Level.ExplosionInteraction.NONE);
		}
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);

		if (!level().isClientSide) {
			this.setHomingTarget(null);
			Vec3 hitPos = blockHitResult.getLocation();
			level().explode(null, hitPos.x, hitPos.y, hitPos.z, EXPLOSION_RADIUS, Level.ExplosionInteraction.NONE);
		}
	}

	@Override
	public void tick() {
		if(this.hasHomingTarget()){
			Entity homingTarget = this.getHomingTarget();
			if (homingTarget != null && !this.inGround) {
				float shotVelocity = this.getShotVelocity();
				Vec3 positionDelta = homingTarget.getEyePosition().subtract(this.position());
				this.setPosRaw(this.getX(), this.getY() + positionDelta.y * 0.015D * (double)shotVelocity, this.getZ());
				if (this.level().isClientSide) {
					this.yOld = this.getY();
				}

				double stepScale = 0.05D * (double)shotVelocity;
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(positionDelta.normalize().scale(stepScale)));

				Vec3 deltaMovement = this.getDeltaMovement();
				double xNew = this.getX() + deltaMovement.x;
				double yNew = this.getY() + deltaMovement.y;
				double zNew = this.getZ() + deltaMovement.z;
				this.level().addParticle(ParticleTypes.SMOKE, xNew - deltaMovement.x * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, yNew - deltaMovement.y * 0.25D - 0.5D, zNew - deltaMovement.z * 0.25D + this.random.nextDouble() * 0.6D - 0.3D, deltaMovement.x, deltaMovement.y, deltaMovement.z);
			}
		} else if(!this.level().isClientSide){
			LivingEntity target = this.level().getNearestEntity(LivingEntity.class, TARGETING_CONDITIONS, this.getShooter(), this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(16.0D));
			if(target != null) this.setHomingTarget(target);
		}

		super.tick();
	}

	@Nullable
	private LivingEntity getShooter(){
		Entity owner = this.getOwner();
		if(owner instanceof LivingEntity shooter) return shooter;
		return null;
	}

	public void setHomingTarget(@Nullable Entity entity) {
		this.entityData.set(DATA_HOMING_TARGET_ID, entity == null ? OptionalInt.empty() : OptionalInt.of(entity.getId()));
	}

	@Nullable
	public Entity getHomingTarget(){
		if (this.homingTarget == null) {
			this.entityData.get(DATA_HOMING_TARGET_ID).ifPresent((id) -> this.homingTarget = this.level().getEntity(id));
		}
		return this.homingTarget;
	}

	private boolean hasHomingTarget() {
		return this.entityData.get(DATA_HOMING_TARGET_ID).isPresent();
	}

	@Override
	public void playerTouch(Player pEntity) {
		if (!this.hasHomingTarget() || this.inGround) {
			super.playerTouch(pEntity);
		}
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		if(this.hasHomingTarget() && entity == this.getOwner()){
			return false;
		}

		return super.canHitEntity(entity);
	}

	@Override
	public boolean canBypassGravity() {
		return this.hasHomingTarget();
	}

	@Override
	public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
		super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
		if(!this.level().isClientSide) this.setShotVelocity(pVelocity);
	}

	@Override
	public void shootFromRotation(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
		super.shootFromRotation($$0, $$1, $$2, $$3, $$4, $$5);
		if(!this.level().isClientSide) this.setShotVelocity($$4);
	}

	private float getShotVelocity(){
		return this.entityData.get(DATA_SHOT_VELOCITY);
	}

	private void setShotVelocity(float pVelocity) {
		this.entityData.set(DATA_SHOT_VELOCITY, pVelocity);
	}

}
