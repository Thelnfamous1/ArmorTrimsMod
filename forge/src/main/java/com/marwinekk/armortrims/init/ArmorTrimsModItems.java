
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import com.marwinekk.armortrims.item.TNTArrowItem;
import com.marwinekk.armortrims.item.PotionThrowItem;
import com.marwinekk.armortrims.item.PiercingArrowItem;
import com.marwinekk.armortrims.item.Pa3Item;
import com.marwinekk.armortrims.item.Pa2Item;
import com.marwinekk.armortrims.ArmorTrimsModForge;

public class ArmorTrimsModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ArmorTrimsModForge.MOD_ID);
	public static final RegistryObject<Item> PIERCING_ARROW = REGISTRY.register("piercing_arrow", () -> new PiercingArrowItem());
	public static final RegistryObject<Item> PA_2 = REGISTRY.register("pa_2", () -> new Pa2Item());
	public static final RegistryObject<Item> PA_3 = REGISTRY.register("pa_3", () -> new Pa3Item());
	public static final RegistryObject<Item> TNT_ARROW = REGISTRY.register("tnt_arrow", () -> new TNTArrowItem());
	public static final RegistryObject<Item> POTION_THROW = REGISTRY.register("potion_throw", () -> new PotionThrowItem());
}
