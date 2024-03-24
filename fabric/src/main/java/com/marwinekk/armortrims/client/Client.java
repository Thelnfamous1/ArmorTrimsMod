package com.marwinekk.armortrims.client;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ArmorTrimsModEntities;
import com.marwinekk.armortrims.client.renderer.BasicArrowRenderer;
import com.marwinekk.armortrims.commands.ATCommands;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.network.PacketHandler;
import com.marwinekk.armortrims.util.CopperTrimAbilities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
        ClientPlayNetworking.registerGlobalReceiver(PacketHandler.S2C_PLAY_EFFECTS_PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    UUID effectPlayerUuid = buf.readUUID();
                    client.execute(() -> {
                        Player effectPlayer = client.player.getCommandSenderWorld().getPlayerByUUID(effectPlayerUuid);
                        if (effectPlayer != null) {
                            CopperTrimAbilities.createDoubleJumpEffect(client.player, effectPlayer);
                        }
                    });
                });
    }

    private void renderHUDElement(GuiGraphics guiGraphics, float v) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {

            PlayerDuck playerDuck = (PlayerDuck)player;

            int drawn = 0;
            for(int idx = ArmorTrimsMod.slots.length - 1; idx >= 0; idx--){ // null, feet, legs, chest, head
                EquipmentSlot slot = ArmorTrimsMod.slots[idx];
                int timer = playerDuck.abilityTimer(slot);
                int cooldown = playerDuck.abilityCooldown(slot);
                if (cooldown > 0 || timer > 0) {
                    drawn++;
                    boolean available = cooldown <= 0;
                    int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                    int posX = w / 2;

                    int seconds = (available ? timer : cooldown) / 20;
                    drawAvailability(guiGraphics, getAbilityPrefix(slot), available, seconds, posX + 10, h - 50 - getYOffset(drawn));
                }
            }
        }
    }

    private static String getAbilityPrefix(@Nullable EquipmentSlot slot) {
        return slot == null ? "SET " : slot.name() + " ";
    }

    private static int getYOffset(int drawn){
        return Minecraft.getInstance().font.lineHeight * (drawn - 1);
    }

    private static void drawAvailability(GuiGraphics guiGraphics, String slotPrefix, boolean available, int seconds, int x, int y) {
        guiGraphics.drawString(Minecraft.getInstance().font,
                Component.literal(slotPrefix + "Ability " + (available ? "Active" : "Unavailable")).withStyle(available ? ChatFormatting.GREEN : ChatFormatting.RED)
                        .append(Component.literal(":").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal("" + seconds).withStyle(ChatFormatting.DARK_GREEN))
                        .append(Component.literal("s").withStyle(ChatFormatting.WHITE)), x, y, 0xffffff);
    }
}
