package com.marwinekk.armortrims;

import com.marwinekk.armortrims.client.ArmorTrimsModClient;
import com.marwinekk.armortrims.commands.ATCommands;
import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.ducks.WitchDuck;
import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import com.marwinekk.armortrims.util.ArmorTrimAbility;
import com.marwinekk.armortrims.world.deferredevent.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class ArmorTrimsMod {

    public static final String MOD_ID = "armor_trims";
    public static final String MOD_NAME = "ArmorTrimsMod";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final String ARMOR_TRIMS_LOCKED_TAG = "armor_trims:locked";
    public static final Consumer<ItemStack> LOCK_SLOT = stack -> {
        if(!stack.isEmpty()) setLocked(stack, true);
    };
    public static final Consumer<ItemStack> UNLOCK_SLOT = stack -> {
        if(!stack.isEmpty() && isLocked(stack)) setLocked(stack, false);
    };
    public static final int BONUS_SLOTS = 5;
    private static final String ENCHANT_BOOSTS = "armor_trims:enchant_boosts";
    private static final String BONUS_ENCHANTS = "armor_trims:bonus_enchants";

    public static MinecraftServer server;

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
    }

    public static boolean manepear;

    public static void serverStarted(MinecraftServer server) {
        ArmorTrimsMod.server = server;
        GameRules.BooleanValue gameRule =server.getLevel(Level.OVERWORLD).getGameRules().getRule(GameRules.RULE_LIMITED_CRAFTING);
        gameRule.set(true,server);
    }

    public static void tickLevel(ServerLevel level) {
        getDeferredEventSystem(level).tickDeferredEvents(level);
    }

    public static void serverStopped(MinecraftServer server) {
        ArmorTrimsMod.server = null;
    }

    public static void tickPlayer(Player player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        if (playerDuck.checkInventory()) {
            handleCheckInventory(player);
            playerDuck.setCheckInventory(false);
        }
        if (!player.level().isClientSide) {
            playerDuck.tickServer();
        }
    }

    public static DeferredEventSystem getDeferredEventSystem(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(DeferredEventSystem::loadStatic,DeferredEventSystem::new,MOD_ID+ ":deferredevents");
    }

    public static void addDeferredEvent(ServerLevel level, DeferredEvent event) {
        getDeferredEventSystem(level).addDeferredEvent(event);
    }

    public static void removeDeferredEvent(ServerLevel level, Predicate<DeferredEvent> predicate){
        getDeferredEventSystem(level).removeDeferredEvent(predicate);
    }

    public static boolean changeTarget(LivingEntity victim, LivingEntity attacker) {
        if (victim instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            if (playerDuck.hasSetBonus(Items.AMETHYST_SHARD)) {
                //allow the witch to "attack" friendly players
                return !(attacker instanceof WitchDuck witchDuck) || !victim.getUUID().equals(witchDuck.getOwnerUUID());
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
        if (livingEntity instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            return playerDuck.hasSetBonus(Items.IRON_INGOT);
        }
        return false;
    }

    public static final EquipmentSlot[] slots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, null};

    public static void onDamaged(LivingEntity victim, DamageSource source) {
        if (source.getEntity() instanceof Player attacker) {
            boolean manepear = ArmorTrimsMod.manepear && ATCommands.isNamed(ATCommands.MANE_PEAR, attacker);
            if(manepear){
                applyWitherPunch(victim);
            }

            PlayerDuck playerDuck = (PlayerDuck) attacker;
            for (EquipmentSlot slot : slots) {
                Item trim = slot == null ? playerDuck.regularSetBonus() : getTrimItem(attacker.level(), attacker.getItemBySlot(slot));

                if (trim != null) {
                    int timer = playerDuck.abilityTimer(slot);
                    if (timer > 0) {
                        if (trim == Items.NETHERITE_INGOT) {
                            applyWitherPunch(victim);
                        } else if (trim == Items.IRON_INGOT) {
                            IronFist ironFist = new IronFist(victim);
                            ironFist.setTimer(2);
                            addDeferredEvent((ServerLevel) victim.level(),ironFist);
                        }
                    }
                }
            }
        }
    }

    private static void applyWitherPunch(LivingEntity victim) {
        victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 8 * 20, 0));
        victim.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8 * 20, 0));
        victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 8 * 20, 0));
    }

    public static float onKnockback(double original,LivingEntity victim) {
            LivingEntity lastAttacker = victim.getLastAttacker();
            if (lastAttacker instanceof Player playerAttacker) {
                PlayerDuck playerDuck = (PlayerDuck) playerAttacker;
                for (EquipmentSlot slot : slots) {
                    Item trim = slot == null ? playerDuck.regularSetBonus() : getTrimItem(playerAttacker.level(), playerAttacker.getItemBySlot(slot));
                    int timer = playerDuck.abilityTimer(slot);
                    if (trim == Items.IRON_INGOT && timer > 0) {
                        //	victim.addDeltaMovement(new Vec3(0,4,0));
                        return 0; // neutralize vanilla knockback if attacker has iron fists ability active, so Iron Fists can work
                    }
                }
            }
            if(victim instanceof Player playerVictim){
                PlayerDuck playerDuck = (PlayerDuck) playerVictim;
                for (EquipmentSlot slot : slots) {
                    Item trim = slot == null ? playerDuck.regularSetBonus() : getTrimItem(playerVictim.level(), playerVictim.getItemBySlot(slot));
                    int timer = playerDuck.abilityTimer(slot);
                    if (trim == Items.IRON_INGOT && timer > 0) {
                        //	victim.addDeltaMovement(new Vec3(0,4,0));
                        return 0; // neutralize vanilla knockback if victim has iron fists ability active
                    }
                }
            }
            return (float) original;
        }

    public static void playerLogin(Player player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        playerDuck.setCheckInventory(true);
    }

    public static void handleCheckInventory(Player player) {
        PlayerDuck playerDuck = (PlayerDuck) player;

        Pair<Set<Item>, Boolean> currentSetPair = lookupTrimMaterials(player);
        Set<Item> currentSet = currentSetPair.getLeft();
        Item regularSetBonus = currentSetPair.getRight() ? getRegularSetBonus(currentSet) : null;
        boolean hasEgg = player.getInventory().items.stream().anyMatch(stack -> stack.is(Items.DRAGON_EGG));

        Set<Item> oldSet = playerDuck.trimMaterials();
        Item oldSetBonus = playerDuck.regularSetBonus();
        boolean oldHasEgg = playerDuck.dragonEgg();

        if (!player.level().isClientSide) {

            ServerPlayer serverPlayer = (ServerPlayer) player;

            if (hasEgg != oldHasEgg) {
                //now has egg, didn't before
                if (hasEgg) {
                    if (!currentSet.contains(oldSetBonus) || isRunEveryEquip(oldSetBonus)) {
                        if (oldSetBonus != null)
                            ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(oldSetBonus).onRemove.accept(serverPlayer);
                    }
                    Set<Item> runOn = new HashSet<>(currentSet);
                    if(!isRunEveryEquip(oldSetBonus)) runOn.remove(oldSetBonus);
                    for (Item item : runOn) {
                        ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(item).onEquip.accept(serverPlayer);
                    }
                } else {
                    //had egg before, doesn't now
                    Set<Item> runOn = new HashSet<>(oldSet);
                    runOn.remove(regularSetBonus);

                    for (Item item : runOn) {
                        ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(item).onRemove.accept(serverPlayer);
                    }

                    if (!oldSet.contains(regularSetBonus)) {
                        if (regularSetBonus != null)
                            ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(regularSetBonus).onEquip.accept(serverPlayer);
                    }
                }
            } else {
                if (hasEgg) {

                    Set<Item> newEffects = new HashSet<>(currentSet);
                    newEffects.removeIf(item -> oldSet.contains(item) && !isRunEveryEquip(item));
                    Set<Item> oldEffects = new HashSet<>(oldSet);
                    oldEffects.removeIf(item -> currentSet.contains(item) && !isRunEveryEquip(item));

                    oldEffects.stream().map(ArmorTrimAbilities.ARMOR_TRIM_REGISTRY::get).forEach(armorTrimModifier -> armorTrimModifier.onRemove.accept(serverPlayer));
                    newEffects.stream().map(ArmorTrimAbilities.ARMOR_TRIM_REGISTRY::get).forEach(armorTrimModifier -> armorTrimModifier.onEquip.accept(serverPlayer));

                } else {
                    if (oldSetBonus != regularSetBonus) {
                        if (oldSetBonus != null)
                            ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(oldSetBonus).onRemove.accept(serverPlayer);
                        if (regularSetBonus != null)
                            ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(regularSetBonus, ArmorTrimAbilities.DUMMY).onEquip.accept(serverPlayer);
                    }
                }
            }
        }

        playerDuck.setRegularSetBonus(regularSetBonus);
        playerDuck.setTrimMaterials(currentSet);
        playerDuck.setDragonEgg(hasEgg);


        boolean cook = playerDuck.hasSetBonus(Items.QUARTZ);

        if (player.level().isClientSide || !cook) return;

        List<ItemStack> cooked = new ArrayList<>();

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.is(ModTags.QUARTZ_SMELTABLE)) {
                RecipeManager recipemanager = player.level().getRecipeManager();
                SimpleContainer simpleContainer = new SimpleContainer(stack);
                Optional<SmeltingRecipe> optional = recipemanager.getRecipeFor(RecipeType.SMELTING,simpleContainer,player.level());
                if (optional.isPresent()) {
                    while(stack.getCount() > 0){
                        SmeltingRecipe smeltingRecipe = optional.get();
                        ItemStack result = smeltingRecipe.assemble(simpleContainer,player.level().registryAccess());
                        cooked.add(result);
                        stack.shrink(1);
                    }
                }
            }
        }

        for (ItemStack stack : cooked) {
            player.getInventory().add(stack);
        }

    }

    private static boolean isRunEveryEquip(@Nullable Item item) {
        if(item == null) return false;
        return ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.getOrDefault(item, ArmorTrimAbilities.DUMMY).isRunEveryEquip();
    }

    public static final String POWER_TAG = "armor_trims:power";
    public static final String TNT_TAG = "armor_trims:tnt";

    public static boolean attackEvent(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            return source.is(DamageTypes.LIGHTNING_BOLT) && playerDuck.hasSetBonus(Items.COPPER_INGOT);
        }
        return false;
    }

    public static void onInventoryChange(Inventory inventory, EquipmentSlot slot) {
        Player player = inventory.player;
        PlayerDuck playerDuck = (PlayerDuck) player;
        playerDuck.setCheckInventory(true);
        playerDuck.setAbilityTimer(slot, 0);
    }

    public static final List<Pair<MobEffect, Integer>> BEACON_EFFECTS = List.of(
            Pair.of(MobEffects.MOVEMENT_SPEED, 0),
            Pair.of(MobEffects.DIG_SPEED, 1),
            Pair.of(MobEffects.DAMAGE_RESISTANCE, 0),
            Pair.of(MobEffects.JUMP, 1),
            Pair.of(MobEffects.DAMAGE_BOOST, 0),
            Pair.of(MobEffects.HERO_OF_THE_VILLAGE, 9));

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};


    public static Pair<Set<Item>, Boolean> lookupTrimMaterials(LivingEntity living) {
        Set<Item> items = new HashSet<>();
        boolean complete = true;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            Item item = getTrimItem(living.level(), living.getItemBySlot(slot));
            if (item != null) {
                items.add(item);
            } else {
                complete = false;
            }
        }
        return Pair.of(items, complete && items.size() == 1);
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


    public static void activateCombatAbility(ServerPlayer player, @Nullable EquipmentSlot slot) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        int cooldown = playerDuck.abilityCooldown(slot);
        if (cooldown > 0) {
            player.sendSystemMessage(Component.translatable("Can't use " + (slot != null ? slot : "SET") + " trim ability yet"), true);
            return;
        }
        int timer = playerDuck.abilityTimer(slot);
        if (timer > 0) {
            player.sendSystemMessage(Component.translatable((slot != null ? slot : "SET") + " trim ability is already active"), true);
            return;
        }

        if (!playerDuck.dragonEgg() && slot != null) {
            player.sendSystemMessage(Component.translatable("No dragon egg"));
            return;
        }

        if(playerDuck.dragonEgg() && slot == null){
            player.sendSystemMessage(Component.translatable("Can't use SET trim ability with a dragon egg"), true);
            return;
        }

        Item trimItem = getTrimItemOrSetBonus(player, slot, playerDuck);
        if(trimItem != null){
            for(EquipmentSlot otherSlot : slots){
                if(otherSlot == slot) continue;
                int otherCooldown = playerDuck.abilityCooldown(otherSlot);
                int otherTimer = playerDuck.abilityTimer(otherSlot);
                if((otherCooldown > 0 || otherTimer > 0) && getTrimItemOrSetBonus(player, otherSlot, playerDuck) == trimItem){
                    player.sendSystemMessage(Component.translatable("Can't use " + (slot != null ? slot : "SET") + " trim ability yet"), true);
                    return;
                }
            }
        }

        if (trimItem != null) {
            ArmorTrimAbility armorTrimAbility = ArmorTrimAbilities.ARMOR_TRIM_REGISTRY.get(trimItem);
            boolean shouldApplyCooldown = armorTrimAbility.activateCombatAbility.test(player,slot);
            if (shouldApplyCooldown){
                SetAbilityCooldown setAbilityCooldown = new SetAbilityCooldown(player, slot, armorTrimAbility.cooldown);
                setAbilityCooldown.setTimer(armorTrimAbility.activeTicks);
                addDeferredEvent(player.serverLevel(),setAbilityCooldown);
            }
            playerDuck.setAbilityTimer(slot, armorTrimAbility.activeTicks);
            //LOG.info("Activated " + trimItem + " combat ability for slot " + (slot == null ? "set" : slot));
        }
    }

    @Nullable
    private static Item getTrimItemOrSetBonus(ServerPlayer player, @Nullable EquipmentSlot slot, PlayerDuck playerDuck) {
        return slot != null ? getTrimItem(player.level(), player.getItemBySlot(slot)) : playerDuck.regularSetBonus();
    }

    public static void lockAllSlots(ServerPlayer player){
        ArmorTrimAbilities.applyToAllSlots(player, LOCK_SLOT);
    }

    public static void unlockAllSlots(ServerPlayer player){
        ArmorTrimAbilities.applyToAllSlots(player, UNLOCK_SLOT);
    }

    public static boolean isLocked(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(ARMOR_TRIMS_LOCKED_TAG);
    }

    public static void setLocked(ItemStack stack, boolean locked) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(ARMOR_TRIMS_LOCKED_TAG, locked);
    }

    public static boolean hasEnchantBoosts(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(ENCHANT_BOOSTS);
    }

    public static void setEnchantBoosts(ItemStack stack, boolean enchantBoosts) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(ENCHANT_BOOSTS, enchantBoosts);
    }

    public static int getBonusEnchant(ItemStack stack, Enchantment enchantment) {
        CompoundTag tag = stack.getTag();
        if(tag == null) return 0;
        if(!tag.contains(BONUS_ENCHANTS, Tag.TAG_LIST)) return 0;
        ListTag bonusEnchants = tag.getList(BONUS_ENCHANTS, Tag.TAG_COMPOUND);
        ResourceLocation targetId = EnchantmentHelper.getEnchantmentId(enchantment);
        for(int idx = 0; idx < bonusEnchants.size(); ++idx) {
            CompoundTag bonusEnchant = bonusEnchants.getCompound(idx);
            ResourceLocation bonusId = EnchantmentHelper.getEnchantmentId(bonusEnchant);
            if (bonusId != null && bonusId.equals(targetId)) {
                return EnchantmentHelper.getEnchantmentLevel(bonusEnchant);
            }
        }
        return 0;
    }

    public static void setBonusEnchant(ItemStack stack, Enchantment enchantment, int level) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(BONUS_ENCHANTS, Tag.TAG_LIST)) {
            tag.put(BONUS_ENCHANTS, new ListTag());
        }
        ListTag bonusEnchants = tag.getList(BONUS_ENCHANTS, Tag.TAG_COMPOUND);
        ResourceLocation searchId = EnchantmentHelper.getEnchantmentId(enchantment);
        Iterator<Tag> iterator = bonusEnchants.iterator();
        while (iterator.hasNext()) { // clear dupes
            CompoundTag bonusEnchant = (CompoundTag) iterator.next();
            ResourceLocation id = EnchantmentHelper.getEnchantmentId(bonusEnchant);
            if (id != null && id.equals(searchId)) {
                iterator.remove();
            }
        }
        bonusEnchants.add(EnchantmentHelper.storeEnchantment(searchId, level));
    }



    public static void removeAllBonusEnchants(ItemStack stack) {
        if(!stack.hasTag()) return;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(BONUS_ENCHANTS, Tag.TAG_LIST)) {
            tag.put(BONUS_ENCHANTS, new ListTag());
        }
        ListTag bonusEnchants = tag.getList(BONUS_ENCHANTS, Tag.TAG_COMPOUND);
        Map<Enchantment, Integer> current = EnchantmentHelper.getEnchantments(stack);
        Map<Enchantment, Integer> bonus = EnchantmentHelper.deserializeEnchantments(bonusEnchants);
        bonus.keySet().forEach(ench -> {
            int bonusLevel = bonus.get(ench);
            if(bonusLevel <= 0) return;
            int currentLevel = current.getOrDefault(ench, 0);
            if(currentLevel - bonusLevel > 0){
                current.put(ench, currentLevel - bonusLevel);
            } else{
                current.remove(ench);
            }
        });
        EnchantmentHelper.setEnchantments(current,stack);
        tag.remove(BONUS_ENCHANTS);
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

    public static void addOneEffectRemoveOther(ServerPlayer player, MobEffect mobEffect, int amplifier) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        if (playerDuck.hasSetBonus(Items.EMERALD)) {
            player.addEffect(new MobEffectInstance(mobEffect, -1, amplifier, true, false));
            MobEffect old = playerDuck.beaconEffect();
            player.removeEffect(old);
            playerDuck.setBeaconEffect(mobEffect);
        } else {
            player.sendSystemMessage(Component.literal("Cannot apply beacon effects without emerald trim"));
        }
    }

    public static void giveTotemToDyingPlayer(LivingEntity living) {
        if (living instanceof ServerPlayer player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            EquipmentSlot usedSlot = null;
            boolean gaveTotem = false;
            for (EquipmentSlot slot : slots) {
                Item trim = slot == null ? playerDuck.regularSetBonus() : getTrimItem(player.level(), player.getItemBySlot(slot));
                int timer = playerDuck.abilityTimer(slot);
                if (trim == Items.EMERALD && timer > 0) {
                    if (!living.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
                        ((Player) living).drop(living.getItemInHand(InteractionHand.OFF_HAND),true);
                    }
                    living.setItemSlot(EquipmentSlot.OFFHAND,Items.TOTEM_OF_UNDYING.getDefaultInstance());
                    usedSlot = slot;
                    gaveTotem = true;
                    break;
                }
            }
            if(gaveTotem){
                playerDuck.setAbilityCooldown(usedSlot, 20 * 60 * 10);
                playerDuck.setAbilityTimer(usedSlot, 0);
                removeDeferredEvent(player.serverLevel(), de -> de.type() == DeferredEventTypes.SET_ABILITY_COOLDOWN);
            }
        }
    }

    public static boolean isUnableToTarget(LivingEntity attacker, LivingEntity target){
        if(attacker instanceof PiglinBruteDuck){
            return target.getUUID().equals(((OwnableEntity)attacker).getOwnerUUID());
        } else if (target instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            boolean amethyst = playerDuck.hasSetBonus(Items.AMETHYST_SHARD);
            if(amethyst && attacker instanceof WitchDuck witchDuck && isOwnerOrOwnerAlly(witchDuck, target)){
                return false;
            }
            return amethyst;
        }
        return false;
    }

    public static boolean isOwnerOrOwnerAlly(OwnableEntity mob, LivingEntity pTarget) {
        LivingEntity owner = mob.getOwner();
        return owner != null && (owner == pTarget || pTarget.isAlliedTo(owner));
    }

    public static boolean hasBonusSlots(Player player) {
        return ((PlayerDuck) player).hasSetBonus(Items.DIAMOND);
    }

    //Whats the SMP about?
    //
    //The SMP is based on different armor trims colors and their powers
    //
    // Iron: You take no fall damage and no drowning.
    // todo When activating combat mode, you have a punch that makes players fly 50 blocks in the air.
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
    //  When activating combat mode, you have a punch that withers a player for 5 seconds and resets every hit.
    //
    // todo: Copper: Copper acts as iron and you can craft anything iron-related using copper. When activating combat mode, you have the ability to summon 10 lighting strikes anywhere you look.
    //
    // Quartz: Auto smelt ores and food. When activating combat mode,
    //
    // todo you have a smoke cloud that appears around you and you become completely invisible to all players around you. It also makes every entity around you glow
    //
    //  Emerald: You can give yourself beacon effects any time.
    //
    //  When activating combat mode, you have a 15-second period where if you were to die, you have a totem that saves you.
    //
    // Red: Get Blast Resistance 4 on all armor.
    //  todo When activating combat mode, you have 3 homing arrows that explode and deal a lot of damage to players


    //bugs

    // CORE ISSUE
    //Cannot join a server with the mod, we recieve an error that says “Invalid Player Data”
    //
    //
    //Change cooldown to seconds instead of ticks
    //
    //
    //
    //Quartz
    //todo Quartz passive does not insta-smelt items in the player’s inventory. All ores and food should automatically be smelted when they are in the player’s inventory. (Should also work when unsmelted items are taken out of a chest)
    //
    //
    //todo Combat does not give you true invisibility. The player looks completely invisible on their screen, but not to other players. Please use the true-invis mod, and give the player the custom potion effect instead of whatever you’re doing currently.
    //trueinvis:true_invis
    //
    //Quartz Combat gives glowing with infinite duration, and should only last the same amount of time as invisibility.
    //
    //Emerald
    //Effects Balancing:
    //Haste 2
    //Strength 2
    //Hero of the Village 10
    //Jump Boost 2
    //Keep the rest the same
    //
    //When you take off the armor, you keep the effects. The effects should be removed when the set bonus is no longer active.
    //
    //Lapis
    //Passive is not functional. When putting on the armor, the player does not gain a level, nor do they seem to gain a level after 10 minutes.
    //
    //todo During combat activation, the player should not be able to remove their armor from themselves.
    //If they leave the game, it should automatically revert back to previous enchants.
    //This fixes situations where the player can get +1 enchant permanently, even if they remove the trim and start using another one.
    //
    //Redstone & Diamond
    //Redstone and Diamond Combat just give you the arrow. Instead, it should shoot out the arrow from the player. Refer back to the original mod and replicate the feature, please. One arrow shoots out every time you press the keybind, and the cooldown starts when the last arrow is released.
    //
    //Redstone TNT Arrows should not grief the world, only deal explosive damage to entities.
    //
    //
    //Also when you take off redstone or diamond and change the color of the armor, the enchantment that comes with the redstone trim and diamond trim still stays on the armor instead of taking it off. The player should not be able to keep the enchantments if they retrim their armor.
    //
    //
    //Diamond
    //
    //Iron
    //Combat ability lasts 2 seconds, and you can do it however many times you want. It also does a hit like a knockback hit instead of sending them in the air like it was supposed to be.
    //
    //To change it make it so they fly in the air and so the ability lasts a certain amount of time like 15 seconds instead of 2 seconds and doing it whenever you want
    //
    //Also, add the particles as it was in the original mod
    //
    //https://cdn.discordapp.com/attachments/1139119056478289971/1174104338675802224/Minecraft_2023.11.14_-_16.50.00.09.DVR_-_Trim.mp4?ex=656660e0&is=6553ebe0&hm=0012ba38aea4c4bfc74f417d6c9e0112307d58dadd8e7164146fe46a7e54133d&
    //
    //Gold
    //When brutes are brought into the overworld, they are turned into pigmen due to a vanilla mechanic. The pigmen do not have custom targeting, they behave like normal mobs.
    //
    //https://cdn.discordapp.com/attachments/1139119056478289971/1174106616761679912/Minecraft_2023.11.14_-_16.59.33.11.DVR_-_Trim.mp4?ex=656662ff&is=6553edff&hm=9c489a18585287fcbeb71e5db9cb3ca3d569dde08042f3ed1f5886eccc4d5979&
    //
    //Copper
    //This recipe needs to give a door, not iron bars
    //
    //
    //The thunder in combat does not deal any damage to players
    //
    //
    //Amethyst
    //Amethyst passive does not work, mobs still attack you instead of ignoring you.
    //https://cdn.discordapp.com/attachments/1139119056478289971/1174107959064805456/Minecraft_2023.11.14_-_17.04.40.12.DVR_-_Trim.mp4?ex=6566643f&is=6553ef3f&hm=0a94940709ec72e2948f8062ce51effc73d07aec554a0f4b7ec36c327ac0de26&
    //
    //For the witches, when you get too far away, they don’t TP to you like dogs.
    //When witches see other players they don't even try to heal you or anything, they just try attacking other players near you
    //
    //Please change witch attack to exactly how it was in the old mod
    //
    //Netherite
    //The withering effect does not work on combat ability

}