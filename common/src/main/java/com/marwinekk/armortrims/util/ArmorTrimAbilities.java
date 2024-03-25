package com.marwinekk.armortrims.util;

import com.google.common.base.Suppliers;
import com.marwinekk.armortrims.ArmorTrimsMod;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ArmorTrimAbilities {

    public static final Map<Item, ArmorTrimAbility> ARMOR_TRIM_REGISTRY = new HashMap<>();
    static final Consumer<ServerPlayer> NULL = player -> {
    };
    static final BiPredicate<ServerPlayer, EquipmentSlot> BI_NULL = (player, slot) -> true;

    public static final ArmorTrimAbility DUMMY = new ArmorTrimAbility(NULL, NULL, BI_NULL, NULL);
    public static final String ARMOR_TRIMS_TAG = "armor_trims";
    public static final UUID MODIFIER_UUID = UUID.fromString("097157ba-a99b-47c7-ac42-a360cbd74a73");
    public static final int MAX_LOCK_ON_DIST = 1024;


    static {
        ARMOR_TRIM_REGISTRY.put(Items.IRON_INGOT, IronTrimAbilities.IRON_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.LAPIS_LAZULI, LapisLazuliTrimAbilities.LAPIS_LAZULI_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.NETHERITE_INGOT, NetheriteTrimAbilities.NETHERITE_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.GOLD_INGOT, GoldTrimAbilities.GOLD_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.AMETHYST_SHARD, AmethystTrimAbilities.AMETHYST_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.DIAMOND, DiamondTrimAbilities.DIAMOND_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.REDSTONE, RedstoneTrimAbilities.REDSTONE_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.EMERALD, EmeraldTrimAbilities.EMERALD_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.QUARTZ, QuartzTrimAbilities.QUARTZ_TRIM_ABILITY);
        ARMOR_TRIM_REGISTRY.put(Items.COPPER_INGOT, CopperTrimAbilities.COPPER_TRIM_ABILITY);
    }

    static void playActivationSound(ServerPlayer player, SoundEvent soundEvent){
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, player.getSoundSource(), 1.0F, 1.0F);
    }

    static void tickParticles(ServerPlayer player, ParticleOptions particleOptions){
        tickParticles(player, particleOptions, null);
    }

    private static void tickParticles(ServerPlayer player, ParticleOptions particleType, @Nullable Integer color) {
        boolean makeParticle;
        if (player.isInvisible()) {
            makeParticle = player.getRandom().nextInt(15) == 0;
        } else {
            makeParticle = player.getRandom().nextBoolean();
        }

        if (makeParticle) {
            double xSpeed = color != null ? (color >> 16 & 255) / 255.0 : player.getDeltaMovement().x;
            double ySpeed = color != null ? (color >> 8 & 255) / 255.0 : player.getDeltaMovement().y;
            double zSpeed = color != null ? (color & 255) / 255.0 : player.getDeltaMovement().z;
            player.serverLevel().sendParticles(particleType, player.getRandomX(1), player.getRandomY(), player.getRandomZ(1), 0, xSpeed, ySpeed, zSpeed, 1);
        }
    }

    static void addAttributeModifier(ServerPlayer player, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        player.getAttribute(attribute).removePermanentModifier(MODIFIER_UUID);
        player.getAttribute(attribute).addPermanentModifier(new AttributeModifier(MODIFIER_UUID, "Armor Trims mod", amount, operation));
    }

    static void removeAttributeModifier(ServerPlayer player, Attribute attribute) {
        player.getAttribute(attribute).removePermanentModifier(MODIFIER_UUID);

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static final Supplier<MobEffect> TRUE_INVIS = Suppliers.memoize(() -> BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation("trueinvis:true_invis")));

    public static Optional<MobEffect> getTrueInvis(){
        return Optional.ofNullable(TRUE_INVIS.get());
    }

    public static boolean hasTrueInvis(LivingEntity entity){
        MobEffect trueInvis = TRUE_INVIS.get();
        return trueInvis != null && entity.hasEffect(trueInvis);
    }

    public static boolean messagePlayer(ServerPlayer player, Component message) {
        player.sendSystemMessage(message, true);
        return true;
    }

    static void applyBonusEnchantToArmor(ServerPlayer player, Enchantment enchantment, int level, Item trimItem) {
        applyToArmor(player, stack -> {
            if(ArmorTrimsMod.getTrimItem(player.serverLevel(), stack) == trimItem){
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                int currentLevel = enchantments.getOrDefault(enchantment, 0);
                if(currentLevel < level){
                    enchantments.put(enchantment, level);
                    EnchantmentHelper.setEnchantments(enchantments, stack);
                    ArmorTrimsMod.setBonusEnchant(stack, enchantment, level - currentLevel);
                }
            }
        });
    }

    public static void applyToArmor(ServerPlayer player, Consumer<ItemStack> applyToStack) {
        NonNullList<ItemStack> armors = player.getInventory().armor;
        for (ItemStack stack : armors) {
            applyToStack.accept(stack);
        }
    }

    public static void applyToAllSlots(ServerPlayer player, Consumer<ItemStack> applyToStack) {
        for(EquipmentSlot slot : EquipmentSlot.values()){
            applyToStack.accept(player.getItemBySlot(slot));
        }
    }

    static void removeBonusEnchantFromArmor(ServerPlayer player, Enchantment enchantment, Item item) {
        NonNullList<ItemStack> armors = player.getInventory().armor;
        //remove setbonus from other armors
        for (ItemStack stack : armors) {
            if (ArmorTrimsMod.getTrimItem(player.serverLevel(), stack) == item) {
                removeBonusEnchant(stack,enchantment);
            }
        }
        AbstractContainerMenu containerMenu = player.containerMenu;
        ItemStack carry = containerMenu.getCarried();
        if (ArmorTrimsMod.getTrimItem(player.serverLevel(),carry) == item) {
            removeBonusEnchant(carry,enchantment);
        }

        NonNullList<ItemStack> items = player.getInventory().items;
        for (ItemStack stack : items) {
            if (ArmorTrimsMod.getTrimItem(player.serverLevel(), stack) == item) {
                removeBonusEnchant(stack,enchantment);
            }
        }
    }

    public static void removeBonusEnchant(ItemStack stack, Enchantment enchantment) {
        int bonusLevel = ArmorTrimsMod.getBonusEnchant(stack, enchantment);
        if(bonusLevel <= 0) return;
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        int currentLevel = map.getOrDefault(enchantment, 0);
        if(currentLevel - bonusLevel > 0){
            map.put(enchantment, currentLevel - bonusLevel);
        } else{
            map.remove(enchantment);
        }
        EnchantmentHelper.setEnchantments(map,stack);
        ArmorTrimsMod.setBonusEnchant(stack, enchantment, 0);
    }

    static void toggleEnchantBoosts(ServerPlayer player, boolean boost) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            toggleEnchantBoost(stack, boost);
        }
    }

    public static void toggleEnchantBoost(ItemStack stack, boolean boost) {
        if (!stack.getEnchantmentTags().isEmpty() && ArmorTrimsMod.hasEnchantBoosts(stack) != boost) {
            ListTag enchantmentTags = stack.getEnchantmentTags();
            for (int idx = 0; idx < enchantmentTags.size(); ++idx) {
                CompoundTag tagCompound = enchantmentTags.getCompound(idx);
                EnchantmentHelper.setEnchantmentLevel(tagCompound, EnchantmentHelper.getEnchantmentLevel(tagCompound) + (boost ? 2 : -2)); // buffed to +/- 2 by request
            }
            ArmorTrimsMod.setEnchantBoosts(stack, boost);
        }
    }

    static Either<BlockHitResult, EntityHitResult> getHitResult(LivingEntity shooter) {
        BlockHitResult hitResult = (BlockHitResult) shooter.pick(MAX_LOCK_ON_DIST, 1.0F, false);
        Vec3 startVec = shooter.getEyePosition();
        double maxLockOnDistSqr = MAX_LOCK_ON_DIST * MAX_LOCK_ON_DIST;
        if (hitResult.getType() != HitResult.Type.MISS) {
            maxLockOnDistSqr = hitResult.getLocation().distanceToSqr(startVec);
        }
        Vec3 viewVector = shooter.getViewVector(1.0F).scale(MAX_LOCK_ON_DIST);
        Vec3 endVec = startVec.add(viewVector);
        AABB searchBox = shooter.getBoundingBox().expandTowards(viewVector).inflate(1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(shooter, startVec, endVec, searchBox, (e) -> !e.isSpectator() && e.isPickable(), maxLockOnDistSqr);
        if(entityHitResult != null) return Either.right(entityHitResult);
        return Either.left(hitResult);
    }
    public static void spawnParticlesInCircle(LevelAccessor world, ParticleOptions particleType, double x, double y, double z, double xzRadius, int amount) {
        int counter = 0;
        while (counter < amount) {
            double targetX = x + Math.cos((Mth.TWO_PI / amount) * counter) * xzRadius;
            double targetZ = z + Math.sin((Mth.TWO_PI / amount) * counter) * xzRadius;
            Vec3 step = new Vec3(targetX - x, 0, targetZ - z).normalize().scale(0.1);
            world.addParticle(particleType,
                    x,
                    y,
                    z,
                    step.x, 0.01, step.z);
            counter = counter + 1;
        }
    }

    public static boolean isLookingAt(LivingEntity viewer, LivingEntity target){
        Vec3 viewVector = viewer.getViewVector(1.0F).normalize();
        Vec3 distanceVec = new Vec3(target.getX() - viewer.getX(), target.getEyeY() - viewer.getEyeY(), target.getZ() - viewer.getZ());
        double distance = distanceVec.length();
        distanceVec = distanceVec.normalize();
        double dot = viewVector.dot(distanceVec);
        double threshold = 1.0D - 0.025D / distance;
        return dot > threshold && viewer.hasLineOfSight(target);
    }

    public static boolean isInFrontOf(LivingEntity viewer, LivingEntity target){
        Vec3 viewVector = viewer.getViewVector(1.0F);
        Vec3 distanceVec = target.position().vectorTo(viewer.position()).normalize();
        //distanceVec = new Vec3(distanceVec.x, 0.0D, distanceVec.z);
        return distanceVec.dot(viewVector) < 0.0D;
    }

}
