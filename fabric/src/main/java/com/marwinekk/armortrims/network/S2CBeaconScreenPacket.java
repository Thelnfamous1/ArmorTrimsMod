package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.BeaconEffectScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CBeaconScreenPacket {

    public S2CBeaconScreenPacket(FriendlyByteBuf buffer) {
    }



    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        Misdirection.method();
        context.setPacketHandled(true);
    }

    static class Misdirection {
        static void method() {
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new BeaconEffectScreen(Component.empty())));
        }
    }
}
