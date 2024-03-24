package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ModTags;
import com.marwinekk.armortrims.ducks.PlayerDuck;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuartzTrimAbilities {
    private static final int QUARTZ_ACTIVE_TICKS = 20 * 15;
    private static final int QUARTZ_COOLDOWN_TICKS = 20 * 70;
    public static final ArmorTrimAbility QUARTZ_TRIM_ABILITY = new ArmorTrimAbility(
            ArmorTrimAbilities.NULL,
            ArmorTrimAbilities.NULL,
            QuartzTrimAbilities::activateCombatAbility,
            ArmorTrimAbilities.NULL,
            QUARTZ_ACTIVE_TICKS,
            QUARTZ_COOLDOWN_TICKS)
            .setOnCombatAbilityActivated(QuartzTrimAbilities::onCombatAbilityActivated);

    private static void onCombatAbilityActivated(ServerPlayer player) {
        ArmorTrimAbilities.playActivationSound(player, SoundEvents.ELDER_GUARDIAN_DEATH);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
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
        ArmorTrimAbilities.getTrueInvis().ifPresent(trueInvis -> player.addEffect(new MobEffectInstance(trueInvis, 20 * 15, 10, false, false)));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 2, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 15, 2, false, false));

        return true;
    }

    public static void onInventoryCheck(Player player) {
        PlayerDuck playerDuck = (PlayerDuck) player;

        boolean cook = playerDuck.hasSetBonus(Items.QUARTZ);

        if (player.level().isClientSide || !cook) return;

        List<ItemStack> cooked = new ArrayList<>();

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.is(ModTags.QUARTZ_SMELTABLE)) {
                RecipeManager recipemanager = player.level().getRecipeManager();
                SimpleContainer simpleContainer = new SimpleContainer(stack);
                Optional<SmeltingRecipe> optional = recipemanager.getRecipeFor(RecipeType.SMELTING,simpleContainer, player.level());
                if (optional.isPresent()) {
                    while(stack.getCount() > 0){
                        SmeltingRecipe smeltingRecipe = optional.get();
                        ItemStack result = smeltingRecipe.assemble(simpleContainer, player.level().registryAccess());
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
}
