package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;

import javax.annotation.Nullable;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class CopperPassiveProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() != null) {
			execute(event, event.getSource(), event.getEntity());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity) {
		execute(null, damagesource, entity);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity) {
		if (damagesource == null || entity == null)
			return;
		if (damagesource.is(DamageTypes.LIGHTNING_BOLT) && (entity.getCapability(ArmorTrimsModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ArmorTrimsModVariables.PlayerVariables())).copper) {
			if (event != null && event.isCancelable()) {
				event.setCanceled(true);
			}
		}
	}
}
