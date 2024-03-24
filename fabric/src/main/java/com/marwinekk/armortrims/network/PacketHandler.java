package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.util.EmeraldTrimAbilities;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;

public class PacketHandler {

    public static final ResourceLocation keybind = new ResourceLocation(ArmorTrimsMod.MOD_ID,"keybind");
    public static final ResourceLocation mob_effect = new ResourceLocation(ArmorTrimsMod.MOD_ID,"mob_effect");

    public static final ResourceLocation C2S_DO_DOUBLEJUMP = new ResourceLocation(ArmorTrimsMod.MOD_ID, "request_double_jump");
    public static final ResourceLocation S2C_PLAY_EFFECTS_PACKET_ID = new ResourceLocation(ArmorTrimsMod.MOD_ID, "play_double_jump");

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(mob_effect, PacketHandler::receiveMobEffect);
        ServerPlayNetworking.registerGlobalReceiver(keybind, PacketHandler::receiveKeybind);
        ServerPlayNetworking.registerGlobalReceiver(C2S_DO_DOUBLEJUMP,
                (server, player, handler, buf, responseSender) -> {
                    FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
                    passedData.writeUUID(buf.readUUID());

                    server.execute(() -> {
                        PlayerLookup.tracking(player).forEach(p -> {
                            if (p != player) {
                                ServerPlayNetworking.send(p, S2C_PLAY_EFFECTS_PACKET_ID, passedData);
                            }
                        });
                    });
                });

    }

    public static void receiveKeybind(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        EquipmentSlot slot = id > -1 ? EquipmentSlot.values()[id] : null;
        server.execute(() -> ArmorTrimsMod.activateCombatAbility(player,slot));
    }

    public static void receiveMobEffect(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        MobEffect mobEffect = MobEffect.byId(id);
        int amplifier = buf.readInt();
        server.execute(() -> EmeraldTrimAbilities.updateBeaconEffect(player,mobEffect, amplifier));
    }
}
