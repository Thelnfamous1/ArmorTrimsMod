package com.marwinekk.armortrims.procedures;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.marwinekk.armortrims.network.ArmorTrimsModVariables;

@Mod.EventBusSubscriber
public class PassiveIronProcedure {
	@SubscribeEvent
	public static void onEntityFall(LivingFallEvent event) {
		LivingEntity livingEntity = event.getEntity();
		if (ArmorTrimsMod.onFallDamage(livingEntity)) {
			event.setCanceled(true);
		}
	}
}
