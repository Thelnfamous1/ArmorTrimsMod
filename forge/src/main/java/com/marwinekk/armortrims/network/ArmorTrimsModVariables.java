package com.marwinekk.armortrims.network;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

import com.marwinekk.armortrims.ArmorTrimsModForge;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorTrimsModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ArmorTrimsModForge.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.register(PlayerVariables.class);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().level().isClientSide())
				((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getEntity().level().isClientSide())
				((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().level().isClientSide())
				((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			clone.iron = original.iron;
			clone.lapis = original.lapis;
			clone.gold = original.gold;
			clone.diamond = original.diamond;
			clone.amethyst = original.amethyst;
			clone.netherite = original.netherite;
			clone.redstone = original.redstone;
			clone.copper = original.copper;
			clone.quartz = original.quartz;
			clone.emerald = original.emerald;
			clone.canUseIron = original.canUseIron;
			clone.canUseLapis = original.canUseLapis;
			clone.canUseGold = original.canUseGold;
			clone.canUseDiamond = original.canUseDiamond;
			clone.canUseAmethyst = original.canUseAmethyst;
			clone.canUseNetherite = original.canUseNetherite;
			clone.canUseRedstone = original.canUseRedstone;
			clone.canUseCopper = original.canUseCopper;
			clone.canUseQuartz = original.canUseQuartz;
			clone.canUseEmerald = original.canUseEmerald;
			clone.cooldownIron = original.cooldownIron;
			clone.targeting = original.targeting;
			clone.died = original.died;
			clone.didntDie = original.didntDie;
			clone.timer = original.timer;
			clone.cooldownNetherite = original.cooldownNetherite;
			if (!event.isWasDeath()) {
				clone.beaconeffect = original.beaconeffect;
				clone.dragonegg = original.dragonegg;
				clone.helmet = original.helmet;
				clone.chestplate = original.chestplate;
				clone.leggings = original.leggings;
				clone.boots = original.boots;
				clone.combatpressed = original.combatpressed;
			}
		}
	}

	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerVariables>() {
	});

	@Mod.EventBusSubscriber
	private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
				event.addCapability(new ResourceLocation("armor_trims", "player_variables"), new PlayerVariablesProvider());
		}

		private final PlayerVariables playerVariables = new PlayerVariables();
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Override
		public Tag serializeNBT() {
			return playerVariables.writeNBT();
		}

		@Override
		public void deserializeNBT(Tag nbt) {
			playerVariables.readNBT(nbt);
		}
	}

	public static class PlayerVariables {
		public boolean iron = false;
		public boolean lapis = false;
		public boolean gold = false;
		public boolean diamond = false;
		public boolean amethyst = false;
		public boolean netherite = false;
		public boolean redstone = false;
		public boolean copper = false;
		public boolean quartz = false;
		public boolean emerald = false;
		public boolean canUseIron = true;
		public boolean canUseLapis = true;
		public boolean canUseGold = true;
		public boolean canUseDiamond = true;
		public boolean canUseAmethyst = true;
		public boolean canUseNetherite = true;
		public boolean canUseRedstone = true;
		public boolean canUseCopper = true;
		public boolean canUseQuartz = true;
		public boolean canUseEmerald = true;
		public boolean cooldownIron = false;
		public boolean targeting = false;
		public boolean died = false;
		public boolean didntDie = false;
		public double timer = 0;
		public boolean cooldownNetherite = false;
		public String beaconeffect = "\"\"";
		public boolean dragonegg = false;
		public String helmet = "\"\"";
		public String chestplate = "\"\"";
		public String leggings = "\"\"";
		public String boots = "\"\"";
		public double combatpressed = 0;

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				ArmorTrimsModForge.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
		}

		public Tag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.putBoolean("iron", iron);
			nbt.putBoolean("lapis", lapis);
			nbt.putBoolean("gold", gold);
			nbt.putBoolean("diamond", diamond);
			nbt.putBoolean("amethyst", amethyst);
			nbt.putBoolean("netherite", netherite);
			nbt.putBoolean("redstone", redstone);
			nbt.putBoolean("copper", copper);
			nbt.putBoolean("quartz", quartz);
			nbt.putBoolean("emerald", emerald);
			nbt.putBoolean("canUseIron", canUseIron);
			nbt.putBoolean("canUseLapis", canUseLapis);
			nbt.putBoolean("canUseGold", canUseGold);
			nbt.putBoolean("canUseDiamond", canUseDiamond);
			nbt.putBoolean("canUseAmethyst", canUseAmethyst);
			nbt.putBoolean("canUseNetherite", canUseNetherite);
			nbt.putBoolean("canUseRedstone", canUseRedstone);
			nbt.putBoolean("canUseCopper", canUseCopper);
			nbt.putBoolean("canUseQuartz", canUseQuartz);
			nbt.putBoolean("canUseEmerald", canUseEmerald);
			nbt.putBoolean("cooldownIron", cooldownIron);
			nbt.putBoolean("targeting", targeting);
			nbt.putBoolean("died", died);
			nbt.putBoolean("didntDie", didntDie);
			nbt.putDouble("timer", timer);
			nbt.putBoolean("cooldownNetherite", cooldownNetherite);
			nbt.putString("beaconeffect", beaconeffect);
			nbt.putBoolean("dragonegg", dragonegg);
			nbt.putString("helmet", helmet);
			nbt.putString("chestplate", chestplate);
			nbt.putString("leggings", leggings);
			nbt.putString("boots", boots);
			nbt.putDouble("combatpressed", combatpressed);
			return nbt;
		}

		public void readNBT(Tag Tag) {
			CompoundTag nbt = (CompoundTag) Tag;
			iron = nbt.getBoolean("iron");
			lapis = nbt.getBoolean("lapis");
			gold = nbt.getBoolean("gold");
			diamond = nbt.getBoolean("diamond");
			amethyst = nbt.getBoolean("amethyst");
			netherite = nbt.getBoolean("netherite");
			redstone = nbt.getBoolean("redstone");
			copper = nbt.getBoolean("copper");
			quartz = nbt.getBoolean("quartz");
			emerald = nbt.getBoolean("emerald");
			canUseIron = nbt.getBoolean("canUseIron");
			canUseLapis = nbt.getBoolean("canUseLapis");
			canUseGold = nbt.getBoolean("canUseGold");
			canUseDiamond = nbt.getBoolean("canUseDiamond");
			canUseAmethyst = nbt.getBoolean("canUseAmethyst");
			canUseNetherite = nbt.getBoolean("canUseNetherite");
			canUseRedstone = nbt.getBoolean("canUseRedstone");
			canUseCopper = nbt.getBoolean("canUseCopper");
			canUseQuartz = nbt.getBoolean("canUseQuartz");
			canUseEmerald = nbt.getBoolean("canUseEmerald");
			cooldownIron = nbt.getBoolean("cooldownIron");
			targeting = nbt.getBoolean("targeting");
			died = nbt.getBoolean("died");
			didntDie = nbt.getBoolean("didntDie");
			timer = nbt.getDouble("timer");
			cooldownNetherite = nbt.getBoolean("cooldownNetherite");
			beaconeffect = nbt.getString("beaconeffect");
			dragonegg = nbt.getBoolean("dragonegg");
			helmet = nbt.getString("helmet");
			chestplate = nbt.getString("chestplate");
			leggings = nbt.getString("leggings");
			boots = nbt.getString("boots");
			combatpressed = nbt.getDouble("combatpressed");
		}
	}

	public static class PlayerVariablesSyncMessage {
		public PlayerVariables data;

		public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
			this.data = new PlayerVariables();
			this.data.readNBT(buffer.readNbt());
		}

		public PlayerVariablesSyncMessage(PlayerVariables data) {
			this.data = data;
		}

		public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeNbt((CompoundTag) message.data.writeNBT());
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
					variables.iron = message.data.iron;
					variables.lapis = message.data.lapis;
					variables.gold = message.data.gold;
					variables.diamond = message.data.diamond;
					variables.amethyst = message.data.amethyst;
					variables.netherite = message.data.netherite;
					variables.redstone = message.data.redstone;
					variables.copper = message.data.copper;
					variables.quartz = message.data.quartz;
					variables.emerald = message.data.emerald;
					variables.canUseIron = message.data.canUseIron;
					variables.canUseLapis = message.data.canUseLapis;
					variables.canUseGold = message.data.canUseGold;
					variables.canUseDiamond = message.data.canUseDiamond;
					variables.canUseAmethyst = message.data.canUseAmethyst;
					variables.canUseNetherite = message.data.canUseNetherite;
					variables.canUseRedstone = message.data.canUseRedstone;
					variables.canUseCopper = message.data.canUseCopper;
					variables.canUseQuartz = message.data.canUseQuartz;
					variables.canUseEmerald = message.data.canUseEmerald;
					variables.cooldownIron = message.data.cooldownIron;
					variables.targeting = message.data.targeting;
					variables.died = message.data.died;
					variables.didntDie = message.data.didntDie;
					variables.timer = message.data.timer;
					variables.cooldownNetherite = message.data.cooldownNetherite;
					variables.beaconeffect = message.data.beaconeffect;
					variables.dragonegg = message.data.dragonegg;
					variables.helmet = message.data.helmet;
					variables.chestplate = message.data.chestplate;
					variables.leggings = message.data.leggings;
					variables.boots = message.data.boots;
					variables.combatpressed = message.data.combatpressed;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
