package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.world.deferredevent.DeferredEventTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EmeraldTrimAbilities {
    private static final int EMERALD_ACTIVE_TICKS = 20 * 15;
    private static final int EMERALD_COOLDOWN_TICKS = 20 * 60 * 2;
    public static final ArmorTrimAbility EMERALD_TRIM_ABILITY = new ArmorTrimAbility(
            ArmorTrimAbilities.NULL,
            ArmorTrimAbilities.NULL,
            EmeraldTrimAbilities::activateCombatAbility,
            EmeraldTrimAbilities::onRemove,
            EMERALD_ACTIVE_TICKS,
            EMERALD_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(EmeraldTrimAbilities::onCombatAbilityActivated)
            .setOnCombatAbilityActive(EmeraldTrimAbilities::onCombatAbilityActive);

    private static void onCombatAbilityActive(ServerPlayer player) {
        ArmorTrimAbilities.tickParticles(player, ParticleTypes.EGG_CRACK);
    }

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.EVOKER_CAST_SPELL);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        return ArmorTrimAbilities.messagePlayer(player, Component.translatable("Totem Save Activated"));
    }

    private static void onRemove(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        MobEffect old = playerDuck.beaconEffect();
        player.removeEffect(old);
    }

    public static void updateBeaconEffect(ServerPlayer player, MobEffect mobEffect, int amplifier) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        if (canUseBeaconEffect(playerDuck)) {
            player.addEffect(new MobEffectInstance(mobEffect, -1, amplifier, true, false));
            MobEffect old = playerDuck.beaconEffect();
            player.removeEffect(old);
            playerDuck.setBeaconEffect(mobEffect);
        } else {
            player.sendSystemMessage(Component.literal("Cannot apply beacon effects without emerald trim"));
        }
    }

    public static boolean canUseBeaconEffect(PlayerDuck playerDuck) {
        return playerDuck.hasSetBonus(Items.EMERALD);
    }

    public static void onDeathProtectionCheck(LivingEntity living) {
        if (living instanceof ServerPlayer player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            EquipmentSlot usedSlot = null;
            boolean gaveTotem = false;
            for (EquipmentSlot slot : ArmorTrimsMod.slots) {
                Item trim = slot == null ? playerDuck.regularSetBonus() : ArmorTrimsMod.getTrimItem(player.level(), player.getItemBySlot(slot));
                int timer = playerDuck.abilityTimer(slot);
                if (trim == Items.EMERALD && timer > 0) {
                    if (!living.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                        ((Player) living).drop(living.getItemInHand(InteractionHand.OFF_HAND),true);
                    }
                    living.setItemSlot(EquipmentSlot.OFFHAND,Items.TOTEM_OF_UNDYING.getDefaultInstance());
                    usedSlot = slot;
                    gaveTotem = true;
                    break;
                }
            }
            if(gaveTotem){
                playerDuck.setAbilityCooldown(usedSlot, 20 * 60 * 10);
                playerDuck.setAbilityTimer(usedSlot, 0);
                ArmorTrimsMod.removeDeferredEvent(player.serverLevel(), de -> de.type() == DeferredEventTypes.SET_ABILITY_COOLDOWN);
            }
        }
    }
}
