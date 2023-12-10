package com.marwinekk.armortrims.datagen;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ArmorTrimsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.DIAMOND_ARROW_IMMUNE)
                .addTag(BlockTags.WITHER_IMMUNE)
                .addTag(BlockTags.DRAGON_IMMUNE);
    }
}
