package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    //z is for non dragonegg combat activation (need full set)
    //x, c, v, b is for dragon egg combat activation (each one activates the combat ability of the Armor piece

    public static final KeyMapping SET_BONUS_ABILITY = new KeyMapping("key.set_bonus_ability",GLFW.GLFW_KEY_Z,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping HELMET_BONUS_ABILITY = new KeyMapping("key.helmet_bonus_ability",GLFW.GLFW_KEY_X,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping CHESTPLATE_BONUS_ABILITY = new KeyMapping("key.chestplate_bonus_ability",GLFW.GLFW_KEY_C,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping LEGGINGS_BONUS_ABILITY = new KeyMapping("key.leggings_bonus_ability",GLFW.GLFW_KEY_V,"key.categories."+ ArmorTrimsMod.MOD_ID);
    public static final KeyMapping BOOTS_BONUS_ABILITY = new KeyMapping("key.boots_bonus_ability",GLFW.GLFW_KEY_B,"key.categories."+ ArmorTrimsMod.MOD_ID);


}
