package com.marwinekk.armortrims.util;

import com.marwinekk.armortrims.ducks.PlayerDuck;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
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
    private static final ResourceLocation[] COPPER_RECIPES = new ResourceLocation[]{
            new ResourceLocation("armor_trims:activator_rail_cop"),
            new ResourceLocation("armor_trims:anvil_cop"),
            new ResourceLocation("armor_trims:blast_furnace_cop"),
            new ResourceLocation("armor_trims:bucket_cop"),
            new ResourceLocation("armor_trims:cauldron_cop"),
            new ResourceLocation("armor_trims:chain_cop"),
            new ResourceLocation("armor_trims:copper_cop"),
            new ResourceLocation("armor_trims:crossbow_cop"),
            new ResourceLocation("armor_trims:detector_rail"),
            new ResourceLocation("armor_trims:flint_and_steel_cop"),
            new ResourceLocation("armor_trims:heavt_weighted_pressure_plate_cop"),
            new ResourceLocation("armor_trims:hopper"),
            new ResourceLocation("armor_trims:iron_axe_cop"),
            new ResourceLocation("armor_trims:iron_trapdoor_cop"),
            new ResourceLocation("armor_trims:iron_axe_cop"),
            new ResourceLocation("armor_trims:iron_axe_cop_2"),
            new ResourceLocation("armor_trims:iron_bars"),
            new ResourceLocation("armor_trims:iron_boots_cop"),
            new ResourceLocation("armor_trims:iron_chestplate_cop"),
            new ResourceLocation("armor_trims:iron_door_cop"),
            new ResourceLocation("armor_trims:iron_door_cop_2"),
            new ResourceLocation("armor_trims:iron_helmet_cop"),
            new ResourceLocation("armor_trims:iron_hoe_cop"),
            new ResourceLocation("armor_trims:iron_hoe_cop_2"),
            new ResourceLocation("armor_trims:iron_leggings_cop"),
            new ResourceLocation("armor_trims:iron_nugget"),
            new ResourceLocation("armor_trims:iron_sword_cop"),
            new ResourceLocation("armor_trims:minecart_cop"),
            new ResourceLocation("armor_trims:piston_cop"),
            new ResourceLocation("armor_trims:rail_cop"),
            new ResourceLocation("armor_trims:shears_cop"),
            new ResourceLocation("armor_trims:shield_cop"),
            new ResourceLocation("armor_trims:smithing_table_cop"),
            new ResourceLocation("armor_trims:stonecutter_cop"),
            new ResourceLocation("armor_trims:tripwire_hook")};
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
        player.awardRecipesByKey(COPPER_RECIPES);
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
        player.resetRecipes(Arrays.stream(COPPER_RECIPES).map(resourceLocation -> server.getRecipeManager().byKey(resourceLocation))
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

        ArmorTrimAbilities.spawnParticlesInCircle(world, ParticleTypes.CLOUD, effectPlayer.getX(), effectPlayer.getY(), effectPlayer.getZ(), 3, 10);
    }

    public static void setDoubleJumping(PlayerDuck playerDuck, boolean doubleJumping) {
        playerDuck.armorTrimsMod$setDoubleJumping(doubleJumping);
    }

    public static float getJumpBoostPower(int amplifier) {
        return 0.1F * (amplifier + 1.0F);
    }
}
