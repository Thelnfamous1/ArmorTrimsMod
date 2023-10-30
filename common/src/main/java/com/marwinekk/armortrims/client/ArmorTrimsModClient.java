package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.BeaconEffectScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ArmorTrimsModClient {

    public static void clientTick() {
        while (ModKeybinds.BEACON_GUI.consumeClick()) {
            Minecraft.getInstance().setScreen(new BeaconEffectScreen(Component.translatable("no")));
        }
    }
}
