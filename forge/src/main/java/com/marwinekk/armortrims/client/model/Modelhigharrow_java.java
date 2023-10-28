package com.marwinekk.armortrims.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelhigharrow_java<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("armor_trims", "modelhigharrow_java"), "main");
	public final ModelPart group;

	public Modelhigharrow_java(ModelPart root) {
		this.group = root.getChild("group");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition group = partdefinition.addOrReplaceChild("group",
				CubeListBuilder.create().texOffs(0, 13).addBox(-5.4375F, -0.5F, -6.0625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 13).addBox(-5.4375F, -0.5F, -4.0625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 0)
						.addBox(-6.4375F, -0.5F, -5.0625F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 12).addBox(-4.4375F, -0.5F, -4.0625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 11)
						.addBox(-3.4375F, -0.5F, -4.0625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 9).addBox(-3.4375F, -0.5F, -3.0625F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 11)
						.addBox(-2.4375F, -0.5F, -2.0625F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 6).addBox(-1.4375F, -0.5F, -1.0625F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 10)
						.addBox(-0.4375F, -0.5F, -0.0625F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 4).addBox(0.5625F, -0.5F, 0.9375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 9)
						.addBox(0.5625F, -0.5F, 2.9375F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 7).addBox(1.5625F, -0.5F, 3.9375F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(2.5625F, -0.5F, 0.9375F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(6, 4).addBox(3.5625F, -0.5F, 4.9375F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 2)
						.addBox(4.5625F, -0.5F, 3.9375F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 7).addBox(4.5625F, -0.5F, 1.9375F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.4375F, 15.5F, 0.0625F, 0.2132F, 0.762F, -1.3979F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
