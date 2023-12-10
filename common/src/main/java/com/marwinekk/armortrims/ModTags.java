package com.marwinekk.armortrims;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Item> QUARTZ_SMELTABLE = TagKey.create(Registries.ITEM,new ResourceLocation(ArmorTrimsMod.MOD_ID,"quartz_smeltable"));

    public static final TagKey<Block> DIAMOND_ARROW_IMMUNE = TagKey.create(Registries.BLOCK,new ResourceLocation(ArmorTrimsMod.MOD_ID,"diamond_arrow_immune"));

}
