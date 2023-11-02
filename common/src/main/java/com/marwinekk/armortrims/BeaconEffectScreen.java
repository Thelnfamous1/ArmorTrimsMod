package com.marwinekk.armortrims;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;

public class BeaconEffectScreen extends Screen {
    public BeaconEffectScreen(Component component) {
        super(component);
    }



    @Override
    protected void init() {
        super.init();

        int leftPos = (this.width - 176) / 2;
        int topPos = (this.height - 166) / 2;

        for (int i = 0 ; i < 6;i++) {
            addRenderableWidget(new BeaconButton(leftPos + 35 * i,topPos,ArmorTrimsMod.BEACON_EFFECTS.get(i)));
        }
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        this.renderBackground($$0);
        super.render($$0, $$1, $$2, $$3);
    }

    public static class BeaconButton extends BeaconScreen.BeaconScreenButton {
        private MobEffect effect;
        private TextureAtlasSprite sprite;


        public BeaconButton(int pX, int pY, MobEffect pEffect) {
            super(pX, pY);
            this.setEffect(pEffect);
        }

        protected void setEffect(MobEffect pEffect) {
            this.effect = pEffect;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(pEffect);
            this.setTooltip(Tooltip.create(this.createEffectDescription(pEffect), (Component)null));
        }

        protected MutableComponent createEffectDescription(MobEffect pEffect) {
            return Component.translatable(pEffect.getDescriptionId());
        }

        public void onPress() {

        }

        protected void renderIcon(GuiGraphics pGuiGraphics) {
            pGuiGraphics.blit(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
        }

        public void updateStatus(int pBeaconTier) {
        }

        protected MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.effect);
        }
    }


}
