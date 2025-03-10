package com.marwinekk.armortrims.mixin;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.util.CopperTrimAbilities;
import com.marwinekk.armortrims.util.DiamondTrimAbilities;
import com.marwinekk.armortrims.util.RedstoneTrimAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(Player.class)
abstract class PlayerMixin  extends LivingEntity implements PlayerDuck {

    private transient Item setBonus;
    transient Set<Item> trimMaterials = new HashSet<>();
    private transient boolean dragonEgg;
    private transient boolean checkInventory;
    private transient MobEffect beaconEffect;

    private static final EntityDataAccessor<CompoundTag> ARMOR_TRIMS_DATA = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    @Unique
    private boolean armorTrimsMod$doubleJumping;

    protected PlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }


    @Inject(method = "defineSynchedData",at = @At("RETURN"))
    private void registerCustom(CallbackInfo ci) {
        this.entityData.define(ARMOR_TRIMS_DATA,new CompoundTag());
    }

    @Override
    public CompoundTag getArmorTrimsData() {
        return entityData.get(ARMOR_TRIMS_DATA);
    }

    @Override
    public void setArmorTrimsData(CompoundTag tag) {
        entityData.set(ARMOR_TRIMS_DATA,tag,true);
    }


    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addExtra(CompoundTag tag, CallbackInfo ci) {
        tag.put("mod_data",getArmorTrimsData());
        MobEffect beaconEffect = beaconEffect();
        if(beaconEffect != null){
            tag.putInt("beacon_effect", MobEffect.getId(beaconEffect));
        }
    }

    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readExtra(CompoundTag tag, CallbackInfo ci) {
        setArmorTrimsData(tag.getCompound("mod_data"));
        if(tag.contains("beacon_effect", Tag.TAG_INT)){
            setBeaconEffect(MobEffect.byId(tag.getInt("beacon_effect")));
        }
    }

    private int lightningStrikesLeft = CopperTrimAbilities.COPPER_ABILITY_USES;

    private int redstoneArrowsLeft = RedstoneTrimAbilities.REDSTONE_ABILITY_USES;

    private int diamondArrowsLeft = DiamondTrimAbilities.DIAMOND_ABILITY_USES;

    @Override
    public int redstoneArrowsLeft() {
        return redstoneArrowsLeft;
    }

    @Override
    public void setRedstoneArrowsLeft(int redstoneArrowsLeft) {
        this.redstoneArrowsLeft = redstoneArrowsLeft;
    }

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

    @Override
    public int diamondArrowsLeft() {
        return this.diamondArrowsLeft;
    }

    @Override
    public void setDiamondArrowsLeft(int arrowsLeft) {
        this.diamondArrowsLeft = arrowsLeft;
    }

    @Override
    public void armorTrimsMod$setDoubleJumping(boolean doubleJumping) {
        this.armorTrimsMod$doubleJumping = doubleJumping;
    }

    @Override
    public boolean armorTrimsMod$isDoubleJumping() {
        return this.armorTrimsMod$doubleJumping;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void handleAiStep(CallbackInfo ci){
        if(this.onGround() || this.onClimbable()){
            this.armorTrimsMod$setDoubleJumping(false);
        }
    }
}