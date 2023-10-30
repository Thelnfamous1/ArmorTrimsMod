package com.marwinekk.armortrims.procedures;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AmethystPassiveProcedure {
	@SubscribeEvent
	public static void onEntityTarget(LivingChangeTargetEvent event) {
		LivingEntity victim = event.getOriginalTarget();
		if(ArmorTrimsMod.changeTarget(victim,event.getEntity())) {
			event.setNewTarget(null);
		}
	}
}
