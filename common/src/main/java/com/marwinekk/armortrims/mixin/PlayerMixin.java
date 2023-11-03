package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    private transient Item setBonus;
    transient Set<Item> trimMaterials = new HashSet<>();
    private transient boolean dragonEgg;
    private transient boolean checkInventory;
    private transient MobEffect beaconEffect;
    private final Map<EquipmentSlot,Integer> abilityCooldowns = new HashMap<>();
    private final Map<EquipmentSlot,Integer> abilityTimers = new HashMap<>();

    private int lightningStrikesLeft;

    @Override
    public int lightningStrikesLeft() {
        return lightningStrikesLeft;
    }

    @Override
    public void setLightningStrikesLeft(int lightningStrikesLeft) {
        this.lightningStrikesLeft = lightningStrikesLeft;
    }

    @Inject(method = "setItemSlot",at = @At("RETURN"))
    private void armorInventoryChanged(EquipmentSlot $$0, ItemStack $$1, CallbackInfo ci) {
        ArmorTrimsMod.onInventoryChange($elf().getInventory(),$$0);
    }

    public Map<EquipmentSlot, Integer> abilityCooldowns() {
        return abilityCooldowns;
    }

    @Override
    public Map<EquipmentSlot, Integer> abilityTimers() {
        return abilityTimers;
    }

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

    public void setAbilityCooldown(EquipmentSlot slot, int abilityCooldown) {
        this.abilityCooldowns.put(slot,abilityCooldown);
    }

    public int abilityCooldown(EquipmentSlot slot) {
        return abilityCooldowns.getOrDefault(slot,0);
    }

    @Override
    public int abilityTimer(EquipmentSlot slot) {
        return abilityTimers.getOrDefault(slot,0);
    }

    @Override
    public void setAbilityTimer(EquipmentSlot slot, int timer) {
        abilityTimers.put(slot,timer);
    }

    @Override
    public void setCheckInventory(boolean checkInventory) {
        this.checkInventory = checkInventory;
    }
}