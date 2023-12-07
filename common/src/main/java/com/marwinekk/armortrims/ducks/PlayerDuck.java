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

import java.util.Map;
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

    default int abilityCooldown(EquipmentSlot slot) {
        return abilityCooldowns().getInt(slot == null ? "null": slot.getName());
    }
    default CompoundTag abilityCooldowns() {
        return getArmorTrimsData().getCompound(AB_COOLDOWN);
    }

    int abilityTimer(EquipmentSlot slot);
    void setAbilityTimer(EquipmentSlot slot,int timer);
    Map<EquipmentSlot,Integer> abilityTimers();


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

        boolean active = false;
        for (Map.Entry<EquipmentSlot,Integer> entry : abilityTimers().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            int v = entry.getValue();
            if (v > 0) {
                setAbilityTimer(slot, v - 1);
                active = true;
            }
        }

        setArmorTrimsData(armorTrimsData);

        if (dragonEgg()) {
            for (Item item : trimMaterials()) {
                ArmorTrimAbility armorTrimAbility = ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(item, ArmorTrimAbilities.DUMMY);
                armorTrimAbility.onServerPlayerTick.accept((ServerPlayer) $elf());
                if(active) armorTrimAbility.onCombatAbilityActive.accept((ServerPlayer) $elf());
            }
        } else {
            ArmorTrimAbility armorTrimAbility = ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(regularSetBonus(), ArmorTrimAbilities.DUMMY);
            armorTrimAbility.onServerPlayerTick.accept((ServerPlayer) $elf());
            if(active) armorTrimAbility.onCombatAbilityActive.accept((ServerPlayer) $elf());
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
}
