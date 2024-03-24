package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;

public class LapisLazuliTrimAbilities {
    private static final int LAPIS_ACTIVE_TICKS = 20 * 20;
    private static final int LAPIS_COOLDOWN_TICKS = 20 * 50;

    //give 1 xp level every 10 minutes
    public static final ArmorTrimAbility LAPIS_LAZULI_TRIM_ABILITY = new ArmorTrimAbility(
            ArmorTrimAbilities.NULL,
            LapisLazuliTrimAbilities::onTick,
            LapisLazuliTrimAbilities::activateCombatAbility,
            ArmorTrimAbilities.NULL,
            LAPIS_ACTIVE_TICKS,
            LAPIS_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(LapisLazuliTrimAbilities::onCombatAbilityActivated)
            .setOnCombatAbilityActive(LapisLazuliTrimAbilities::onCombatAbilityActive)
            .setOnCombatAbilityInactive(LapisLazuliTrimAbilities::onCombatAbilityInactive);

    private static void onCombatAbilityActive(ServerPlayer player) {
        ArmorTrimAbilities.tickParticles(player, ParticleTypes.REVERSE_PORTAL);
    }

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
    }

    private static void onTick(ServerPlayer player) {
        if (player.level().getGameTime() % (20 * 20 * 10) == 0) {
            player.giveExperienceLevels(1);
            player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1, 1);
            player.displayClientMessage(Component.literal("[§c!§f] Level §agained§f!"), true);
        }
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot1) {
        ArmorTrimAbilities.toggleEnchantBoosts(player, true);
        ArmorTrimsMod.lockAllSlots(player);
        return true;
    }

    private static void onCombatAbilityInactive(ServerPlayer player) {
        ArmorTrimAbilities.toggleEnchantBoosts(player, false);
        ArmorTrimsMod.unlockAllSlots(player);
    }
}
