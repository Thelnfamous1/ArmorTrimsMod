package com.marwinekk.armortrims.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ArmorTrimsModClient {

    public static void clientTick() {
        while (ModKeybinds.SET_BONUS_ABILITY.consumeClick()) {

        }
    }

    public static Level getClientWorld() {
        return Minecraft.getInstance().level;
    }

}
