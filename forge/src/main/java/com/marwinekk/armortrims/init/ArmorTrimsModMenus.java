
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import com.marwinekk.armortrims.world.inventory.MenuBeaconMenu;
import com.marwinekk.armortrims.ArmorTrimsModForge;

public class ArmorTrimsModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArmorTrimsModForge.MOD_ID);
	public static final RegistryObject<MenuType<MenuBeaconMenu>> MENU_BEACON = REGISTRY.register("menu_beacon", () -> new MenuType<>(MenuBeaconMenu::new, FeatureFlags.VANILLA_SET));
}
