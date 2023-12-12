package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ArmorTrimsModEntities;
import com.marwinekk.armortrims.client.renderer.BasicArrowRenderer;
import com.marwinekk.armortrims.commands.ATCommands;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;

public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModKeybinds.allKeybinds().forEach(KeyBindingHelper::registerKeyBinding);
        EntityRendererRegistry.register(ArmorTrimsModEntities.TNT_ARROW, BasicArrowRenderer::new);
        EntityRendererRegistry.register(ArmorTrimsModEntities.DAMAGELESS_ARROW, BasicArrowRenderer::new);
        EntityRendererRegistry.register(ArmorTrimsModEntities.BLOCK_BREAKER_ARROW, BasicArrowRenderer::new);
        ClientTickEvents.START_CLIENT_TICK.register(ArmorTrimsModClient::clientTick);
        HudRenderCallback.EVENT.register(this::renderHUDElement);
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if(ATCommands.isTNTArrow(stack)){
                lines.add(Component.literal("TNT").withStyle(ChatFormatting.DARK_GRAY));
            }
        });
    }

    private void renderHUDElement(GuiGraphics guiGraphics, float v) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {

            PlayerDuck playerDuck = (PlayerDuck)player;

            for(EquipmentSlot slot : ArmorTrimsMod.slots){
                int cooldown = playerDuck.abilityCooldown(slot);
                if (cooldown > 0) {
                    int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    int posX = w / 2;
                    int posY = h / 2;

                    int seconds = cooldown / 20;
                    guiGraphics.drawString(Minecraft.getInstance().font,
                            Component.literal("Ability Unavailable").withStyle(ChatFormatting.RED)
                                    .append(Component.literal(":").withStyle(ChatFormatting.WHITE))
                                    .append(Component.literal("" + seconds).withStyle(ChatFormatting.DARK_GREEN))
                                    .append(Component.literal("s").withStyle(ChatFormatting.WHITE)),  posX + 10, h - 50, 0xffffff);
                    break;
                }
            }
            for(EquipmentSlot slot : ArmorTrimsMod.slots){
                int timer = playerDuck.abilityTimer(slot);
                if (timer > 0) {
                    int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    int posX = w / 2;
                    int posY = h / 2;

                    int seconds = timer / 20;
                    guiGraphics.drawString(Minecraft.getInstance().font,
                            Component.literal("Ability Active").withStyle(ChatFormatting.GREEN)
                                    .append(Component.literal(":").withStyle(ChatFormatting.WHITE))
                                    .append(Component.literal("" + seconds).withStyle(ChatFormatting.DARK_GREEN))
                                    .append(Component.literal("s").withStyle(ChatFormatting.WHITE)),  posX + 10, h - 50, 0xffffff);
                    break;
                }
            }
        }
    }
}
