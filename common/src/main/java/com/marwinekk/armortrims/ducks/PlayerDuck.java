package com.marwinekk.armortrims.ducks;

import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import com.marwinekk.armortrims.util.ArmorTrimAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface PlayerDuck {

    default Player $elf() {
        return (Player) this;
    }

    Item regularSetBonus();


    CompoundTag getArmorTrimsData();
    void setArmorTrimsData(CompoundTag tag);


    int lightningStrikesLeft();
    void setLightningStrikesLeft(int strikesLeft);

    int redstoneArrowsLeft();
    void setRedstoneArrowsLeft(int strikesLeft);

    Set<Item> trimMaterials();
    void setTrimMaterials(Set<Item> item);
    boolean dragonEgg();

    MobEffect beaconEffect();
    void setBeaconEffect(MobEffect beaconEffect);

    default void setAbilityCooldown(@Nullable EquipmentSlot slot, int abilityCooldown) {
        CompoundTag tag = abilityCooldowns();
        tag.putInt(slot == null ? "null": slot.getName(),abilityCooldown);
        CompoundTag armorTrimData = getArmorTrimsData();
        armorTrimData.put(AB_COOLDOWN,tag);
        setArmorTrimsData(armorTrimData);
    }

    String AB_COOLDOWN = "abilityCooldowns";

    String AB_TIMER = "abilityTimers";

    default int abilityCooldown(@Nullable EquipmentSlot slot) {
        return abilityCooldowns().getInt(slot == null ? "null": slot.getName());
    }
    default CompoundTag abilityCooldowns() {
        return getArmorTrimsData().getCompound(AB_COOLDOWN);
    }

    default int abilityTimer(@Nullable EquipmentSlot slot){
        return abilityTimers().getInt(slot == null ? "null": slot.getName());
    }

    default void setAbilityTimer(@Nullable EquipmentSlot slot,int timer){
        CompoundTag tag = abilityTimers();
        tag.putInt(slot == null ? "null": slot.getName(), timer);
        CompoundTag armorTrimData = getArmorTrimsData();
        armorTrimData.put(AB_TIMER,tag);
        setArmorTrimsData(armorTrimData);
    }

    default CompoundTag abilityTimers(){
        return getArmorTrimsData().getCompound(AB_TIMER);
    }

    default void tickServer() {
        CompoundTag armorTrimsData = getArmorTrimsData();
        CompoundTag abilityCooldowns = abilityCooldowns();
        for (String entry : abilityCooldowns.getAllKeys()) {
            int v = abilityCooldowns.getInt(entry);
            if (v > 0) {
                abilityCooldowns.putInt(entry, v - 1);
                armorTrimsData.put(AB_COOLDOWN,abilityCooldowns);
            }
        }

        CompoundTag abilityTimers = abilityTimers();
        boolean active = false;
        for (String entry : abilityTimers().getAllKeys()) {
            int v = abilityTimers.getInt(entry);
            if (v > 0) {
                abilityTimers.putInt(entry, v - 1);
                armorTrimsData.put(AB_TIMER,abilityTimers);
                if(v - 1 > 0) active = true;
            }
        }

        setArmorTrimsData(armorTrimsData);

        if (dragonEgg()) {
            for (Item item : trimMaterials()) {
                ArmorTrimAbility armorTrimAbility = ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(item, ArmorTrimAbilities.DUMMY);
                armorTrimAbility.onServerPlayerTick.accept((ServerPlayer) $elf());
                if(active) armorTrimAbility.getOnCombatAbilityActive().accept((ServerPlayer) $elf());
                else armorTrimAbility.getOnCombatAbilityActiveInactive().accept((ServerPlayer) $elf());
            }
        } else {
            ArmorTrimAbility armorTrimAbility = ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(regularSetBonus(), ArmorTrimAbilities.DUMMY);
            armorTrimAbility.onServerPlayerTick.accept((ServerPlayer) $elf());
            if(active) armorTrimAbility.getOnCombatAbilityActive().accept((ServerPlayer) $elf());
            else armorTrimAbility.getOnCombatAbilityActiveInactive().accept((ServerPlayer) $elf());
        }
    }

    default boolean hasSetBonus(Item item) {
        return dragonEgg() ? trimMaterials().contains(item) : regularSetBonus() == item;
    }

    boolean checkInventory();
    void setCheckInventory(boolean checkInventory);

    void setRegularSetBonus(Item item);
    void setDragonEgg(boolean dragonegg);

    int diamondArrowsLeft();

    void setDiamondArrowsLeft(int arrowsLeft);

    void armorTrimsMod$setDoubleJumping(boolean doubleJumping);

    boolean armorTrimsMod$isDoubleJumping();
}
