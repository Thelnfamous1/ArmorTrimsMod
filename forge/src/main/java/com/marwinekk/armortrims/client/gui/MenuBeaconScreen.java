package com.marwinekk.armortrims.client.gui;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.ArmorTrimsModForge;
import com.marwinekk.armortrims.network.MenuBeaconButtonMessage;
import com.marwinekk.armortrims.world.inventory.MenuBeaconMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

public class MenuBeaconScreen extends AbstractContainerScreen<MenuBeaconMenu> {


    ImageButton imagebutton_speed;
    ImageButton imagebutton_haste;
    ImageButton imagebutton_resistance;
    ImageButton imagebutton_jump_boost;
    ImageButton imagebutton_strength;
    ImageButton imagebutton_hero_of_the_village;

    public MenuBeaconScreen(MenuBeaconMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
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
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public void init() {
        super.init();
        imagebutton_speed = new ImageButton(this.leftPos + 1, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_speed.png"), 18, 36, e -> {
            sendEffect(0);
        });
        this.addRenderableWidget(imagebutton_speed);
        imagebutton_haste = new ImageButton(this.leftPos + 37, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_haste.png"), 18, 36, e -> {
            sendEffect(1);
        });
        this.addRenderableWidget(imagebutton_haste);
        imagebutton_resistance = new ImageButton(this.leftPos + 73, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_resistance.png"), 18, 36, e -> {
            sendEffect(2);
        });
        this.addRenderableWidget(imagebutton_resistance);
        imagebutton_jump_boost = new ImageButton(this.leftPos + 109, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_jump_boost.png"), 18, 36, e -> {
            sendEffect(3);
        });
        this.addRenderableWidget(imagebutton_jump_boost);
        imagebutton_strength = new ImageButton(this.leftPos + 145, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_strength.png"), 18, 36, e -> {
            sendEffect(4);
        });
        this.addRenderableWidget(imagebutton_strength);
        imagebutton_hero_of_the_village = new ImageButton(this.leftPos + 181, this.topPos + 3, 18, 18, 0, 0, 18, new ResourceLocation("armor_trims:textures/screens/atlas/imagebutton_hero_of_the_village.png"), 18, 36, e -> {
            sendEffect(5);
        });
        this.addRenderableWidget(imagebutton_hero_of_the_village);
    }

    void sendEffect(int id) {
        ArmorTrimsModForge.PACKET_HANDLER.sendToServer(new MenuBeaconButtonMessage(ArmorTrimsMod.BEACON_EFFECTS.get(id)));
    }
}
