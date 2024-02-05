package com.marwinekk.armortrims.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class ArmorTrimAbility {

    public static final Consumer<ServerPlayer> NULL = player -> {
    };
    public final Consumer<ServerPlayer> onEquip;
    public final Consumer<ServerPlayer> onServerPlayerTick;
    public final BiPredicate<ServerPlayer, EquipmentSlot> activateCombatAbility;
    public final Consumer<ServerPlayer> onRemove;

    public final int activeTicks;
    public final int cooldown;
    private Consumer<ServerPlayer> onCombatAbilityActive = NULL;
    private Consumer<ServerPlayer> onCombatAbilityActiveInactive = NULL;
    private boolean runEveryEquip;
    private Consumer<ServerPlayer> onCombatAbilityActivated = NULL;


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

    public ArmorTrimAbility setOnCombatAbilityActivated(Consumer<ServerPlayer> onCombatAbilityActivated) {
        this.onCombatAbilityActivated = onCombatAbilityActivated;
        return this;
    }

    public ArmorTrimAbility setOnCombatAbilityActive(Consumer<ServerPlayer> onCombatAbilityActive) {
        this.onCombatAbilityActive = onCombatAbilityActive;
        return this;
    }

    public ArmorTrimAbility setOnCombatAbilityInactive(Consumer<ServerPlayer> onCombatAbilityActive) {
        this.onCombatAbilityActiveInactive = onCombatAbilityActive;
        return this;
    }

    public ArmorTrimAbility runEveryEquip(){
        this.runEveryEquip = true;
        return this;
    }

    public Consumer<ServerPlayer> getOnCombatAbilityActive() {
        return onCombatAbilityActive;
    }

    public Consumer<ServerPlayer> getOnCombatAbilityActiveInactive() {
        return onCombatAbilityActiveInactive;
    }

    public boolean isRunEveryEquip() {
        return runEveryEquip;
    }

    public Consumer<ServerPlayer> onCombatAbilityActivated() {
        return this.onCombatAbilityActivated;
    }
}
