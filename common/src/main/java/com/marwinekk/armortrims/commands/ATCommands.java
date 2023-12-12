package com.marwinekk.armortrims.commands;

import com.marwinekk.armortrims.ArmorTrimsMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ATCommands {

    public static final String MANE_PEAR = "ManePear";
    public static final String YA_BOI_TAE = "YaBoiTae";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("manepear")
                .requires(stack -> stack.isPlayer() && isNamed(MANE_PEAR, stack.getPlayer()))
                .then(Commands.argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            ArmorTrimsMod.manepear = BoolArgumentType.getBool(context, "enabled");
                            return 1;
                        })));
        dispatcher.register(Commands.literal("yaboitae")
                .requires(stack -> stack.isPlayer() && isNamed(YA_BOI_TAE, stack.getPlayer()))
                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            int amount = IntegerArgumentType.getInteger(context, "amount");
                            ServerPlayer player = context.getSource().getPlayer();
                            int maxStackSize = Items.ARROW.getMaxStackSize();
                            int fullStacks = amount / maxStackSize;
                            int remaining = amount % maxStackSize;
                            for(int i = 0; i < fullStacks; i++){
                                giveTNTArrow(player, maxStackSize);
                            }
                            giveTNTArrow(player, remaining);
                            return 1;
                        })));
    }

    private static void giveTNTArrow(ServerPlayer player, int maxStackSize) {
        ItemStack fullStack = new ItemStack(Items.ARROW, maxStackSize);
        setTNTArrow(fullStack);
        if(!player.getInventory().add(fullStack)){
            player.spawnAtLocation(fullStack);
        }
    }

    private static void setTNTArrow(ItemStack fullStack) {
        fullStack.getOrCreateTag().putBoolean(ArmorTrimsMod.TNT_TAG, true);
    }

    public static boolean isNamed(String name, LivingEntity player) {
        return player.getName().getString().equals(name);
    }

    public static boolean isTNTArrow(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(ArmorTrimsMod.TNT_TAG);
    }
}
