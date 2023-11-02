package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ArmorTrimsMod.MOD_ID, ArmorTrimsMod.MOD_ID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static void registerPackets() {
        addNetworkMessage(C2SMobEffectPacket.class, C2SMobEffectPacket::encode, C2SMobEffectPacket::new, C2SMobEffectPacket::handle);
        addNetworkMessage(C2SKeybindPacket.class, C2SKeybindPacket::encode, C2SKeybindPacket::new, C2SKeybindPacket::handle);
        addNetworkMessage(S2CBeaconScreenPacket.class, S2CBeaconScreenPacket::encode, S2CBeaconScreenPacket::new, S2CBeaconScreenPacket::handle);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        CHANNEL.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }
}
