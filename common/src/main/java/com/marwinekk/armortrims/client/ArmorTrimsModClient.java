package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.BeaconEffectScreen;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.platform.Services;
import com.marwinekk.armortrims.util.EmeraldTrimAbilities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;

public class ArmorTrimsModClient {

    public static void clientTick(Minecraft client) {
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

        while (ModKeybinds.BEACON_SCREEN.consumeClick()) {
            if (EmeraldTrimAbilities.canUseBeaconEffect(((PlayerDuck) client.player))) {
                client.setScreen(new BeaconEffectScreen(Component.empty()));
            } else {
                client.player.sendSystemMessage(Component.translatable("Cannot use beacon effects without emerald trim",true));
            }
        }



    }

    public static Level getClientWorld() {
        return Minecraft.getInstance().level;
    }

}
