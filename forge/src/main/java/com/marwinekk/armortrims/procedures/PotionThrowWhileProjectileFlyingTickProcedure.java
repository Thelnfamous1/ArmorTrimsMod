package com.marwinekk.armortrims.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class PotionThrowWhileProjectileFlyingTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 25, 0.15, 0.15, 0.15, 0.1);
	}
}
