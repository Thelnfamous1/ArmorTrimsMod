package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.world.deferredevent.DespawnLater;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class GoldTrimAbilities {
    private static final int GOLD_ACTIVE_TICKS = 0;
    private static final int GOLD_COOLDOWN_TICKS = 20 * 50;
    public static final ArmorTrimAbility GOLD_TRIM_ABILITY = new ArmorTrimAbility(
            GoldTrimAbilities::onEquip,
            ArmorTrimAbilities.NULL,
            GoldTrimAbilities::activateCombatAbility,
            GoldTrimAbilities::onRemove,
            GOLD_ACTIVE_TICKS,
            GOLD_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(GoldTrimAbilities::onCombatAbilityActivated);

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    private static void onEquip(ServerPlayer player) {
        ArmorTrimAbilities.addAttributeModifier(
                player, Attributes.MAX_HEALTH, 4, AttributeModifier.Operation.ADDITION);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        Level level = player.level();
        for (int i = 0; i < 5; i++) {

            PiglinBrute piglinBrute = EntityType.PIGLIN_BRUTE.spawn(
                    (ServerLevel) level, Items.PIGLIN_SPAWN_EGG.getDefaultInstance(),
                    player, player.blockPosition(), MobSpawnType.SPAWN_EGG, false, false);

            piglinBrute.setImmuneToZombification(true);
            PiglinBruteDuck piglinBruteDuck = (PiglinBruteDuck) piglinBrute;
            piglinBruteDuck.setOwnerUUID(player.getUUID());
            DespawnLater despawnLater = new DespawnLater(piglinBrute);
            despawnLater.setTimer(2 * 60 * 20);
            ArmorTrimsMod.addDeferredEvent(player.serverLevel(),despawnLater);
        }
        return true;
    }

    private static void onRemove(ServerPlayer player) {
        ArmorTrimAbilities.removeAttributeModifier(player, Attributes.MAX_HEALTH);
    }

    public static boolean victimsDropGold(PlayerDuck playerDuck) {
        return playerDuck.hasSetBonus(Items.GOLD_INGOT);
    }
}
