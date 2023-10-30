package com.marwinekk.armortrims.ducks;

import com.marwinekk.armortrims.util.ArmorTrimTicks;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Set;

public interface PlayerDuck {

    default Player self() {
        return (Player) this;
    }

    Item regularSetBonus();

    Set<Item> trimMaterials();
    void setTrimMaterials(Set<Item> item);
    boolean dragonEgg();

    MobEffect beaconEffect();
    void setBeaconEffect(MobEffect beaconEffect);

    default void tickSetBonusEffects() {
        if (dragonEgg()) {
            for (Item item : trimMaterials()) {
                ArmorTrimTicks.TRIM_SET_TICKS.getOrDefault(item,player -> {}).accept(self());
            }
        } else {
            ArmorTrimTicks.TRIM_SET_TICKS.getOrDefault(regularSetBonus(),player -> {}).accept(self());
        }
    }

    default boolean hasSetBonus(Item item) {
        return dragonEgg() ? trimMaterials().contains(item) : regularSetBonus() == item;
    }

    boolean checkInventory();
    void setCheckInventory(boolean checkInventory);

    void applyRegularSetBonus(Item item);
    void setDragonEgg(boolean dragonegg);

}
