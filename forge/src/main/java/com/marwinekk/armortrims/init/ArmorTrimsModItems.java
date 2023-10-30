
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import com.marwinekk.armortrims.ArmorTrimsModForge;
import com.marwinekk.armortrims.item.TNTArrowItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArmorTrimsModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorTrimsModForge.MOD_ID);
	public static final RegistryObject<Item> TNT_ARROW = REGISTRY.register("tnt_arrow", () -> new TNTArrowItem());
}
