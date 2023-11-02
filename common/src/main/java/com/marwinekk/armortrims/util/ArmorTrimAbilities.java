package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.WitchDuck;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ArmorTrimAbilities {

    public static final Map<Item, ArmorTrimAbility> ARMOR_TRIM_REGISTRY = new HashMap<>();
    static final Consumer<ServerPlayer> NULL = player -> {};
    public static final ArmorTrimAbility DUMMY = new ArmorTrimAbility(NULL,NULL, NULL, NULL);

    static  {
        ARMOR_TRIM_REGISTRY.put(Items.IRON_INGOT,
                new ArmorTrimAbility(NULL, player -> player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 9, false, false)),
                        ArmorTrimAbilities::ironFist, NULL,20 * 5, 20));
        //give 1 xp level every 10 minutes
        ARMOR_TRIM_REGISTRY.put(Items.LAPIS_LAZULI,new ArmorTrimAbility(NULL, player -> {
            if (player.level().getGameTime() % (20 * 60 * 10) == 0) {
                player.giveExperienceLevels(1);
                player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1, 1);
                player.displayClientMessage(Component.literal("[§c!§f] Level §agained§f!"), true);
            }
        }, ArmorTrimAbilities::plus1ToAllEnchants, NULL));

        ARMOR_TRIM_REGISTRY.put(Items.NETHERITE_INGOT,new ArmorTrimAbility(NULL, player -> {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 9, true, false));
            if (player.isInLava()) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, true, false));
            }
        }, player -> messagePlayer(player,Component.translatable("Wither Touch Active")), NULL,100, 20 * 30));

        ARMOR_TRIM_REGISTRY.put(Items.GOLD_INGOT,new ArmorTrimAbility(player -> addAttributeModifier(
                player, Attributes.MAX_HEALTH,4, AttributeModifier.Operation.ADDITION),
                NULL, ArmorTrimAbilities::summonFriendlyPiglinBrutes,
                player -> removeAttributeModifier(player,Attributes.MAX_HEALTH)));

        ARMOR_TRIM_REGISTRY.put(Items.AMETHYST_SHARD,new ArmorTrimAbility(NULL,NULL, ArmorTrimAbilities::summonFriendlyWitch,NULL));
        ARMOR_TRIM_REGISTRY.put(Items.DIAMOND,new ArmorTrimAbility(ArmorTrimAbilities::applyUnbreakingOnAllArmor,NULL, ArmorTrimAbilities::givePower8Arrows,NULL));
        ARMOR_TRIM_REGISTRY.put(Items.REDSTONE,new ArmorTrimAbility(player -> applyEnchantToArmor(player,Enchantments.BLAST_PROTECTION,4),
                NULL, NULL, NULL));

        ARMOR_TRIM_REGISTRY.put(Items.EMERALD,new ArmorTrimAbility(NULL,NULL,
                player -> messagePlayer(player,Component.translatable("Totem Save Activated")),NULL,20 * 15, 20 * 30));

        ARMOR_TRIM_REGISTRY.put(Items.QUARTZ,new ArmorTrimAbility(NULL,NULL,ArmorTrimAbilities::smokeCloud,NULL,20 * 15,20 * 30));

        ARMOR_TRIM_REGISTRY.put(Items.COPPER_INGOT,new ArmorTrimAbility(ArmorTrimAbilities::awardCopperRecipes,NULL,NULL,ArmorTrimAbilities::revokeCopperRecipes));
    }

    static final UUID modifier_uuid = UUID.fromString("097157ba-a99b-47c7-ac42-a360cbd74a73");

    static void addAttributeModifier(ServerPlayer player, Attribute attribute, double amount, AttributeModifier.Operation operation) {
        player.getAttribute(attribute).removePermanentModifier(modifier_uuid);
        player.getAttribute(attribute).addPermanentModifier(new AttributeModifier(modifier_uuid, "Armor Trims mod",amount,operation));
    }

    static void removeAttributeModifier(ServerPlayer player,Attribute attribute) {
        player.getAttribute(attribute).removePermanentModifier(modifier_uuid);

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static void smokeCloud(ServerPlayer player) {
        Vec3 center = player.getPosition(0);
        int segments = 10;
        ServerLevel serverLevel = player.serverLevel();
        for (int i = 0; i < segments; i++) {
            double degrees = i * 360d/segments;
            double x = Math.cos(Math.toRadians(degrees));
            double z = Math.sin(Math.toRadians(degrees));
            for (int j = 0 ; j <16; j++) {
                serverLevel.sendParticles(ParticleTypes.POOF, center.x + x, center.y + j/8d, center.z + z, 0, 0, 0, 0, 0);
            }
        }

        List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(10), entity -> true);

        for (Entity entity : entities) {
            entity.setGlowingTag(true);
        }
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,20 * 15,0,false,false));
    }

    static void summonFriendlyPiglinBrutes(ServerPlayer player) {
        Level level = player.level();
        for (int i = 0; i < 5;i++) {

            PiglinBrute piglinBrute = EntityType.PIGLIN_BRUTE.spawn(
                    (ServerLevel)level, Items.PIGLIN_SPAWN_EGG.getDefaultInstance(),
                    player, player.blockPosition(), MobSpawnType.SPAWN_EGG, false, false);

            PiglinBruteDuck piglinBruteDuck = (PiglinBruteDuck) piglinBrute;
            piglinBruteDuck.setOwnerUUID(player.getUUID());
        }
    }

    public static void ironFist(ServerPlayer player) {
        messagePlayer(player,Component.translatable("Iron Fists Active"));
    }

    public static void messagePlayer(ServerPlayer player,Component message) {
        player.sendSystemMessage(message,true);
    }

    static void summonFriendlyWitch(ServerPlayer player) {
        Level level = player.level();
        for (int i = 0; i < 2;i++) {
            Witch witch = EntityType.WITCH.create(level);
            witch.moveTo(player.getPosition(0));
            WitchDuck witchDuck = (WitchDuck) witch;
            witchDuck.setOwnerUUID(player.getUUID());
            level.addFreshEntity(witch);
        }
    }

    static void applyEnchantToArmor(ServerPlayer player, Enchantment enchantment,int level) {
        NonNullList<ItemStack> armors = player.getInventory().armor;
        for (ItemStack stack : armors) {
            stack.enchant(enchantment,level);
        }
    }

    static void applyUnbreakingOnAllArmor(ServerPlayer player) {
        applyEnchantToArmor(player,Enchantments.UNBREAKING,4);
    }

    static void plus1ToAllEnchants(ServerPlayer player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.getEnchantmentTags().isEmpty()) {
                ListTag listTag = stack.getEnchantmentTags();
                for(int i = 0; i < listTag.size(); ++i) {
                    CompoundTag tagCompound = listTag.getCompound(i);
                    EnchantmentHelper.setEnchantmentLevel(tagCompound,EnchantmentHelper.getEnchantmentLevel(tagCompound) +1);
                }
            }
        }
    }

    static void givePower8Arrows(ServerPlayer player) {
        ItemStack stack = new ItemStack(Items.ARROW,3);
        stack.getOrCreateTag().putInt(ArmorTrimsMod.POWER_TAG,8);
        player.getInventory().add(stack);
    }

    static final ResourceLocation[] copper_recipes = new ResourceLocation[]{new ResourceLocation("armor_trims:activator_rail_cop"), new ResourceLocation("armor_trims:anvil_cop"), new ResourceLocation("armor_trims:blast_furnace_cop"), new ResourceLocation("armor_trims:bucket_cop"), new ResourceLocation("armor_trims:cauldron_cop"), new ResourceLocation("armor_trims:chain_cop"), new ResourceLocation("armor_trims:copper_cop"), new ResourceLocation("armor_trims:crossbow_cop"), new ResourceLocation("armor_trims:detector_rail"), new ResourceLocation("armor_trims:flint_and_steel_cop"), new ResourceLocation("armor_trims:heavt_weighted_pressure_plate_cop"), new ResourceLocation("armor_trims:hopper"), new ResourceLocation("armor_trims:iron_axe_cop"), new ResourceLocation("armor_trims:iron_trapdoor_cop"), new ResourceLocation("armor_trims:iron_axe_cop"), new ResourceLocation("armor_trims:iron_axe_cop_2"), new ResourceLocation("armor_trims:iron_bars"), new ResourceLocation("armor_trims:iron_boots_cop"), new ResourceLocation("armor_trims:iron_chestplate_cop"), new ResourceLocation("armor_trims:iron_door_cop"), new ResourceLocation("armor_trims:iron_door_cop_2"), new ResourceLocation("armor_trims:iron_helmet_cop"), new ResourceLocation("armor_trims:iron_hoe_cop"), new ResourceLocation("armor_trims:iron_hoe_cop_2"), new ResourceLocation("armor_trims:iron_leggings_cop"), new ResourceLocation("armor_trims:iron_nugget"), new ResourceLocation("armor_trims:iron_sword_cop"), new ResourceLocation("armor_trims:minecraft_cop"), new ResourceLocation("armor_trims:piston_cop"), new ResourceLocation("armor_trims:rail_cop"), new ResourceLocation("armor_trims:shears_cop"), new ResourceLocation("armor_trims:shield_cop"), new ResourceLocation("armor_trims:smithing_table_cop"), new ResourceLocation("armor_trims:stonecutter_cop"), new ResourceLocation("armor_trims:tripwire_hook")};


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
