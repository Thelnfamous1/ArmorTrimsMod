package com.marwinekk.armortrims.procedures;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.damagesource.DamageSource;

@Mod.EventBusSubscriber
public class CopperPassiveProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		LivingEntity livingEntity = event.getEntity();
		DamageSource source = event.getSource();
		if (ArmorTrimsMod.attackEvent(livingEntity,source)) {
			event.setCanceled(true);
		}
	}
}
