
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import com.marwinekk.armortrims.client.gui.MenuBeaconScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ArmorTrimsModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(ArmorTrimsModMenus.MENU_BEACON.get(), MenuBeaconScreen::new);
		});
	}
}
