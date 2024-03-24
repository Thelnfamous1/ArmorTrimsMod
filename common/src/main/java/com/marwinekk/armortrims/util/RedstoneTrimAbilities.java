package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.entity.TNTArrowEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class RedstoneTrimAbilities {
    public static final int REDSTONE_ABILITY_USES = 3;
    private static final int REDSTONE_ACTIVE_TICKS = 0;
    public static final ArmorTrimAbility REDSTONE_TRIM_ABILITY = new ArmorTrimAbility(
            RedstoneTrimAbilities::onEquip,
            ArmorTrimAbilities.NULL,
            RedstoneTrimAbilities::activateCombatAbility,
            RedstoneTrimAbilities::onRemove,
            REDSTONE_ACTIVE_TICKS,
            0)
            .runEveryEquip();
    public static final int REDSTONE_COOLDOWN_TICKS = 45 * 20;

    private static void onEquip(ServerPlayer player) {
        ArmorTrimAbilities.applyBonusEnchantToArmor(player, Enchantments.BLAST_PROTECTION, 4, Items.REDSTONE);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        TNTArrowEntity tntArrowEntity = new TNTArrowEntity(player.level(),player);
        tntArrowEntity.setExplosionRadius(1.3F);
        tntArrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0);
        lockOn(player, tntArrowEntity);
        player.level().addFreshEntity(tntArrowEntity);
        PlayerDuck playerDuck = (PlayerDuck) player;
        int arrows = playerDuck.redstoneArrowsLeft();
        if (arrows <=0) {
            playerDuck.setRedstoneArrowsLeft(REDSTONE_ABILITY_USES - 1); // 3 arrows total, we just fired 1
        } else {
            arrows--;
            if (arrows <= 0) {
                playerDuck.setAbilityCooldown(slot, RedstoneTrimAbilities.REDSTONE_COOLDOWN_TICKS);
            }
            playerDuck.setRedstoneArrowsLeft(arrows);
        }
        return false;
    }

    private static void onRemove(ServerPlayer player) {
        ArmorTrimAbilities.removeBonusEnchantFromArmor(player, Enchantments.BLAST_PROTECTION, Items.REDSTONE);
    }

    public static void lockOn(LivingEntity shooter, TNTArrowEntity homingArrow) {
        ArmorTrimAbilities.getHitResult(shooter).ifRight(ehr -> homingArrow.setHomingTarget(ehr.getEntity()));
    }
}
