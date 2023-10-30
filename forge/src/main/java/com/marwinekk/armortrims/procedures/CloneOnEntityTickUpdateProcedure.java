package com.marwinekk.armortrims.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import java.util.Comparator;

import com.marwinekk.armortrims.init.ArmorTrimsModEntities;
import com.marwinekk.armortrims.entity.PotionThrowEntity;

public class CloneOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof TamableAnimal _tamEnt && _tamEnt.isTame())) {
			if (entity instanceof TamableAnimal _toTame && ((Entity) world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 16, 16, 16), e -> true).stream().sorted(new Object() {
				Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
					return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
				}
			}.compareDistOf(x, y, z)).findFirst().orElse(null)) instanceof Player _owner)
				_toTame.tame(_owner);
		}
		if (entity.getPersistentData().getDouble("timerThrowGoodStuff") == 0) {
			entity.getPersistentData().putDouble("timerThrowGoodStuff", (Mth.nextInt(RandomSource.create(), 20, 60)));
		} else {
			entity.getPersistentData().putDouble("timerThrowGoodStuff", (entity.getPersistentData().getDouble("timerThrowGoodStuff") - 1));
		}
		if (entity.getPersistentData().getDouble("timerThrowGoodStuff") == 0) {
			if (world instanceof ServerLevel projectileLevel) {
				Entity targetEntity = entity instanceof TamableAnimal _tamEnt ? _tamEnt.getOwner() : null;
				if (targetEntity != null) {
					Projectile _entityToSpawn = new Object() {
						public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
							AbstractArrow entityToSpawn = new PotionThrowEntity(ArmorTrimsModEntities.POTION_THROW.get(), level);
							entityToSpawn.setOwner(shooter);
							entityToSpawn.setCritArrow(true);
							entityToSpawn.setBaseDamage(damage);
							entityToSpawn.setKnockback(knockback);
							entityToSpawn.setSilent(true);
							return entityToSpawn;
						}
					}.getArrow(projectileLevel, entity, (float) 0, 0);
					_entityToSpawn.setPos(x, y, z);
					double dX = targetEntity.getX() - x;
					double dY = targetEntity.getY() - y;
					double dZ = targetEntity.getZ() - z;
					double distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
					_entityToSpawn.shoot(dX / distance, dY / distance, dZ / distance, 3, 0);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
		}
	}
}
