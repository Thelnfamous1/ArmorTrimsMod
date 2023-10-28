package com.marwinekk.armortrims.procedures;

import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import com.marwinekk.armortrims.init.ArmorTrimsModItems;

public class ExProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player) {
			ItemStack _setstack = new ItemStack(ArmorTrimsModItems.TNT_ARROW.get());
			_setstack.setCount(1);
			ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
		}
	}
}
