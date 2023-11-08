package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
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

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(mob_effect, PacketHandler::receiveMobEffect);
        ServerPlayNetworking.registerGlobalReceiver(keybind, PacketHandler::receiveKeybind);

    }

    public static void receiveKeybind(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        MobEffect mobEffect = MobEffect.byId(id);
        server.execute(() -> ArmorTrimsMod.addOneEffectRemoveOther(player,mobEffect));
    }

    public static void receiveMobEffect(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        EquipmentSlot slot = id > -1 ? EquipmentSlot.values()[id] : null;
        server.execute(() -> ArmorTrimsMod.activateCombatAbility(player,slot));
    }
}
