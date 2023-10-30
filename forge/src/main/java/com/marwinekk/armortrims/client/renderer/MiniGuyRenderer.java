
package com.marwinekk.armortrims.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import com.marwinekk.armortrims.entity.MiniGuyEntity;
import com.marwinekk.armortrims.client.model.Modelminiguy;

public class MiniGuyRenderer extends MobRenderer<MiniGuyEntity, Modelminiguy<MiniGuyEntity>> {
	public MiniGuyRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelminiguy<>(context.bakeLayer(Modelminiguy.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(MiniGuyEntity entity) {
		return new ResourceLocation("armor_trims:textures/entities/miniguy.png");
	}
}
