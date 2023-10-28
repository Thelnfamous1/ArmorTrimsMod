package com.marwinekk.armortrims.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

public class Pa3ProjectileHitsBlockProcedure {
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
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.break_wooden_door")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.break_wooden_door")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
		}
	}
}
