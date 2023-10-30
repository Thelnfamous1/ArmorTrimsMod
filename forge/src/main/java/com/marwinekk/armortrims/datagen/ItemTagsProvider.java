package com.marwinekk.armortrims.datagen;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {
    public ItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, ArmorTrimsMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        List<Item> food = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.getFoodProperties() != null)  {
                food.add(item);
            }
        }
        tag(ModTags.QUARTZ_SMELTABLE).addTag(Tags.Items.ORES).add(food.toArray(new Item[0]));
    }
}
