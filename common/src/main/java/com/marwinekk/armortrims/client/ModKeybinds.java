package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {

    //z is for non dragonegg combat activation (need full set)
    //x, c, v, b is for dragon egg combat activation (each one activates the combat ability of the Armor piece

    static final String CAT = "key.categories."+ ArmorTrimsMod.MOD_ID;

    public static final KeyMapping SET_BONUS_ABILITY = new KeyMapping("key.set_bonus_ability",GLFW.GLFW_KEY_Z,CAT);
    public static final KeyMapping HELMET_BONUS_ABILITY = new KeyMapping("key.helmet_bonus_ability",GLFW.GLFW_KEY_X,CAT);
    public static final KeyMapping CHESTPLATE_BONUS_ABILITY = new KeyMapping("key.chestplate_bonus_ability",GLFW.GLFW_KEY_C,CAT);
    public static final KeyMapping LEGGINGS_BONUS_ABILITY = new KeyMapping("key.leggings_bonus_ability",GLFW.GLFW_KEY_V,CAT);
    public static final KeyMapping BOOTS_BONUS_ABILITY = new KeyMapping("key.boots_bonus_ability",GLFW.GLFW_KEY_B,CAT);
    public static final KeyMapping BEACON_SCREEN = new KeyMapping("key.beacon_screen",GLFW.GLFW_KEY_N,CAT);
    static List<KeyMapping> ALL = new ArrayList<>();

    public static List<KeyMapping> allKeybinds() {
        if (ALL.isEmpty()) {
            Field[] fields = ModKeybinds.class.getFields();

            for (Field field : fields) {
                try {
                    if (field.get(null) instanceof KeyMapping keyMapping) {
                        ALL.add(keyMapping);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ALL;
    }
}
