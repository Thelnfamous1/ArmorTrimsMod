package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class TimerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getDouble("timerForTimer") == 0) {
			entity.getPersistentData().putDouble("timerForTimer", 20);
		} else {
			entity.getPersistentData().putDouble("timerForTimer", (entity.getPersistentData().getDouble("timerForTimer") - 1));
		}
		if (entity.getPersistentData().getDouble("timerForTimer") == 0) {
			if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).iron
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).lapis
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).gold
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).diamond
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).amethyst
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).netherite
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).redstone
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).copper
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).quartz
					|| (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).emerald) {
				if ((entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).timer > 0) {
					{
						double _setval = (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).timer - 1;
						entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
							capability.timer = _setval;
							capability.syncPlayerVariables(entity);
						});
					}
				}
			}
		}
	}
}
