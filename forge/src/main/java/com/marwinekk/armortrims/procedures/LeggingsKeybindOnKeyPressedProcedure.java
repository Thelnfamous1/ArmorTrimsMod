package com.marwinekk.armortrims.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

public class LeggingsKeybindOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			double _setval = 3;
			entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.combatpressed = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
