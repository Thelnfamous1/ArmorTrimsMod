package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.ducks.WitchDuck;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AmethystTrimAbilities {
    private static final int AMETHYST_ACTIVE_TICKS = 0;
    private static final int AMETHYST_COOLDOWN_TICKS = 20 * 60;
    public static final ArmorTrimAbility AMETHYST_TRIM_ABILITY = new ArmorTrimAbility(
            ArmorTrimAbilities.NULL,
            ArmorTrimAbilities.NULL,
            AmethystTrimAbilities::activateCombatAbility,
            ArmorTrimAbilities.NULL,
            AMETHYST_ACTIVE_TICKS,
            AMETHYST_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(AmethystTrimAbilities::onCombatAbilityActivated);

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.ILLUSIONER_PREPARE_BLINDNESS);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        Level level = player.level();
        for (int i = 0; i < 2; i++) {
            Witch witch = EntityType.WITCH.create(level);
            witch.moveTo(player.getPosition(0));
            WitchDuck witchDuck = (WitchDuck) witch;
            witchDuck.setOwnerUUID(player.getUUID());
            // survives 5 critical hits from sharp V diamond sword
            witch.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0);
            witch.setHealth(100.0F);
            level.addFreshEntity(witch);
        }
        return true;
    }

    public static boolean cannotBeTargetedByAI(PlayerDuck playerDuck) {
        return playerDuck.hasSetBonus(Items.AMETHYST_SHARD);
    }

    public static boolean cannotReceiveNegativeMobEffects(PlayerDuck playerDuck) {
        return playerDuck.hasSetBonus(Items.AMETHYST_SHARD);
    }
}
