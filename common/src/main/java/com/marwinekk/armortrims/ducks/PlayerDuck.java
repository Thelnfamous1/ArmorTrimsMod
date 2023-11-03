package com.marwinekk.armortrims.ducks;

import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.Set;

public interface PlayerDuck {

    default Player $elf() {
        return (Player) this;
    }

    Item regularSetBonus();

    int lightningStrikesLeft();
    void setLightningStrikesLeft(int strikesLeft);

    Set<Item> trimMaterials();
    void setTrimMaterials(Set<Item> item);
    boolean dragonEgg();

    MobEffect beaconEffect();
    void setBeaconEffect(MobEffect beaconEffect);

    int abilityCooldown(EquipmentSlot slot);
    void setAbilityCooldown(EquipmentSlot slot,int cooldown);
    Map<EquipmentSlot,Integer> abilityCooldowns();

    int abilityTimer(EquipmentSlot slot);
    void setAbilityTimer(EquipmentSlot slot,int timer);
    Map<EquipmentSlot,Integer> abilityTimers();


    default void tickServer() {

        for (Map.Entry<EquipmentSlot,Integer> entry : abilityCooldowns().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            int v = entry.getValue();
            if (v > 0) {
                setAbilityCooldown(slot, v - 1);
            }
        }

        for (Map.Entry<EquipmentSlot,Integer> entry : abilityTimers().entrySet()) {
            EquipmentSlot slot = entry.getKey();
            int v = entry.getValue();
            if (v > 0) {
                setAbilityTimer(slot, v - 1);
                $elf().sendSystemMessage(Component.literal("slot: " + slot + " " + v));
            }
        }

        if (dragonEgg()) {
            for (Item item : trimMaterials()) {
                ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(item, ArmorTrimAbilities.DUMMY).onServerPlayerTick.accept((ServerPlayer) $elf());
            }
        } else {
            ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(regularSetBonus(), ArmorTrimAbilities.DUMMY).onServerPlayerTick.accept((ServerPlayer) $elf());
        }
    }

    default boolean hasSetBonus(Item item) {
        return dragonEgg() ? trimMaterials().contains(item) : regularSetBonus() == item;
    }

    boolean checkInventory();
    void setCheckInventory(boolean checkInventory);

    void setRegularSetBonus(Item item);
    void setDragonEgg(boolean dragonegg);

}
