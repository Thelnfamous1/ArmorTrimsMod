package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.world.deferredevent.IronFist;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class IronTrimAbilities {
    private static final int IRON_ACTIVE_TICKS = 15 * 20;
    private static final int IRON_COOLDOWN_TICKS = 30 * 20;
    public static final ArmorTrimAbility IRON_TRIM_ABILITY = new ArmorTrimAbility(
            ArmorTrimAbilities.NULL,
            IronTrimAbilities::onTick,
            IronTrimAbilities::activateCombatAbility,
            ArmorTrimAbilities.NULL,
            IRON_ACTIVE_TICKS,
            IRON_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(IronTrimAbilities::onCombatAbilityActivated)
            .setOnCombatAbilityActive(IronTrimAbilities::onCombatAbilityActive);

    private static void onCombatAbilityActive(ServerPlayer player) {
        ArmorTrimAbilities.tickParticles(player, ParticleTypes.WAX_OFF);
    }

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.IRON_GOLEM_REPAIR);
    }

    private static boolean onTick(ServerPlayer player) {
        return player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 9, false, false));
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        ArmorTrimAbilities.messagePlayer(player, Component.translatable("Iron Fists Active"));
        return true;
    }

    public static boolean onFallDamage(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            return playerDuck.hasSetBonus(Items.IRON_INGOT);
        }
        return false;
    }

    public static void applyIronFist(LivingEntity victim) {
        IronFist ironFist = new IronFist(victim);
        ironFist.setTimer(2);
        ArmorTrimsMod.addDeferredEvent((ServerLevel) victim.level(),ironFist);
    }
}
