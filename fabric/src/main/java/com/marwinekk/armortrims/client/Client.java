package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsModEntities;
import com.marwinekk.armortrims.client.renderer.TNTArrowRenderer;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;

public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModKeybinds.allKeybinds().forEach(KeyBindingHelper::registerKeyBinding);
        EntityRendererRegistry.register(ArmorTrimsModEntities.TNT_ARROW,TNTArrowRenderer::new);
        ClientTickEvents.START_CLIENT_TICK.register(ArmorTrimsModClient::clientTick);
        HudRenderCallback.EVENT.register(this::renderHUDElement);
    }

    private void renderHUDElement(GuiGraphics guiGraphics, float v) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {

            PlayerDuck playerDuck = (PlayerDuck)player;

            CompoundTag armorTrimData = playerDuck.getArmorTrimsData();

            int cooldown = playerDuck.abilityCooldown(null);

            if (cooldown > 0) {


                int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                int posX = w / 2;
                int posY = h / 2;

                guiGraphics.drawString(Minecraft.getInstance().font, "Ability Unavailable Cooldown: "
                        + playerDuck.abilityCooldown(null),  posX + 10, h - 50, 0xffffff);
            }
        }
    }
}
