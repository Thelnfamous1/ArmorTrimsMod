package com.marwinekk.armortrims.platform;

import com.marwinekk.armortrims.network.C2SKeybindPacket;
import com.marwinekk.armortrims.network.C2SMobEffectPacket;
import com.marwinekk.armortrims.network.PacketHandler;
import com.marwinekk.armortrims.platform.services.IPlatformHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.Nullable;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void sendAbilityPacket(@Nullable EquipmentSlot slot) {
        PacketHandler.CHANNEL.sendToServer(new C2SKeybindPacket(slot));
    }

    @Override
    public void sendMobEffectPacket(MobEffect effect, int amplifier) {
        PacketHandler.CHANNEL.sendToServer(new C2SMobEffectPacket(effect, amplifier));
    }
}