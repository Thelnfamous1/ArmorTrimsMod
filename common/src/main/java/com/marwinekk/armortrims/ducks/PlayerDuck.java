package com.marwinekk.armortrims.ducks;

import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.minecraft.server.level.ServerPlayer;
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
                ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(item, ArmorTrimAbilities.DUMMY).onServerPlayerTick.accept((ServerPlayer) self());
            }
        } else {
            ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(regularSetBonus(), ArmorTrimAbilities.DUMMY).onServerPlayerTick.accept((ServerPlayer) self());
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
