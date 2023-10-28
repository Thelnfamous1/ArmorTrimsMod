
package com.marwinekk.armortrims.command;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

import com.marwinekk.armortrims.procedures.ExProcedure;

@Mod.EventBusSubscriber
public class TtCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("tt")

				.executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					ExProcedure.execute(entity);
					return 0;
				}));
	}
}
