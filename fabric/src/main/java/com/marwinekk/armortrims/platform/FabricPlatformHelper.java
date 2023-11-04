package com.marwinekk.armortrims.platform;

import com.marwinekk.armortrims.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
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

    }

    @Override
    public void sendMobEffectPacket(MobEffect effect) {

    }
}
