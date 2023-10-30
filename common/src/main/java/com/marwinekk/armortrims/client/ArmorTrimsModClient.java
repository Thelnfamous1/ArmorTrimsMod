package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;

public class ArmorTrimsModClient {

    public static void clientTick() {
        while (ModKeybinds.SET_BONUS_ABILITY.consumeClick()) {
            Services.PLATFORM.sendAbilityPacket(null);
        }
        while (ModKeybinds.HELMET_BONUS_ABILITY.consumeClick()) {
            Services.PLATFORM.sendAbilityPacket(EquipmentSlot.HEAD);
        }
        while (ModKeybinds.CHESTPLATE_BONUS_ABILITY.consumeClick()) {
            Services.PLATFORM.sendAbilityPacket(EquipmentSlot.CHEST);
        }
        while (ModKeybinds.LEGGINGS_BONUS_ABILITY.consumeClick()) {
            Services.PLATFORM.sendAbilityPacket(EquipmentSlot.LEGS);
        }
        while (ModKeybinds.BOOTS_BONUS_ABILITY.consumeClick()) {
            Services.PLATFORM.sendAbilityPacket(EquipmentSlot.FEET);
        }
    }

    public static Level getClientWorld() {
        return Minecraft.getInstance().level;
    }

}
