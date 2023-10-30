package com.marwinekk.armortrims.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;
import com.marwinekk.armortrims.init.ArmorTrimsModEntities;
import com.marwinekk.armortrims.entity.Pa2Entity;
import com.marwinekk.armortrims.ArmorTrimsModForge;

public class PiercingArrowProjectileHitsBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity immediatesourceentity) {
		if (immediatesourceentity == null)
			return;



		if (!((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.BEDROCK || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.OBSIDIAN
				|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.END_PORTAL || (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.END_PORTAL_FRAME
				|| (world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == Blocks.NETHER_PORTAL)) {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			if (!immediatesourceentity.level().isClientSide())
				immediatesourceentity.discard();
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 25, 0.2, 0.2, 0.2, 0.05);
			ArmorTrimsModForge.queueServerWork(2, () -> {
				if (!world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 256, 256, 256), e -> true).isEmpty()
						&& (((Entity) world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 256, 256, 256), e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(x, y, z)).findFirst().orElse(null)).getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).diamond) {
					if (world instanceof ServerLevel projectileLevel) {
						Projectile _entityToSpawn = new Object() {
							public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
								AbstractArrow entityToSpawn = new Pa2Entity(ArmorTrimsModEntities.PA_2.get(), level);
								entityToSpawn.setOwner(shooter);
								entityToSpawn.setBaseDamage(damage);
								entityToSpawn.setKnockback(knockback);
								entityToSpawn.setSilent(true);
								entityToSpawn.setCritArrow(true);
								return entityToSpawn;
							}
						}.getArrow(projectileLevel, ((Entity) world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 256, 256, 256), e -> true).stream().min(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(x, y, z)).orElse(null)), 2, 0);
						_entityToSpawn.setPos(x, y, z);
						_entityToSpawn.shoot((world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 256, 256, 256), e -> true).stream().min(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(x, y, z)).orElse(null).getLookAngle().x), (world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 256, 256, 256), e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(x, y, z)).findFirst().orElse(null).getLookAngle().y), (world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 256, 256, 256), e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(x, y, z)).findFirst().orElse(null).getLookAngle().z), 2, 0);
						projectileLevel.addFreshEntity(_entityToSpawn);
					}
				}
			});
		}
	}
}
