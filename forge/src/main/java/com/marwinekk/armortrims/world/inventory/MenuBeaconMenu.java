
package com.marwinekk.armortrims.world.inventory;

import com.marwinekk.armortrims.init.ArmorTrimsModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class MenuBeaconMenu extends AbstractContainerMenu {

	public MenuBeaconMenu(int id, Inventory inv) {
		super(ArmorTrimsModMenus.MENU_BEACON.get(), id);

	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		return ItemStack.EMPTY;
	}
}
