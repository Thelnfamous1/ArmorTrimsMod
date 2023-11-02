package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;

public class Client {

    public static void setup() {
        MinecraftForge.EVENT_BUS.addListener(Client::clientTick);
        MinecraftForge.EVENT_BUS.addListener(Client::login);
    }

    public static void keybinds(RegisterKeyMappingsEvent event) {
        event.register(ModKeybinds.SET_BONUS_ABILITY);
        event.register(ModKeybinds.HELMET_BONUS_ABILITY);
        event.register(ModKeybinds.CHESTPLATE_BONUS_ABILITY);
        event.register(ModKeybinds.LEGGINGS_BONUS_ABILITY);
        event.register(ModKeybinds.BOOTS_BONUS_ABILITY);
        event.register(ModKeybinds.BEACON_SCREEN);
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ArmorTrimsModClient.clientTick();
        }
    }

    public static void login(ClientPlayerNetworkEvent.LoggingIn event) {
        ArmorTrimsMod.playerLogin(event.getPlayer());
    }
}
