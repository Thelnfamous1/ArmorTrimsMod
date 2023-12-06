package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.marwinekk.armortrims.ducks.WitchDuck;
import com.marwinekk.armortrims.entity.BlockBreakerArrow;
import com.marwinekk.armortrims.entity.TNTArrowEntity;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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

    static {
        ARMOR_TRIM_REGISTRY.put(Items.IRON_INGOT,
                new ArmorTrimAbility(NULL, player -> player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 9, false, false)),
                        ArmorTrimAbilities::ironFist, NULL, 15 * 20, 60 * 20));
        //give 1 xp level every 10 minutes
        ARMOR_TRIM_REGISTRY.put(Items.LAPIS_LAZULI, new ArmorTrimAbility(NULL, player -> {
            if (player.level().getGameTime() % (20 * 20 * 10) == 0) {
                player.giveExperienceLevels(1);
                player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1, 1);
                player.displayClientMessage(Component.literal("[§c!§f] Level §agained§f!"), true);
            }
        }, ArmorTrimAbilities::plus1ToAllEnchants, NULL, 20 * 20, 20 * 50));

        ARMOR_TRIM_REGISTRY.put(Items.NETHERITE_INGOT, new ArmorTrimAbility(NULL, player -> {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 9, true, false));
            if (player.isInLava()) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, true, false));
            }
        }, (player, slot) -> messagePlayer(player, Component.translatable("Wither Touch Active")), NULL, 20 * 10, 20 * 45));

        ARMOR_TRIM_REGISTRY.put(Items.GOLD_INGOT, new ArmorTrimAbility(player -> addAttributeModifier(
                player, Attributes.MAX_HEALTH, 4, AttributeModifier.Operation.ADDITION),
                NULL, ArmorTrimAbilities::summonFriendlyPiglinBrutes,
                player -> removeAttributeModifier(player, Attributes.MAX_HEALTH), 0, 20 * 50));

        ARMOR_TRIM_REGISTRY.put(Items.AMETHYST_SHARD, new ArmorTrimAbility(NULL, NULL, ArmorTrimAbilities::summonFriendlyWitch, NULL, 0, 20 * 60));
        ARMOR_TRIM_REGISTRY.put(Items.DIAMOND, new ArmorTrimAbility(ArmorTrimAbilities::applyUnbreakingOnAllArmor, NULL, ArmorTrimAbilities::givePower8Arrows, player ->
                removeEnchantFromArmor(player,Enchantments.UNBREAKING,Items.DIAMOND)));
        ARMOR_TRIM_REGISTRY.put(Items.REDSTONE, new ArmorTrimAbility(player -> applyEnchantToArmor(player, Enchantments.BLAST_PROTECTION, 4),
                NULL, ArmorTrimAbilities::summonHomingArrows, player -> removeEnchantFromArmor(player, Enchantments.BLAST_PROTECTION, Items.REDSTONE),0,0));

        ARMOR_TRIM_REGISTRY.put(Items.EMERALD, new ArmorTrimAbility(NULL, NULL,
                (player, slot) -> messagePlayer(player, Component.translatable("Totem Save Activated")), ArmorTrimAbilities::removeEmeraldEffect, 20 * 15, 20 * 60 * 2));

        ARMOR_TRIM_REGISTRY.put(Items.QUARTZ, new ArmorTrimAbility(NULL, NULL, ArmorTrimAbilities::smokeCloud, NULL, 20 * 15, 20 * 45));

        ARMOR_TRIM_REGISTRY.put(Items.COPPER_INGOT, new ArmorTrimAbility(ArmorTrimAbilities::awardCopperRecipes, NULL, ArmorTrimAbilities::lightningStrike, ArmorTrimAbilities::revokeCopperRecipes, 0, 0));
    }

    static final UUID modifier_uuid = UUID.fromString("097157ba-a99b-47c7-ac42-a360cbd74a73");

    static void addAttributeModifier(ServerPlayer player, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        player.getAttribute(attribute).removePermanentModifier(modifier_uuid);
        player.getAttribute(attribute).addPermanentModifier(new AttributeModifier(modifier_uuid, "Armor Trims mod", amount, operation));
    }

    static void removeEmeraldEffect(ServerPlayer player) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        MobEffect old = playerDuck.beaconEffect();
        player.removeEffect(old);
    }

    static boolean lightningStrike(ServerPlayer player, EquipmentSlot slot) {
        PlayerDuck playerDuck = (PlayerDuck) player;
        int strikes = playerDuck.lightningStrikesLeft();
        if (strikes <= 0) {
            strikes = 10;
        }
        playerDuck.setLightningStrikesLeft(strikes - 1);
        if (strikes == 1) {
            playerDuck.setAbilityCooldown(slot, 20 * 30);
        }

        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
        if (lightningbolt != null) {
            HitResult pick = player.pick(20, 0, false);
            Vec3 pos = pick.getLocation();
            lightningbolt.moveTo(pos);
            lightningbolt.setCause(player);
            lightningbolt.addTag(ARMOR_TRIMS_TAG);
            player.level().addFreshEntity(lightningbolt);
            lightningbolt.playSound(SoundEvents.TRIDENT_THUNDER, 5, 1);
        }
        return false;
    }

    static void removeAttributeModifier(ServerPlayer player, Attribute attribute) {
        player.getAttribute(attribute).removePermanentModifier(modifier_uuid);

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static MobEffect true_invis = BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation("trueinvis:true_invis"));

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
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING,20 * 12,0,false,false));
        }
        player.addEffect(new MobEffectInstance(true_invis, 12 * 20, 10, false, false));
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
            level.addFreshEntity(witch);
        }
        return true;
    }

    static void applyEnchantToArmor(ServerPlayer player, Enchantment enchantment, int level) {
        NonNullList<ItemStack> armors = player.getInventory().armor;
        for (ItemStack stack : armors) {
            stack.enchant(enchantment, level);
        }
    }

    static void removeEnchantFromArmor(ServerPlayer player, Enchantment enchantment, Item item) {
        NonNullList<ItemStack> armors = player.getInventory().armor;
        //remove setbonus from other armors
        for (ItemStack stack : armors) {
            if (ArmorTrimsMod.getTrimItem(player.serverLevel(), stack) == item) {
                removeEnchant(stack,enchantment);
            }
        }
        boolean found4thPiece = false;
        AbstractContainerMenu containerMenu = player.containerMenu;
        ItemStack carry = containerMenu.getCarried();
        if (ArmorTrimsMod.getTrimItem(player.serverLevel(),carry) == item) {
            found4thPiece = true;
            removeEnchant(carry,enchantment);
        }

        if (!found4thPiece) {
            NonNullList<ItemStack> items = player.getInventory().items;
            for (ItemStack stack : items) {
                if (ArmorTrimsMod.getTrimItem(player.serverLevel(), stack) == item) {
                    removeEnchant(stack,enchantment);
                    break;
                }
            }
        }
    }

    public static void removeEnchant(ItemStack stack,Enchantment enchantment) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        map.remove(enchantment);
        EnchantmentHelper.setEnchantments(map,stack);
    }

    static void applyUnbreakingOnAllArmor(ServerPlayer player) {
        applyEnchantToArmor(player, Enchantments.UNBREAKING, 4);
    }

    static boolean plus1ToAllEnchants(ServerPlayer player, EquipmentSlot slot1) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.getEnchantmentTags().isEmpty()) {
                ListTag listTag = stack.getEnchantmentTags();
                for (int i = 0; i < listTag.size(); ++i) {
                    CompoundTag tagCompound = listTag.getCompound(i);
                    EnchantmentHelper.setEnchantmentLevel(tagCompound, EnchantmentHelper.getEnchantmentLevel(tagCompound) + 1);
                }
            }
        }
        return true;
    }

    static boolean givePower8Arrows(ServerPlayer player, EquipmentSlot slot) {
        BlockBreakerArrow arrow = new BlockBreakerArrow(player.level(),player);
        int builtInPowerLvl = 7;
        arrow.setBaseDamage(arrow.getBaseDamage() + builtInPowerLvl * 0.5 + 0.5);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0);
        player.level().addFreshEntity(arrow);
        PlayerDuck playerDuck = (PlayerDuck) player;
        int arrows = playerDuck.redstoneArrowsLeft();
        boolean applyCooldown  = false;
        if (arrows <=0) {
            playerDuck.setRedstoneArrowsLeft(2); // 3 arrows total
        } else {
            arrows--;
            if (arrows <= 0) {
                playerDuck.setAbilityCooldown(slot,45 * 20);
            }
            playerDuck.setRedstoneArrowsLeft(arrows);
        }
        return applyCooldown;
    }

    static boolean summonHomingArrows(ServerPlayer player, EquipmentSlot slot) {
        TNTArrowEntity tntArrowEntity = new TNTArrowEntity(player.level(),player);
        tntArrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 0);
        lockOn(player, tntArrowEntity, 1024);
        player.level().addFreshEntity(tntArrowEntity);
        PlayerDuck playerDuck = (PlayerDuck) player;
        int arrows = playerDuck.redstoneArrowsLeft();
        if (arrows <=0) {
            playerDuck.setRedstoneArrowsLeft(3); // 4 arrows total
        } else {
            arrows--;
            if (arrows <= 0) {
                playerDuck.setAbilityCooldown(slot,45 * 20);
            }
            playerDuck.setRedstoneArrowsLeft(arrows);
        }
        return false;
    }

    private static void lockOn(LivingEntity shooter, TNTArrowEntity homingArrow, int maxLockOnDist) {
        HitResult hitResult = shooter.pick(maxLockOnDist, 0.0F, false);
        Vec3 startVec = shooter.getEyePosition();
        double maxLockOnDistSqr = maxLockOnDist * maxLockOnDist;
        if (hitResult.getType() != HitResult.Type.MISS) {
            maxLockOnDistSqr = hitResult.getLocation().distanceToSqr(startVec);
        }
        Vec3 viewVector = shooter.getViewVector(1.0F).scale(maxLockOnDist);
        Vec3 endVec = startVec.add(viewVector);
        AABB searchBox = shooter.getBoundingBox().expandTowards(viewVector).inflate(1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(shooter, startVec, endVec, searchBox, (e) -> !e.isSpectator() && e.isPickable(), maxLockOnDistSqr);
        if(entityHitResult != null){
            homingArrow.setHomingTarget(entityHitResult.getEntity());
        }
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
