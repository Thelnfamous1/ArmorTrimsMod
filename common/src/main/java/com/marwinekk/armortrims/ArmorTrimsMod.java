package com.marwinekk.armortrims;

import com.marwinekk.armortrims.client.ArmorTrimsModClient;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.platform.Services;
import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ArmorTrimsMod {

    public static final String MOD_ID = "armor_trims";
    public static final String MOD_NAME = "ArmorTrimsMod";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static MinecraftServer server;

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
        if (playerDuck.checkInventory()) {
            handleCheckInventory(player);
            playerDuck.setCheckInventory(false);
        }
        playerDuck.tickSetBonusEffects();
    }

    public static void serverStarted(MinecraftServer server) {
        ArmorTrimsMod.server = server;
    }

    public static void serverStopped(MinecraftServer server) {
        ArmorTrimsMod.server = null;
    }

    public static boolean changeTarget(LivingEntity victim, LivingEntity attacker) {
        if (victim instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            if (playerDuck.hasSetBonus(Items.AMETHYST_SHARD)) {
                return true;
                //"tamed" brutes do not attack player
            } else if (attacker instanceof PiglinBrute piglinBrute) {
                if (player.getUUID().equals(((OwnableEntity) piglinBrute).getOwnerUUID())) {
                    return true;
                }
            }
        }
        return false;
    }


    //this is needed because the client thread calls getAttributes, but a direct call to mc.world would crash servers
    public static Level getWorld() {
        if (server == null) {
            return ArmorTrimsModClient.getClientWorld();
        } else {
            return server.getLevel(Level.OVERWORLD);
        }
    }

    public static boolean onFallDamage(LivingEntity livingEntity) {

        return false;
    }

    public static void serverPlayerLogin(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        playerDuck.setCheckInventory(true);
    }

    public static void handleCheckInventory(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;

        Pair<Set<Item>,Boolean> currentSetPair = lookupTrimMaterials(player);
        Set<Item> currentSet = currentSetPair.getLeft();
        Item regularSetBonus = currentSetPair.getRight() ? getRegularSetBonus(currentSet) : null;
        boolean hasEgg = player.getInventory().items.stream().anyMatch(stack -> stack.is(Items.DRAGON_EGG));

        Set<Item> oldSet = playerDuck.trimMaterials();
        Item oldSetBonus = playerDuck.regularSetBonus();
        boolean oldHasEgg = playerDuck.dragonEgg();

        if (hasEgg != oldHasEgg) {
            //now has egg, didn't before
            if (hasEgg) {
                if (!currentSet.contains(oldSetBonus)) {
                    if (oldSetBonus != null) ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(oldSetBonus).onRemove.accept(player);
                }
                Set<Item> runOn = new HashSet<>(currentSet);
                runOn.remove(oldSetBonus);
                for (Item item : runOn) {
                    ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(item).onEquip.accept(player);
                }
            } else {
                //had egg before, doesn't now
                Set<Item> runOn = new HashSet<>(oldSet);
                runOn.remove(regularSetBonus);

                for (Item item : runOn) {
                    ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(item).onRemove.accept(player);
                }

                if (!oldSet.contains(regularSetBonus)) {
                    if (regularSetBonus != null) ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(regularSetBonus).onEquip.accept(player);
                }
            }
        } else {
            if (hasEgg) {

                Set<Item> newEffects = new HashSet<>(currentSet);
                newEffects.removeAll(oldSet);
                Set<Item> oldEffects = new HashSet<>(oldSet);
                oldEffects.removeAll(currentSet);

                oldEffects.stream().map(ArmorTrimAbilities.ARMOR_TRIM_REGISTRY::get).forEach(armorTrimModifier -> armorTrimModifier.onRemove.accept(player));
                newEffects.stream().map(ArmorTrimAbilities.ARMOR_TRIM_REGISTRY::get).forEach(armorTrimModifier -> armorTrimModifier.onEquip.accept(player));

            } else {
                if (oldSetBonus != regularSetBonus) {
                    if (oldSetBonus!= null) ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(oldSetBonus).onRemove.accept(player);
                    if (regularSetBonus != null) ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(regularSetBonus).onEquip.accept(player);
                }
            }
        }

        playerDuck.setRegularSetBonus(regularSetBonus);
        playerDuck.setTrimMaterials(currentSet);
        playerDuck.setDragonEgg(hasEgg);


        boolean cook = playerDuck.hasSetBonus(Items.QUARTZ);

        if (!cook) return;

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.is(ModTags.QUARTZ_SMELTABLE)) {

            }
        }
    }

    public static final String POWER_TAG = "armor_trims:power";

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
        playerDuck.setCheckInventory(true);
    }

    public static final List<MobEffect> BEACON_EFFECTS = List.of(MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED, MobEffects.DAMAGE_RESISTANCE,
            MobEffects.JUMP, MobEffects.DAMAGE_BOOST, MobEffects.HERO_OF_THE_VILLAGE);

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};


    public static Pair<Set<Item>,Boolean> lookupTrimMaterials(LivingEntity living) {
        Set<Item> items = new HashSet<>();
        boolean complete = true;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            Item item = getTrimItem(living.level(), living.getItemBySlot(slot));
            if (item != null) {
                items.add(item);
            }else {
                complete = false;
            }
        }
        return Pair.of(items,complete && items.size() == 1);
    }

    @Nullable
    public static Item getRegularSetBonus(Set<Item> set) {
        if (set.size() != 1) return null;
        return set.iterator().next();
    }

    public static int countTrim(LivingEntity pLivingEntity, Item trim) {
        int i = 0;
        for (ItemStack itemstack : pLivingEntity.getArmorSlots())
            if (getTrimItem(pLivingEntity.level(), itemstack) == trim) i++;

        return i;
    }

    @Nullable
    public static ArmorTrim getTrim(@Nullable Level level, ItemStack stack) {
        return level == null ? null : ArmorTrim.getTrim(level.registryAccess(), stack).orElse(null);
    }

    @Nullable
    public static TrimMaterial getTrimMaterial(@Nullable Level level, ItemStack stack) {
        ArmorTrim armorTrim = getTrim(level, stack);
        return armorTrim == null ? null : armorTrim.material().value();
    }

    @Nullable
    public static Item getTrimItem(@Nullable Level level, ItemStack stack) {
        TrimMaterial trimMaterial = getTrimMaterial(level, stack);
        return trimMaterial == null ? null : trimMaterial.ingredient().value();
    }

    public static void addOneEffectRemoveOther(ServerPlayer player, MobEffect mobEffect) {
        player.addEffect(new MobEffectInstance(mobEffect, -1, 0, true, false));
        PlayerDuck playerDuck = (PlayerDuck) player;
        MobEffect old = playerDuck.beaconEffect();
        player.removeEffect(old);
        playerDuck.setBeaconEffect(mobEffect);
    }

    //Whats the SMP about?
    //
    //The SMP is based on different armor trims colors and their powers
    //
    // todo Iron: You take no fall damage and no drowning. When activating combat mode, you have a punch that makes players fly 50 blocks in the air.
    //
    // Lapis: Every 10 minutes you gain a level. When activating combat mode, you get a +1 on all your enchants, for example sharpness V turns to sharpness VI
    //
    // Gold: You gain an extra 2 hearts. When activating combat mode, you get 5-6 piglin brutes that attack the nearest players
    //
    // Amethyst: Mobs can't attack you at all. When activating combat mode, you get 2 witches that splash you with healing and regen, and they also give other players slowness and weakness
    //
    // Diamond: Get Unbreaking 4 on all armor. When activating combat mode, you get 3 arrows that have the damage of a power 8 bow shot
    //
    // Netherite: Permanent fire resistance and lava gives regeneration 2.
    //
    //  todo When activating combat mode, you have a punch that withers a player for 5 seconds and resets every hit.
    //
    // Copper: Copper acts as iron and you can craft anything iron-related using copper. When activating combat mode, you have the ability to summon 10 lighting strikes anywhere you look.
    //
    // todo Quartz: Auto smelt ores and food. When activating combat mode, you have a smoke cloud that appears around you and you become completely invisible to all players around you. It also makes every entity around you glow
    //
    // todo Emerald: You can give yourself beacon effects any time. When activating combat mode, you have a 15-second period where if you were to die, you have a totem that saves you.
    //
    // todo Red: Get Blast Resistance 4 on all armor. When activating combat mode, you have 3 homing arrows that explode and deal a lot of damage to players

}