package com.marwinekk.armortrims.platform;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.marwinekk.armortrims.network.PacketHandler;
import com.marwinekk.armortrims.platform.services.IPlatformHelper;
import com.marwinekk.armortrims.util.ArmorTrimAbilities;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class FabricPlatformHelper implements IPlatformHelper {

    public static final String DIAMOND_TRIM_BONUS_SLOT_NAME = "diamond_trim_bonus";

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void sendAbilityPacket(EquipmentSlot slot) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(slot != null ? slot.ordinal() : -1);
        ClientPlayNetworking.send(PacketHandler.keybind,buf);
    }

    @Override
    public void sendMobEffectPacket(MobEffect effect, int amplifier) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(MobEffect.getId(effect));
        buf.writeInt(amplifier);
        ClientPlayNetworking.send(PacketHandler.mob_effect,buf);
    }

    private static final AttributeModifier ADD_DIAMOND_TRIM_BONUS_SLOTS = new AttributeModifier(ArmorTrimAbilities.MODIFIER_UUID, DIAMOND_TRIM_BONUS_SLOT_NAME, ArmorTrimsMod.BONUS_SLOTS, AttributeModifier.Operation.ADDITION);

    @Override
    public void addExtraInventorySlots(Player player) {
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            TrinketInventory diamondTrimBonusInv = getDiamondTrimBonusInv(trinketComponent);
            if(diamondTrimBonusInv != null && !diamondTrimBonusInv.getModifiers().containsKey(ArmorTrimAbilities.MODIFIER_UUID)){
                diamondTrimBonusInv.addPersistentModifier(ADD_DIAMOND_TRIM_BONUS_SLOTS);
            }
        });
    }

    @Nullable
    private static TrinketInventory getDiamondTrimBonusInv(TrinketComponent trinketComponent) {
        return trinketComponent.getInventory().get(ArmorTrimsMod.MOD_ID).get(DIAMOND_TRIM_BONUS_SLOT_NAME);
    }

    @Override
    public void removeExtraInventorySlots(Player player) {
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            TrinketInventory diamondTrimBonusInv = getDiamondTrimBonusInv(trinketComponent);
            if(diamondTrimBonusInv != null){
                diamondTrimBonusInv.removeModifier(ArmorTrimAbilities.MODIFIER_UUID);
            }
        });
    }

    @Override
    public void sendDoubleJump(Player player) {
        FriendlyByteBuf passedData = new FriendlyByteBuf(Unpooled.buffer());
        passedData.writeUUID(player.getUUID());

        ClientPlayNetworking.send(PacketHandler.C2S_DO_DOUBLEJUMP, passedData);
    }
}
