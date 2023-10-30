package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraft.world.entity.Entity;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class FfProcedure {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        execute(event.getEntity());
    }

    private static void execute(Entity entity) {
        boolean _setval = true;
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseIron = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseLapis = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseGold = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseDiamond = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseAmethyst = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseNetherite = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseRedstone = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseCopper = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseQuartz = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.canUseEmerald = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.cooldownIron = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.died = _setval;
            capability.syncPlayerVariables(entity);
        });
        entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
            capability.didntDie = _setval;
            capability.syncPlayerVariables(entity);
        });
    }
}
