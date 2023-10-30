package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ducks.PiglinBruteDuck;
import com.marwinekk.armortrims.ducks.WitchDuck;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ArmorTrimAbilities {

    public static final Map<Item, ArmorTrimAbility> ARMOR_TRIM_REGISTRY = new HashMap<>();
    static final Consumer<ServerPlayer> NULL = player -> {};
    public static final ArmorTrimAbility DUMMY = new ArmorTrimAbility(NULL,NULL, NULL, NULL);

    static  {
        ARMOR_TRIM_REGISTRY.put(Items.IRON_INGOT,
                new ArmorTrimAbility(NULL, player -> player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 40, 9, false, false)), NULL, NULL));
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
        }, NULL, NULL));
        ARMOR_TRIM_REGISTRY.put(Items.GOLD_INGOT,new ArmorTrimAbility(player -> addAttributeModifier(
                player, Attributes.MAX_HEALTH,4, AttributeModifier.Operation.ADDITION),
                NULL, ArmorTrimAbilities::summonFriendlyPiglinBrutes,
                player -> removeAttributeModifier(player,Attributes.MAX_HEALTH)));
        ARMOR_TRIM_REGISTRY.put(Items.AMETHYST_SHARD,new ArmorTrimAbility(NULL,NULL, ArmorTrimAbilities::summonFriendlyWitch,NULL));
        ARMOR_TRIM_REGISTRY.put(Items.DIAMOND,new ArmorTrimAbility(ArmorTrimAbilities::applyUnbreakingOnAllArmor,NULL, ArmorTrimAbilities::givePower8Arrows,NULL));
        ARMOR_TRIM_REGISTRY.put(Items.REDSTONE,new ArmorTrimAbility(player -> applyEnchantToArmor(player,Enchantments.BLAST_PROTECTION,4),
                NULL, NULL, NULL));
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

    static void summonFriendlyPiglinBrutes(ServerPlayer player) {
        Level level = player.level();
        for (int i = 0; i < 5;i++) {
            PiglinBrute piglinBrute = EntityType.PIGLIN_BRUTE.create(level);
            PiglinBruteDuck piglinBruteDuck = (PiglinBruteDuck) piglinBrute;
            piglinBruteDuck.setOwnerUUID(player.getUUID());
            level.addFreshEntity(piglinBrute);
        }
    }

    static void summonFriendlyWitch(ServerPlayer player) {
        Level level = player.level();
        for (int i = 0; i < 5;i++) {
            Witch witch = EntityType.WITCH.create(level);
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
}
