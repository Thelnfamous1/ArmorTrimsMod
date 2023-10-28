package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class DragonEggOnPlayerTickProcedure {
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
		if (entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(Blocks.DRAGON_EGG)) : false) {
			{
				boolean _setval = true;
				entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.dragonegg = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else {
			{
				boolean _setval = false;
				entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.dragonegg = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
