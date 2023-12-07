package com.marwinekk.armortrims.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class ArmorTrimAbility {

    public static final Consumer<ServerPlayer> NULL_ACTIVE = player -> {
    };
    public final Consumer<ServerPlayer> onEquip;
    public final Consumer<ServerPlayer> onServerPlayerTick;
    public final BiPredicate<ServerPlayer, EquipmentSlot> activateCombatAbility;
    public final Consumer<ServerPlayer> onRemove;

    public final int activeTicks;
    public final int cooldown;
    public Consumer<ServerPlayer> onCombatAbilityActive = NULL_ACTIVE;


    public ArmorTrimAbility(Consumer<ServerPlayer> onEquip, Consumer<ServerPlayer> onServerPlayerTick,
                            BiPredicate<ServerPlayer, EquipmentSlot> activateCombatAbility, Consumer<ServerPlayer> onRemove) {
        this(onEquip,onServerPlayerTick,activateCombatAbility,onRemove,0, 20 * 20);
    }

    public ArmorTrimAbility(Consumer<ServerPlayer> onEquip, Consumer<ServerPlayer> onServerPlayerTick,
                            BiPredicate<ServerPlayer, EquipmentSlot> activateCombatAbility, Consumer<ServerPlayer> onRemove, int activeTicks, int cooldown) {
        this.onEquip = onEquip;
        this.onServerPlayerTick = onServerPlayerTick;
        this.activateCombatAbility = activateCombatAbility;
        this.onRemove = onRemove;
        this.activeTicks = activeTicks;
        this.cooldown = cooldown;
    }

    public ArmorTrimAbility setOnCombatAbilityActive(Consumer<ServerPlayer> onCombatAbilityActive) {
        this.onCombatAbilityActive = onCombatAbilityActive;
        return this;
    }
}
