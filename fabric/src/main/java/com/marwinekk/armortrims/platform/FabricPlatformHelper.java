package com.marwinekk.armortrims.platform;

import com.marwinekk.armortrims.network.PacketHandler;
import com.marwinekk.armortrims.platform.services.IPlatformHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendAbilityPacket(EquipmentSlot slot) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(slot != null ? slot.ordinal() : -1);
        ClientPlayNetworking.send(PacketHandler.keybind,buf);
    }

    @Override
    public void sendMobEffectPacket(MobEffect effect, int amplifier) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(MobEffect.getId(effect));
        buf.writeInt(amplifier);
        ClientPlayNetworking.send(PacketHandler.mob_effect,buf);
    }
}
