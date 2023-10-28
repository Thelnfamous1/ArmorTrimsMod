package com.marwinekk.armortrims.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class PassiveLapisProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).lapis) {
			if (entity.getPersistentData().getDouble("timerLvlUp") == 0) {
				if (entity instanceof Player _player)
					_player.giveExperienceLevels(1);
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.levelup")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.player.levelup")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.DRAGON_BREATH, x, (y + 1), z, 50, 0.25, 0.25, 0.25, 0.1);
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("[\u00A7c!\u00A7f] Level \u00A7agained\u00A7f!"), true);
			}
			if (entity.getPersistentData().getDouble("timerLvlUp") == 0) {
				entity.getPersistentData().putDouble("timerLvlUp", 18000);
			} else {
				entity.getPersistentData().putDouble("timerLvlUp", (entity.getPersistentData().getDouble("timerLvlUp") - 1));
			}
		}
	}
}
