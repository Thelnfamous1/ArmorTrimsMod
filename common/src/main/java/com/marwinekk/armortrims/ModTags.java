package com.marwinekk.armortrims;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> QUARTZ_SMELTABLE = TagKey.create(Registries.ITEM,new ResourceLocation(ArmorTrimsMod.MOD_ID,"quartz_smeltable"));


}
