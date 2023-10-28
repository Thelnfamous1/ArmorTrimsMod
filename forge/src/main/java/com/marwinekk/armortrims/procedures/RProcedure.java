package com.marwinekk.armortrims.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;

@Mod.EventBusSubscriber
public class RProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onRenderPlayer(RenderLivingEvent.Pre event) {
		LivingEntity entity = event.getEntity();
		if (entity == null)
			return;
		if (entity instanceof LivingEntity && entity.hasEffect(MobEffects.INVISIBILITY)) {
			event.setCanceled(true);
		}
	}
}
