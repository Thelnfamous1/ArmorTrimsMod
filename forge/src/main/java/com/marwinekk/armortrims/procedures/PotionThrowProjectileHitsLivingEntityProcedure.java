package com.marwinekk.armortrims.procedures;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class PotionThrowProjectileHitsLivingEntityProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (!(entity == (sourceentity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null))) {
			if (Math.random() < 0.5) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
			} else {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
			}
		} else if (entity == (sourceentity instanceof TamableAnimal _tamEnt ? (Entity) _tamEnt.getOwner() : null)) {
			if (Math.random() < 0.5) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			} else {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 40, 0));
			}
		}
	}
}
