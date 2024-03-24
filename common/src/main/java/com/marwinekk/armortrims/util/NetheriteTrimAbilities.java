package com.marwinekk.armortrims.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class NetheriteTrimAbilities {
    private static final int NETHERITE_ACTIVE_TICKS = 20 * 10;
    private static final int NETHERITE_COOLDOWN_TICKS = 20 * 45;
    public static final ArmorTrimAbility NETHERITE_TRIM_ABILITY = new ArmorTrimAbility(
            ArmorTrimAbilities.NULL,
            NetheriteTrimAbilities::onTick,
            NetheriteTrimAbilities::activateCombatAbility,
            ArmorTrimAbilities.NULL,
            NETHERITE_ACTIVE_TICKS,
            NETHERITE_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(NetheriteTrimAbilities::onCombatAbilityActivated)
            .setOnCombatAbilityActive(NetheriteTrimAbilities::onCombatAbilityActive);

    private static void onCombatAbilityActive(ServerPlayer player) {
        ArmorTrimAbilities.tickParticles(player, ParticleTypes.SCULK_SOUL);
    }

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.WITHER_HURT);
    }

    private static void onTick(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 9, true, false));
        if (player.isInLava()) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, true, false));
        }
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot equipmentSlot) {
        return ArmorTrimAbilities.messagePlayer(player, Component.translatable("Wither Touch Active"));
    }

    public static void applyWitherPunch(LivingEntity victim) {
        victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 8 * 20, 0));
        victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8 * 20, 0));
        victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 8 * 20, 0));
    }
}
