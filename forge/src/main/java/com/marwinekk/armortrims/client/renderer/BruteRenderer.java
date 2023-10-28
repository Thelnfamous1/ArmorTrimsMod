
package com.marwinekk.armortrims.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import com.marwinekk.armortrims.entity.BruteEntity;
import com.marwinekk.armortrims.client.model.Modelcustom_model;

public class BruteRenderer extends MobRenderer<BruteEntity, Modelcustom_model<BruteEntity>> {
	public BruteRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelcustom_model(context.bakeLayer(Modelcustom_model.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BruteEntity entity) {
		return new ResourceLocation("armor_trims:textures/entities/piglinbrute.png");
	}
}
