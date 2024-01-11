package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.ducks.WitchDuck;
import com.marwinekk.armortrims.entity.BlockBreakerArrow;
import com.marwinekk.armortrims.entity.TNTArrowEntity;
import com.marwinekk.armortrims.platform.Services;
import com.marwinekk.armortrims.world.deferredevent.DespawnLater;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        ARMOR_TRIM_REGISTRY.put(Items.IRON_INGOT,
                new ArmorTrimAbility(NULL, player -> player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 9, false, false)),
                        ArmorTrimAbilities::ironFist, NULL, 15 * 20, 30 * 20)
                        .setOnCombatAbilityActive(player -> tickParticles(player, ParticleTypes.WAX_OFF)));
        //give 1 xp level every 10 minutes
        ARMOR_TRIM_REGISTRY.put(Items.LAPIS_LAZULI, new ArmorTrimAbility(NULL, player -> {
            if (player.level().getGameTime() % (20 * 20 * 10) == 0) {
                player.giveExperienceLevels(1);
                player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1, 1);
                player.displayClientMessage(Component.literal("[§c!§f] Level §agained§f!"), true);
            }
        }, ArmorTrimAbilities::plus1ToAllEnchants, NULL, 20 * 20, 20 * 50)
                .setOnCombatAbilityActive(player -> tickParticles(player, ParticleTypes.ENCHANT))
                .setOnCombatAbilityInactive(ArmorTrimAbilities::minus1ToAllEnchants));

        ARMOR_TRIM_REGISTRY.put(Items.NETHERITE_INGOT, new ArmorTrimAbility(NULL, player -> {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 9, true, false));
            if (player.isInLava()) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, true, false));
            }
        }, (player, slot) -> messagePlayer(player, Component.translatable("Wither Touch Active")), NULL, 20 * 10, 20 * 45)
                .setOnCombatAbilityActive(player -> tickParticles(player, ParticleTypes.AMBIENT_ENTITY_EFFECT, ChatFormatting.BLACK.getColor())));

        ARMOR_TRIM_REGISTRY.put(Items.GOLD_INGOT, new ArmorTrimAbility(player -> addAttributeModifier(
                player, Attributes.MAX_HEALTH, 4, AttributeModifier.Operation.ADDITION),
                NULL, ArmorTrimAbilities::summonFriendlyPiglinBrutes,
                player -> removeAttributeModifier(player, Attributes.MAX_HEALTH), 0, 20 * 50));

        ARMOR_TRIM_REGISTRY.put(Items.AMETHYST_SHARD, new ArmorTrimAbility(NULL, NULL, ArmorTrimAbilities::summonFriendlyWitch, NULL, 0, 20 * 60));
        ARMOR_TRIM_REGISTRY.put(Items.DIAMOND, new ArmorTrimAbility(
                player -> {
                    Services.PLATFORM.addExtraInventorySlots(player);
                    applyBonusEnchantToArmor(player, Enchantments.UNBREAKING, 4, Items.DIAMOND);
                },
                NULL, ArmorTrimAbilities::givePower8Arrows,
                player -> {
                    Services.PLATFORM.removeExtraInventorySlots(player);
                    removeBonusEnchantFromArmor(player,Enchantments.UNBREAKING,Items.DIAMOND);
                }).runEveryEquip());
        ARMOR_TRIM_REGISTRY.put(Items.REDSTONE, new ArmorTrimAbility(player -> applyBonusEnchantToArmor(player, Enchantments.BLAST_PROTECTION, 4, Items.REDSTONE),
                NULL, ArmorTrimAbilities::summonHomingArrows, player -> removeBonusEnchantFromArmor(player, Enchantments.BLAST_PROTECTION, Items.REDSTONE),0,0).runEveryEquip());

        ARMOR_TRIM_REGISTRY.put(Items.EMERALD, new ArmorTrimAbility(NULL, NULL,
                (player, slot) -> messagePlayer(player, Component.translatable("Totem Save Activated")), ArmorTrimAbilities::removeEmeraldEffect, 20 * 15, 20 * 60 * 2)
                .setOnCombatAbilityActive(player -> tickParticles(player, ParticleTypes.EGG_CRACK)));

        ARMOR_TRIM_REGISTRY.put(Items.QUARTZ, new ArmorTrimAbility(NULL, NULL, ArmorTrimAbilities::smokeCloud, NULL, 20 * 15, 20 * 45));

        ARMOR_TRIM_REGISTRY.put(Items.COPPER_INGOT, new ArmorTrimAbility(ArmorTrimAbilities::awardCopperRecipes, NULL, ArmorTrimAbilities::lightningStrike, ArmorTrimAbilities::revokeCopperRecipes, 0, 0));
    }

    private static void tickParticles(ServerPlayer player, ParticleOptions particleOptions){
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

    static void removeEmeraldEffect(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        MobEffect old = playerDuck.beaconEffect();
        player.removeEffect(old);
    }

    static boolean lightningStrike(ServerPlayer player, EquipmentSlot slot) {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
        if (lightningbolt != null) {
            Either<BlockHitResult, EntityHitResult> hitResult = getHitResult(player);
            if(hitResult.left().isPresent() && hitResult.left().get().getType() == HitResult.Type.MISS){
                return false;
            }
            Vec3 pos = hitResult.map(HitResult::getLocation, HitResult::getLocation);
            lightningbolt.moveTo(pos);
            lightningbolt.setCause(player);
            lightningbolt.addTag(ARMOR_TRIMS_TAG);
            player.level().addFreshEntity(lightningbolt);
            lightningbolt.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 5, 1);

            PlayerDuck playerDuck = (PlayerDuck) player;
            int strikes = playerDuck.lightningStrikesLeft();
            if (strikes <= 0) {
                strikes = 10;
            }
            playerDuck.setLightningStrikesLeft(strikes - 1);
            if (strikes == 1) {
                playerDuck.setAbilityCooldown(slot, 20 * 30);
            }
        }
        return false;
    }

    static void removeAttributeModifier(ServerPlayer player, Attribute attribute) {
        player.getAttribute(attribute).removePermanentModifier(MODIFIER_UUID);

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static MobEffect true_invis = BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation("trueinvis:true_invis"));

    public static Optional<MobEffect> getTrueInvis(){
        return Optional.ofNullable(true_invis);
    }

    public static boolean hasTrueInvis(LivingEntity entity){
        return true_invis != null && entity.hasEffect(true_invis);
    }

    public static boolean smokeCloud(ServerPlayer player, EquipmentSlot slot) {
        Vec3 center = player.getPosition(0);
        int segments = 24;
        double r = 2;
        ServerLevel serverLevel = player.serverLevel();
        for (int i = 0; i < segments; i++) {
            double degrees = i * 360d / segments;
            double x = r * Math.cos(Math.toRadians(degrees));
            double z = r * Math.sin(Math.toRadians(degrees));
            for (int j = 0; j < 32; j++) {
                serverLevel.sendParticles(ParticleTypes.POOF, center.x + x, center.y + j / 8d, center.z + z, 0, 0, 0, 0, 0);
            }
        }

        List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(10), entity -> true);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living)
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING,20 * 15,0,false,false));
        }
        getTrueInvis().ifPresent(trueInvis -> player.addEffect(new MobEffectInstance(trueInvis, 20 * 15, 10, false, false)));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 2, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 15, 2, false, false));

        return true;
    }

    static boolean summonFriendlyPiglinBrutes(ServerPlayer player, EquipmentSlot slot) {
        Level level = player.level();
        for (int i = 0; i < 5; i++) {

            PiglinBrute piglinBrute = EntityType.PIGLIN_BRUTE.spawn(
                    (ServerLevel) level, Items.PIGLIN_SPAWN_EGG.getDefaultInstance(),
                    player, player.blockPosition(), MobSpawnType.SPAWN_EGG, false, false);

            piglinBrute.setImmuneToZombification(true);
            PiglinBruteDuck piglinBruteDuck = (PiglinBruteDuck) piglinBrute;
            piglinBruteDuck.setOwnerUUID(player.getUUID());
            DespawnLater despawnLater = new DespawnLater(piglinBrute);
            despawnLater.setTimer(2 * 60 * 20);
            ArmorTrimsMod.addDeferredEvent(player.serverLevel(),despawnLater);
        }
        return true;
    }

    public static boolean ironFist(ServerPlayer player, EquipmentSlot slot) {
        messagePlayer(player, Component.translatable("Iron Fists Active"));
        return true;
    }

    public static boolean messagePlayer(ServerPlayer player, Component message) {
        player.sendSystemMessage(message, true);
        return true;
    }

    static boolean summonFriendlyWitch(ServerPlayer player, EquipmentSlot slot) {
        Level level = player.level();
        for (int i = 0; i < 2; i++) {
            Witch witch = EntityType.WITCH.create(level);
            witch.moveTo(player.getPosition(0));
            WitchDuck witchDuck = (WitchDuck) witch;
            witchDuck.setOwnerUUID(player.getUUID());
            // survives 5 critical hits from sharp V diamond sword
            witch.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0);
            witch.setHealth(100.0F);
            level.addFreshEntity(witch);
        }
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

    static boolean plus1ToAllEnchants(ServerPlayer player, EquipmentSlot slot1) {
        toggleEnchantBoosts(player, true);
        ArmorTrimsMod.lockAllSlots(player);
        return true;
    }

    private static void toggleEnchantBoosts(ServerPlayer player, boolean boost) {
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
                EnchantmentHelper.setEnchantmentLevel(tagCompound, EnchantmentHelper.getEnchantmentLevel(tagCompound) + (boost ? 1 : -1));
            }
            ArmorTrimsMod.setEnchantBoosts(stack, boost);
        }
    }

    private static void minus1ToAllEnchants(ServerPlayer player) {
        toggleEnchantBoosts(player, false);
        ArmorTrimsMod.unlockAllSlots(player);
    }

    static boolean givePower8Arrows(ServerPlayer player, EquipmentSlot slot) {
        BlockBreakerArrow arrow = new BlockBreakerArrow(player.level(),player);
        int builtInPowerLvl = 7;
        arrow.setBaseDamage(arrow.getBaseDamage() + builtInPowerLvl * 0.5 + 0.5);
        arrow.setPierceLevel((byte) (arrow.getPierceLevel() + 1));
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0);
        player.level().addFreshEntity(arrow);
        PlayerDuck playerDuck = (PlayerDuck) player;
        int arrows = playerDuck.diamondArrowsLeft();
        if (arrows <=0) {
            playerDuck.setDiamondArrowsLeft(4); // 5 arrows total, we just fired 1
        } else {
            arrows--;
            if (arrows <= 0) {
                playerDuck.setAbilityCooldown(slot,45 * 20);
            }
            playerDuck.setDiamondArrowsLeft(arrows);
        }
        return false;
    }

    static boolean summonHomingArrows(ServerPlayer player, EquipmentSlot slot) {
        TNTArrowEntity tntArrowEntity = new TNTArrowEntity(player.level(),player);
        tntArrowEntity.setExplosionRadius(1.5F);
        tntArrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0);
        lockOn(player, tntArrowEntity);
        player.level().addFreshEntity(tntArrowEntity);
        PlayerDuck playerDuck = (PlayerDuck) player;
        int arrows = playerDuck.redstoneArrowsLeft();
        if (arrows <=0) {
            playerDuck.setRedstoneArrowsLeft(2); // 3 arrows total, we just fired 1
        } else {
            arrows--;
            if (arrows <= 0) {
                playerDuck.setAbilityCooldown(slot,45 * 20);
            }
            playerDuck.setRedstoneArrowsLeft(arrows);
        }
        return false;
    }

    public static void lockOn(LivingEntity shooter, TNTArrowEntity homingArrow) {
        getHitResult(shooter).ifRight(ehr -> homingArrow.setHomingTarget(ehr.getEntity()));
    }

    private static Either<BlockHitResult, EntityHitResult> getHitResult(LivingEntity shooter) {
        BlockHitResult hitResult = (BlockHitResult) shooter.pick(MAX_LOCK_ON_DIST, 0.0F, false);
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

    static final ResourceLocation[] copper_recipes = new ResourceLocation[]{new ResourceLocation("armor_trims:activator_rail_cop"), new ResourceLocation("armor_trims:anvil_cop"), new ResourceLocation("armor_trims:blast_furnace_cop"), new ResourceLocation("armor_trims:bucket_cop"), new ResourceLocation("armor_trims:cauldron_cop"), new ResourceLocation("armor_trims:chain_cop"), new ResourceLocation("armor_trims:copper_cop"), new ResourceLocation("armor_trims:crossbow_cop"), new ResourceLocation("armor_trims:detector_rail"), new ResourceLocation("armor_trims:flint_and_steel_cop"), new ResourceLocation("armor_trims:heavt_weighted_pressure_plate_cop"), new ResourceLocation("armor_trims:hopper"), new ResourceLocation("armor_trims:iron_axe_cop"), new ResourceLocation("armor_trims:iron_trapdoor_cop"), new ResourceLocation("armor_trims:iron_axe_cop"), new ResourceLocation("armor_trims:iron_axe_cop_2"), new ResourceLocation("armor_trims:iron_bars"), new ResourceLocation("armor_trims:iron_boots_cop"), new ResourceLocation("armor_trims:iron_chestplate_cop"), new ResourceLocation("armor_trims:iron_door_cop"), new ResourceLocation("armor_trims:iron_door_cop_2"), new ResourceLocation("armor_trims:iron_helmet_cop"), new ResourceLocation("armor_trims:iron_hoe_cop"), new ResourceLocation("armor_trims:iron_hoe_cop_2"), new ResourceLocation("armor_trims:iron_leggings_cop"), new ResourceLocation("armor_trims:iron_nugget"), new ResourceLocation("armor_trims:iron_sword_cop"), new ResourceLocation("armor_trims:minecart_cop"), new ResourceLocation("armor_trims:piston_cop"), new ResourceLocation("armor_trims:rail_cop"), new ResourceLocation("armor_trims:shears_cop"), new ResourceLocation("armor_trims:shield_cop"), new ResourceLocation("armor_trims:smithing_table_cop"), new ResourceLocation("armor_trims:stonecutter_cop"), new ResourceLocation("armor_trims:tripwire_hook")};


    static void awardCopperRecipes(ServerPlayer player) {
        player.awardRecipesByKey(copper_recipes);
    }

    static void revokeCopperRecipes(ServerPlayer player) {
        MinecraftServer server = player.server;
        player.resetRecipes(Arrays.stream(copper_recipes).map(resourceLocation -> server.getRecipeManager().byKey(resourceLocation))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList()));
    }
}
