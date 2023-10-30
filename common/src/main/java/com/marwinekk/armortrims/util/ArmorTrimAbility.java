package com.marwinekk.armortrims.util;

import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class ArmorTrimAbility {

    public final Consumer<ServerPlayer> onEquip;
    public final Consumer<ServerPlayer> onServerPlayerTick;
    public final Consumer<ServerPlayer> activateCombatAbility;
    public final Consumer<ServerPlayer> onRemove;

    public ArmorTrimAbility(Consumer<ServerPlayer> onEquip, Consumer<ServerPlayer> onServerPlayerTick,
                            Consumer<ServerPlayer> activateCombatAbility, Consumer<ServerPlayer> onRemove) {
        this.onEquip = onEquip;
        this.onServerPlayerTick = onServerPlayerTick;
        this.activateCombatAbility = activateCombatAbility;
        this.onRemove = onRemove;
    }
}
