package wuxiacraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;

public class CultivationCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("cult")
						.requires(commandSourceStack -> commandSourceStack.hasPermission(2))
						.then(Commands.literal("get")
										.then(Commands.argument("target", EntityArgument.player())
														.executes(CultivationCommand::getCultivation))
						)
		);
	}

	public static int getCultivation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");
		for(var system : Cultivation.System.values()) {
			String systemName = system.name();
			message.append(systemName).append(" stats: ").append("\n");
			message.append("CultivationBase: ").append(String.format("%.1f", cultivation.getSystemData(system).cultivationBase)).append("\n");
			message.append("Foundation: ").append(String.format("%.1f", cultivation.getSystemData(system).foundation)).append("\n");
			message.append("Energy: ").append(String.format("%.1f", cultivation.getSystemData(system).energy)).append("\n\n");
		}

		ctx.getSource().sendSuccess(message, true);
		return 1;
	}

}
