package com.marwinekk.armortrims.procedures;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

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
