package com.marwinekk.armortrims.network;

import com.marwinekk.armortrims.ArmorTrimsMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class C2SKeybindPacket {

    @Nullable EquipmentSlot slot;
    public C2SKeybindPacket(FriendlyByteBuf buffer) {
        int id = buffer.readInt();
        this.slot = id > -1 ? EquipmentSlot.values()[id] : null;
    }

    public C2SKeybindPacket(@Nullable EquipmentSlot slot) {
        this.slot = slot;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(slot == null ? -1 : slot.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ArmorTrimsMod.activateCombatAbility(player,slot);
        });
        context.setPacketHandled(true);
    }

}
