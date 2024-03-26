package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.entity.BlockBreakerArrow;
import com.marwinekk.armortrims.platform.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class DiamondTrimAbilities {
    public static final ArmorTrimAbility DIAMOND_TRIM_ABILITY = new ArmorTrimAbility(
            DiamondTrimAbilities::onEquip,
            ArmorTrimAbilities.NULL,
            DiamondTrimAbilities::activateCombatAbility,
            DiamondTrimAbilities::onRemove)
            .runEveryEquip();
    public static final int DIAMOND_ABILITY_USES = 5;
    private static final int EXTRA_BASE_DAMAGE = 5; // arrow base damage is 2, so total is 7
    private static final int DIAMOND_COOLDOWN_TICKS = 45 * 20;

    private static void onEquip(ServerPlayer player) {
        Services.PLATFORM.addExtraInventorySlots(player);
        ArmorTrimAbilities.applyBonusEnchantToArmor(player, Enchantments.UNBREAKING, 4, Items.DIAMOND);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        BlockBreakerArrow arrow = new BlockBreakerArrow(player.level(),player);
        arrow.setBaseDamage(arrow.getBaseDamage() + EXTRA_BASE_DAMAGE);
        arrow.setPierceLevel((byte) (arrow.getPierceLevel() + 1));
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0);
        player.level().addFreshEntity(arrow);
        PlayerDuck playerDuck = (PlayerDuck) player;
        int arrows = playerDuck.diamondArrowsLeft();
        if (arrows <=0) {
            playerDuck.setDiamondArrowsLeft(DIAMOND_ABILITY_USES - 1); // 5 arrows total, we just fired 1
        } else {
            arrows--;
            if (arrows <= 0) {
                playerDuck.setAbilityCooldown(slot, DIAMOND_COOLDOWN_TICKS);
            }
            playerDuck.setDiamondArrowsLeft(arrows);
        }
        return false;
    }

    private static void onRemove(ServerPlayer player) {
        Services.PLATFORM.removeExtraInventorySlots(player);
        ArmorTrimAbilities.removeBonusEnchantFromArmor(player,Enchantments.UNBREAKING,Items.DIAMOND);
    }

    public static boolean hasBonusSlots(Player player) {
        return ((PlayerDuck) player).hasSetBonus(Items.DIAMOND);
    }
}
