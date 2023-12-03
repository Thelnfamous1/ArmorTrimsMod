package com.marwinekk.armortrims.datagen;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {
    public ItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, ArmorTrimsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.QUARTZ_SMELTABLE)
                .add(Items.RAW_COPPER)
                .add(Items.RAW_IRON)
                .add(Items.RAW_GOLD)
                .add(Items.ANCIENT_DEBRIS)
                .add(Items.BEEF)
                .add(Items.CHICKEN)
                .add(Items.COD)
                .add(Items.KELP)
                .add(Items.SALMON)
                .add(Items.MUTTON)
                .add(Items.PORKCHOP)
                .add(Items.POTATO)
                .add(Items.RABBIT);
    }
}
