package com.marwinekk.armortrims.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

import com.marwinekk.armortrims.world.inventory.MenuBeaconMenu;
import com.marwinekk.armortrims.network.MenuBeaconButtonMessage;
import com.marwinekk.armortrims.ArmorTrimsModForge;

public class MenuBeaconScreen extends AbstractContainerScreen<MenuBeaconMenu> {
	private final static HashMap<String, Object> guistate = MenuBeaconMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_speed;
	ImageButton imagebutton_haste;
	ImageButton imagebutton_resistance;
	ImageButton imagebutton_jump_boost;
	ImageButton imagebutton_strength;
	ImageButton imagebutton_hero_of_the_village;

	public MenuBeaconScreen(MenuBeaconMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 201;
		this.imageHeight = 33;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		imagebutton_speed = new ImageButton(this.leftPos + 1, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_speed.png"), 18, 36, e -> {
			if (true) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(0, x, y, z));
				MenuBeaconButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_speed", imagebutton_speed);
		this.addRenderableWidget(imagebutton_speed);
		imagebutton_haste = new ImageButton(this.leftPos + 37, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_haste.png"), 18, 36, e -> {
			if (true) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(1, x, y, z));
				MenuBeaconButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:imagebutton_haste", imagebutton_haste);
		this.addRenderableWidget(imagebutton_haste);
		imagebutton_resistance = new ImageButton(this.leftPos + 73, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_resistance.png"), 18, 36, e -> {
			if (true) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(2, x, y, z));
				MenuBeaconButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:imagebutton_resistance", imagebutton_resistance);
		this.addRenderableWidget(imagebutton_resistance);
		imagebutton_jump_boost = new ImageButton(this.leftPos + 109, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_jump_boost.png"), 18, 36, e -> {
			if (true) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(3, x, y, z));
				MenuBeaconButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		});
		guistate.put("button:imagebutton_jump_boost", imagebutton_jump_boost);
		this.addRenderableWidget(imagebutton_jump_boost);
		imagebutton_strength = new ImageButton(this.leftPos + 145, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_strength.png"), 18, 36, e -> {
			if (true) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(4, x, y, z));
				MenuBeaconButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		});
		guistate.put("button:imagebutton_strength", imagebutton_strength);
		this.addRenderableWidget(imagebutton_strength);
		imagebutton_hero_of_the_village = new ImageButton(this.leftPos + 181, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_hero_of_the_village.png"), 18, 36, e -> {
			if (true) {
				ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(5, x, y, z));
				MenuBeaconButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		});
		guistate.put("button:imagebutton_hero_of_the_village", imagebutton_hero_of_the_village);
		this.addRenderableWidget(imagebutton_hero_of_the_village);
	}
}
