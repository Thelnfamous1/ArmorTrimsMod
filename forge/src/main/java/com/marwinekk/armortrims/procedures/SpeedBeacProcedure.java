package com.marwinekk.armortrims.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

public class SpeedBeacProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (("speed").equals((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).beaconeffect)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.MOVEMENT_SPEED);
		} else if (("haste").equals((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).beaconeffect)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.DIG_SPEED);
		} else if (("strength").equals((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).beaconeffect)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.DAMAGE_BOOST);
		} else if (("resistance").equals((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).beaconeffect)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.DAMAGE_RESISTANCE);
		} else if (("jump").equals((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).beaconeffect)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.JUMP);
		} else if (("hero").equals((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).beaconeffect)) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(MobEffects.HERO_OF_THE_VILLAGE);
		}
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999999, 0, true, false));
		{
			String _setval = "speed";
			entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.beaconeffect = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
