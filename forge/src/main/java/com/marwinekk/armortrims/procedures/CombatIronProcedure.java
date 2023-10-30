package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import javax.annotation.Nullable;

import com.marwinekk.armortrims.ArmorTrimsModForge;

@Mod.EventBusSubscriber
public class CombatIronProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
	/*	if (entity == null || sourceentity == null)
			return;
		if (sourceentity instanceof Player && entity instanceof LivingEntity) {
			if ((sourceentity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).cooldownIron) {
				if (entity instanceof Player) {
					ArmorTrimsModForge.queueServerWork(2, () -> {
						entity.setDeltaMovement(new Vec3(0, 3, 0));
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (entity.getX()), (entity.getY() + 0.5), (entity.getZ()), 50, 0.15, 0.15, 0.15, 0.1);
					});
				} else {
					ArmorTrimsModForge.queueServerWork(2, () -> {
						entity.setDeltaMovement(new Vec3(0, 3, 0));
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (entity.getX()), (entity.getY() + 0.5), (entity.getZ()), 50, 0.15, 0.15, 0.15, 0.1);
					});
				}
			} else if ((sourceentity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).cooldownNetherite) {
				LivingEntity _entity = (LivingEntity) entity;
				if (!_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1, true, true));
				if (!_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, true, true));
			}
		}*/
	}
}
