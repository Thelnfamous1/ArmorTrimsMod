package com.marwinekk.armortrims.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import com.marwinekk.armortrims.ArmorTrimsModForge;

public class BruteOnInitialEntitySpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		ArmorTrimsModForge.queueServerWork(12000, () -> {
			if (!entity.level().isClientSide())
				entity.discard();
		});
	}
}
