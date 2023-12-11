package com.marwinekk.armortrims.datagen;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.client.ModKeybinds;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, ArmorTrimsMod.MOD_ID,"en_us");
    }

    @Override
    protected void addTranslations() {
        addKey(ModKeybinds.BEACON_SCREEN,"Beacon Gui");
        addKey(ModKeybinds.HELMET_BONUS_ABILITY,"Helmet Trim Ability");
        addKey(ModKeybinds.CHESTPLATE_BONUS_ABILITY,"Chestplate Trim Ability");
        addKey(ModKeybinds.LEGGINGS_BONUS_ABILITY,"Leggings Trim Ability");
        addKey(ModKeybinds.BOOTS_BONUS_ABILITY,"Boots Trim Ability");

        addKey(ModKeybinds.SET_BONUS_ABILITY,"Combat Ability");

        add("trinkets.slot.armor_trims.diamond_trim_bonus","Diamond Trim Bonus");


    }

    void addKey(KeyMapping mapping,String translation) {
        add(mapping.getName(),translation);
    }
}
