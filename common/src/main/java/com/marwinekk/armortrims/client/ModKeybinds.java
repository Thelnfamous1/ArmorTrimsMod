package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static final KeyMapping SET_BONUS_ABILITY = new KeyMapping("key.set_bonus_ability",GLFW.GLFW_KEY_1,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping HELMET_BONUS_ABILITY = new KeyMapping("key.helmet_bonus_ability",GLFW.GLFW_KEY_1,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping CHESTPLATE_BONUS_ABILITY = new KeyMapping("key.chestplate_bonus_ability",GLFW.GLFW_KEY_1,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping LEGGINGS_BONUS_ABILITY = new KeyMapping("key.leggings_bonus_ability",GLFW.GLFW_KEY_1,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping BOOTS_BONUS_ABILITY = new KeyMapping("key.boots_bonus_ability",GLFW.GLFW_KEY_1,"key.categories."+ ArmorTrimsMod.MOD_ID);


}
