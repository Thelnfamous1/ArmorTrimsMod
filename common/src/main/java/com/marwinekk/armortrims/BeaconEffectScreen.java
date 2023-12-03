package com.marwinekk.armortrims;

import com.marwinekk.armortrims.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import org.apache.commons.lang3.tuple.Pair;

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
            Pair<MobEffect, Integer> beaconEffect = ArmorTrimsMod.BEACON_EFFECTS.get(i);
            addRenderableWidget(new BeaconButton(leftPos + 35 * i,topPos, beaconEffect.getLeft(), beaconEffect.getRight()));
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
        private int amplifier;


        public BeaconButton(int pX, int pY, MobEffect pEffect, int amplifier) {
            super(pX, pY);
            this.setEffect(pEffect, amplifier);
        }

        protected void setEffect(MobEffect pEffect, int amplifier) {
            this.effect = pEffect;
            this.amplifier = amplifier;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(pEffect);
            this.setTooltip(Tooltip.create(this.createEffectDescription(pEffect, amplifier), (Component)null));
        }

        protected MutableComponent createEffectDescription(MobEffect pEffect, int amplifier) {
            MutableComponent effectDescription = Component.translatable(pEffect.getDescriptionId());
            if (amplifier >= 1 && amplifier <= 9) {
                effectDescription.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (amplifier + 1)));
            }
            return effectDescription;
        }

        public void onPress() {
            Services.PLATFORM.sendMobEffectPacket(effect, this.amplifier);
        }

        protected void renderIcon(GuiGraphics pGuiGraphics) {
            pGuiGraphics.blit(this.getX() + 2, this.getY() + 2, 0, 18, 18, this.sprite);
        }

        public void updateStatus(int pBeaconTier) {
        }

        protected MutableComponent createNarrationMessage() {
            return this.createEffectDescription(this.effect, this.amplifier);
        }
    }


}
