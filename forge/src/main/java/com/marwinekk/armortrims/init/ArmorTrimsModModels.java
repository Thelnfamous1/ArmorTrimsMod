
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.marwinekk.armortrims.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import com.marwinekk.armortrims.client.model.Modelminiguy;
import com.marwinekk.armortrims.client.model.Modelhigharrow_java;
import com.marwinekk.armortrims.client.model.Modelcustom_model;
import com.marwinekk.armortrims.client.model.Modelbrute;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ArmorTrimsModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelhigharrow_java.LAYER_LOCATION, Modelhigharrow_java::createBodyLayer);
		event.registerLayerDefinition(Modelcustom_model.LAYER_LOCATION, Modelcustom_model::createBodyLayer);
		event.registerLayerDefinition(Modelbrute.LAYER_LOCATION, Modelbrute::createBodyLayer);
		event.registerLayerDefinition(Modelminiguy.LAYER_LOCATION, Modelminiguy::createBodyLayer);
	}
}
