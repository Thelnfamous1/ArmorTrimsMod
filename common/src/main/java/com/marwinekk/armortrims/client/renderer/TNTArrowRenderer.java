package com.marwinekk.armortrims.client.renderer;

import com.marwinekk.armortrims.entity.TNTArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

public class TNTArrowRenderer extends ArrowRenderer<TNTArrowEntity> {
	public TNTArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(TNTArrowEntity entity) {
		return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
	}
}
