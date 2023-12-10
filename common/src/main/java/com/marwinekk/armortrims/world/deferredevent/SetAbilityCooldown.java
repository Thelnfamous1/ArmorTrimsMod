package com.marwinekk.armortrims.world.deferredevent;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SetAbilityCooldown extends DeferredEvent{

    public static final String PLAYER_TAG = "player";
    public static final String SLOT_TAG = "slot";
    public static final String COOLDOWN_TAG = "cooldown";
    private UUID uuid;
    @Nullable
    private EquipmentSlot slot;
    private int cooldown;

    public SetAbilityCooldown() {
        super(DeferredEventTypes.SET_ABILITY_COOLDOWN);
    }

    public SetAbilityCooldown(Player player, @Nullable EquipmentSlot slot, int cooldown) {
        this();
        this.uuid = player.getUUID();
        this.slot = slot;
        this.cooldown = cooldown;
    }

    @Override
    public void writeWithoutMetaData(CompoundTag tag) {
        tag.putUUID(PLAYER_TAG, this.uuid);
        if(this.slot != null){
            tag.putString(SLOT_TAG, slot.getName());
        }
        tag.putInt(COOLDOWN_TAG, this.cooldown);
    }

    @Override
    public boolean attemptRun(ServerLevel level) {
        Player player = level.getPlayerByUUID(uuid);
        if (player != null) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            playerDuck.setAbilityCooldown(this.slot, this.cooldown);
        }
        return true;
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        this.uuid = tag.getUUID(PLAYER_TAG);
        if(tag.contains(SLOT_TAG)){
            this.slot = EquipmentSlot.byName(tag.getString(SLOT_TAG));
        }
        this.cooldown = tag.getInt(COOLDOWN_TAG);
    }
}
