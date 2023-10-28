
package com.marwinekk.armortrims.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.WitchModel;

import com.marwinekk.armortrims.entity.CloneEntity;

public class CloneRenderer extends MobRenderer<CloneEntity, WitchModel<CloneEntity>> {
	public CloneRenderer(EntityRendererProvider.Context context) {
		super(context, new WitchModel(context.bakeLayer(ModelLayers.WITCH)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(CloneEntity entity) {
		return new ResourceLocation("armor_trims:textures/entities/witch.png");
	}
}
