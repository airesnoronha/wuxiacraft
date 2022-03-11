package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.init.WuxiaDefaultTechniqueManuals;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;

public class CreateDefaultManualCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("create_manual").requires((source) -> source.hasPermission(2))
				.then(Commands.argument("target", EntityArgument.player())
						.then(Commands.argument("technique", ResourceLocationArgument.id())
								.executes(CreateDefaultManualCommand::createTechniqueManual)
						)
				)
		);
	}

	public static int createTechniqueManual(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		var manualLocation = ResourceLocationArgument.getId(ctx, "technique");
		var itemStack = WuxiaDefaultTechniqueManuals.getDefaultManual(manualLocation);
		if (itemStack == null) {
			ctx.getSource().sendFailure(new TextComponent("It wasn't possible to find the manual"));
			return 0;
		}
		ItemEntity itementity = target.drop(itemStack.get(), false);
		if (itementity != null) {
			itementity.setNoPickUpDelay();
			itementity.setOwner(target.getUUID());
		}
		return 1;
	}
}
