
package com.marwinekk.armortrims.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import com.marwinekk.armortrims.procedures.PotionThrowProjectileHitsLivingEntityProcedure;
import com.marwinekk.armortrims.init.ArmorTrimsModEntities;

public class PotionThrowEntity extends AbstractArrow implements ItemSupplier {
	public PotionThrowEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(ArmorTrimsModEntities.POTION_THROW.get(), world);
	}

	public PotionThrowEntity(EntityType<? extends PotionThrowEntity> type, Level world) {
		super(type, world);
	}

	public PotionThrowEntity(EntityType<? extends PotionThrowEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public PotionThrowEntity(EntityType<? extends PotionThrowEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return new ItemStack(Items.SPLASH_POTION);
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
		PotionThrowProjectileHitsLivingEntityProcedure.execute(entityHitResult.getEntity(), this.getOwner());
	}

	@Override
	public void tick() {
		super.tick();

		if (!level().isClientSide) {
			((ServerLevel)level()).sendParticles(ParticleTypes.ENTITY_EFFECT, getX(), getY(), getZ(), 25, 0.15, 0.15, 0.15, 0.1);
		}

		if (this.inGround)
			this.discard();
	}

	public static PotionThrowEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		PotionThrowEntity entityarrow = new PotionThrowEntity(ArmorTrimsModEntities.POTION_THROW.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(true);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WITCH_THROW, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static PotionThrowEntity shoot(LivingEntity entity, LivingEntity target) {
		PotionThrowEntity entityarrow = new PotionThrowEntity(ArmorTrimsModEntities.POTION_THROW.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 3f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(1);
		entityarrow.setKnockback(0);
		entityarrow.setCritArrow(true);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),SoundEvents.WITCH_THROW, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
