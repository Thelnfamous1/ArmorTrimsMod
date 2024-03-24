package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CopperTrimAbilities {
    public static final int COPPER_ABILITY_USES = 7;
    public static final int JUMP_COOLDOWN_TICKS = 5 * 20;
    private static final int COPPER_ACTIVE_TICKS = 0;
    private static final int COPPER_COOLDOWN_TICKS = 20 * 30;
    public static final ArmorTrimAbility COPPER_TRIM_ABILITY = new ArmorTrimAbility(
            CopperTrimAbilities::onEquip,
            ArmorTrimAbilities.NULL,
            CopperTrimAbilities::activateCombatAbility,
            CopperTrimAbilities::onRemove,
            COPPER_ACTIVE_TICKS,
            0);

    private static void onEquip(ServerPlayer player) {
        player.awardRecipesByKey(ArmorTrimAbilities.copper_recipes);
    }

    private static boolean activateCombatAbility(ServerPlayer player, EquipmentSlot slot) {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
        if (lightningbolt != null) {
            Either<BlockHitResult, EntityHitResult> hitResult = ArmorTrimAbilities.getHitResult(player);
            if(hitResult.left().isPresent() && hitResult.left().get().getType() == HitResult.Type.MISS){
                return false;
            }
            Vec3 pos = hitResult.map(HitResult::getLocation, HitResult::getLocation);
            lightningbolt.moveTo(pos);
            lightningbolt.setCause(player);
            lightningbolt.addTag(ArmorTrimAbilities.ARMOR_TRIMS_TAG);
            player.level().addFreshEntity(lightningbolt);
            lightningbolt.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 5, 1);

            PlayerDuck playerDuck = (PlayerDuck) player;
            int strikes = playerDuck.lightningStrikesLeft();
            if (strikes <= 0) {
                strikes = COPPER_ABILITY_USES;
            }
            playerDuck.setLightningStrikesLeft(strikes - 1);
            if (strikes == 1) {
                playerDuck.setAbilityCooldown(slot, COPPER_COOLDOWN_TICKS);
            }
        }
        return false;
    }

    private static void onRemove(ServerPlayer player) {
        MinecraftServer server = player.server;
        player.resetRecipes(Arrays.stream(ArmorTrimAbilities.copper_recipes).map(resourceLocation -> server.getRecipeManager().byKey(resourceLocation))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList()));
    }

    public static boolean canPreventDamage(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity instanceof Player player) {
            PlayerDuck playerDuck = (PlayerDuck) player;
            if(source.is(DamageTypeTags.IS_LIGHTNING)){
                return isImmuneToLightningDamage(playerDuck);
            } else if(source.is(DamageTypeTags.IS_FALL) && playerDuck.armorTrimsMod$isDoubleJumping()){
                return canDoubleJump(playerDuck);
            }
        }
        return false;
    }

    private static boolean isImmuneToLightningDamage(PlayerDuck playerDuck) {
        return playerDuck.hasSetBonus(Items.COPPER_INGOT);
    }

    public static boolean canDoubleJump(PlayerDuck duck){
        return duck.hasSetBonus(Items.COPPER_INGOT);
    }

    public static void createDoubleJumpEffect(Player player) {
        createDoubleJumpEffect(player, player);
    }

    public static void createDoubleJumpEffect(Player localPlayer, Player effectPlayer) {
        Level world = localPlayer.getCommandSenderWorld();
        world.playSound(localPlayer, effectPlayer.blockPosition(), SoundEvents.TURTLE_SHAMBLE, SoundSource.PLAYERS, 0.4f, 1);

        for(int i = 0; i < 5; ++i) {
            double xSpeed = world.random.nextGaussian() * 0.02D;
            double ySpeed = world.random.nextGaussian() * 0.02D;
            double zSpeed = world.random.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.CLOUD, effectPlayer.getRandomX(1.0D), effectPlayer.getY(), effectPlayer.getRandomZ(1.0D), xSpeed, ySpeed, zSpeed);
        }
    }

    public static void setDoubleJumping(PlayerDuck playerDuck, boolean doubleJumping) {
        playerDuck.armorTrimsMod$setDoubleJumping(doubleJumping);
    }
}
