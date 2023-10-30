package com.marwinekk.armortrims.procedures;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import io.netty.buffer.Unpooled;

import com.marwinekk.armortrims.world.inventory.MenuBeaconMenu;

public class BeaconGUIOnKeyPressedProcedure {
	public static void execute(Player player) {
		if (player instanceof ServerPlayer) {
			PlayerDuck playerDuck = (PlayerDuck) player;
			if (playerDuck.hasSetBonus(Items.EMERALD)) {
				player.openMenu(new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("MenuBeacon");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new MenuBeaconMenu(id, inventory);
					}
				});
			}
		}
	}
}
