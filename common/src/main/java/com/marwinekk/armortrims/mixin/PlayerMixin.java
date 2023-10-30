package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashSet;
import java.util.Set;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    private transient Item setBonus;
    transient Set<Item> trimMaterials = new HashSet<>();
    private transient boolean dragonEgg;
    private transient boolean checkInventory;
    private transient MobEffect beaconEffect;

    @Override
    public MobEffect beaconEffect() {
        return beaconEffect;
    }

    @Override
    public void setBeaconEffect(MobEffect beaconEffect) {
        this.beaconEffect = beaconEffect;
    }

    @Override
    public Item regularSetBonus() {
        return setBonus;
    }

    @Override
    public Set<Item> trimMaterials() {
        return trimMaterials;
    }

    @Override
    public void setTrimMaterials(Set<Item> trimMaterials) {
        this.trimMaterials = trimMaterials;
    }

    @Override
    public boolean dragonEgg() {
        return dragonEgg;
    }

    @Override
    public void setRegularSetBonus(Item item) {
        this.setBonus = item;
    }

    @Override
    public void setDragonEgg(boolean dragonegg) {
        dragonEgg = dragonegg;
    }

    @Override
    public boolean checkInventory() {
        return checkInventory;
    }

    @Override
    public void setCheckInventory(boolean checkInventory) {
        this.checkInventory = checkInventory;
    }
}