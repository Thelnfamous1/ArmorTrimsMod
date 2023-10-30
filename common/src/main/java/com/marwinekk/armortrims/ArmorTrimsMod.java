package com.marwinekk.armortrims;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.platform.Services;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ArmorTrimsMod {

    public static final String MOD_ID = "armor_trims";
    public static final String MOD_NAME = "ArmorTrimsMod";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("examplemod")) {

            LOG.info("Hello to examplemod");
        }
    }

    public static void tickServerPlayer(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        Item[] set = getSet(player);
        Item regularSetBonus = getRegularSetBonus(set);
        playerDuck.applyRegularSetBonus(regularSetBonus);
        if (playerDuck.checkInventory()) {
            handleCheckInventory(player);
            playerDuck.setCheckInventory(false);
        }

        playerDuck.tickSetBonusEffects();

    }

    public static boolean changeTarget(LivingEntity victim, LivingEntity attacker) {
        if (victim instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            if (playerDuck.hasSetBonus(Items.AMETHYST_SHARD)) {
                return true;
                //"tamed" brutes do not attack player
            } else if (attacker instanceof PiglinBrute piglinBrute) {
                if (player.getUUID().equals(((OwnableEntity)piglinBrute).getOwnerUUID())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean onFallDamage(LivingEntity livingEntity) {

        return false;
    }

    public static void serverPlayerLogin(ServerPlayer player) {

    }

    public static void handleCheckInventory(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;

        playerDuck.setDragonEgg(false);
        if (player.getInventory().items.stream().anyMatch(stack -> stack.is(Items.DRAGON_EGG))) {
            playerDuck.setDragonEgg(true);
        }

        boolean cook = playerDuck.hasSetBonus(Items.QUARTZ);

        if (!cook)return;

        for (int i = 0; i < player.getInventory().items.size();i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.is(ModTags.QUARTZ_SMELTABLE)) {

            }
        }
    }

    public static boolean attackEvent(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            return source.is(DamageTypes.LIGHTNING_BOLT) && playerDuck.hasSetBonus(Items.COPPER_INGOT);
        }
        return false;
    }

    public static void onInventoryChange(Inventory inventory, int slot, ItemStack stack) {
        Player player = inventory.player;
        PlayerDuck playerDuck = (PlayerDuck) player;

        if (playerDuck.hasSetBonus(Items.QUARTZ) && stack.is(ModTags.QUARTZ_SMELTABLE)) {
            playerDuck.setCheckInventory(true);
        }
    }

    public static final List<MobEffect> BEACON_EFFECTS = List.of(MobEffects.MOVEMENT_SPEED,MobEffects.DIG_SPEED,MobEffects.DAMAGE_RESISTANCE,
            MobEffects.JUMP,MobEffects.DAMAGE_BOOST,MobEffects.HERO_OF_THE_VILLAGE);

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};


    public static Item[] getSet(LivingEntity living) {
        Item[] items = new Item[ARMOR_SLOTS.length];
        for (int i = 0; i < ARMOR_SLOTS.length; i++) {
            EquipmentSlot slot = ARMOR_SLOTS[i];
            items[i] = getTrimItem(living.level(),living.getItemBySlot(slot));
        }
        return items;
    }

    @Nullable
    public static Item getRegularSetBonus(Item[] set) {
        Item first = set[0];
        if (first == null) return null;
        for (int i = 1; i < set.length; i++) {
            Item item = set[i];
            if (item != first) return null;
        }
        return first;
    }
    public static int countTrim(LivingEntity pLivingEntity, Item trim) {
        int i = 0;
        for(ItemStack itemstack : pLivingEntity.getArmorSlots())
            if (getTrimItem(pLivingEntity.level(), itemstack) == trim) i++;

        return i;
    }

    @Nullable
    public static ArmorTrim getTrim(@Nullable Level level, ItemStack stack) {
        return level == null ? null : ArmorTrim.getTrim(level.registryAccess(),stack).orElse(null);
    }

    @Nullable
    public static TrimMaterial getTrimMaterial(@Nullable Level level, ItemStack stack) {
        ArmorTrim armorTrim = getTrim(level, stack);
        return armorTrim == null ? null : armorTrim.material().value();
    }

    @Nullable
    public static Item getTrimItem(@Nullable Level level, ItemStack stack) {
        TrimMaterial trimMaterial = getTrimMaterial(level,stack);
        return trimMaterial == null ? null : trimMaterial.ingredient().value();
    }

    public static void addOneEffectRemoveOther(ServerPlayer player,MobEffect mobEffect) {
        player.addEffect(new MobEffectInstance(mobEffect, -1, 0, true, false));
        PlayerDuck playerDuck = (PlayerDuck) player;
        MobEffect old = playerDuck.beaconEffect();
        player.removeEffect(old);
        playerDuck.setBeaconEffect(mobEffect);
    }
}