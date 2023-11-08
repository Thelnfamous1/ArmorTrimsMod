package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import com.marwinekk.armortrims.client.renderer.TNTArrowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModKeybinds.allKeybinds().forEach(KeyBindingHelper::registerKeyBinding);
        EntityRendererRegistry.register(ArmorTrimsModEntities.TNT_ARROW,TNTArrowRenderer::new);
        ClientTickEvents.START_CLIENT_TICK.register(ArmorTrimsModClient::clientTick);

    }
}
