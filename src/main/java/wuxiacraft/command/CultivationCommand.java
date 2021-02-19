package wuxiacraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;

public class CultivationCommand {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("cult").requires(commandSource -> commandSource.hasPermissionLevel(2))
				.then(Commands.literal("get").then(
						Commands.argument("target", EntityArgument.player())
						.executes(CultivationCommand::getCultivationFromPlayer))
				)
				.then(Commands.literal("reset").then(
						Commands.argument("target", EntityArgument.player())
								.then(Commands.literal("confirm").then(Commands.literal("yes")
										.executes(CultivationCommand::resetCultivationToPlayer)))

				))
		);
	}

	/**
	 * Sends a probably detailed information to the requester about a certain person
	 * @param ctx everything about the command
	 * @return idk yet
	 * @throws CommandSyntaxException it's better to let it into the signature, since probably commands api may handle it better
	 */
	private static int getCultivationFromPlayer(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		//it says it's never null so i guess arguments already give errors when it's not null
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);

		SystemStats stats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		StringTextComponent message = new StringTextComponent(String.format("Body Level: %s\nRank: %d\nCultivation Base: %.1f/ %.1f (%.2f%%)\nFoundation: %.1f (%.3f)",
				stats.getLevel().levelName, stats.getSubLevel() + 1, stats.getBase(), stats.getLevel().getProgressBySubLevel(stats.getSubLevel()),
				(stats.getBase()*100/ stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getFoundation(), (stats.getFoundation()/stats.getLevel().getProgressBySubLevel(stats.getSubLevel())))
				);
		//this one was tricky, like y not sendMessage()
		ctx.getSource().sendFeedback(message, true);

		stats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		message = new StringTextComponent(String.format("Divine Level: %s\nRank: %d\nCultivation Base: %.1f/ %.1f (%.2f%%)\nFoundation: %.1f (%.3f)",
				stats.getLevel().levelName, stats.getSubLevel() + 1, stats.getBase(), stats.getLevel().getProgressBySubLevel(stats.getSubLevel()),
				(stats.getBase()*100/ stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getFoundation(), (stats.getFoundation()/stats.getLevel().getProgressBySubLevel(stats.getSubLevel())))
		);
		ctx.getSource().sendFeedback(message, true);

		stats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		message = new StringTextComponent(String.format("Essence Level: %s\nRank: %d\nCultivation Base: %.1f/ %.1f (%.2f%%)\nFoundation: %.1f (%.3f)",
				stats.getLevel().levelName, stats.getSubLevel() + 1, stats.getBase(), stats.getLevel().getProgressBySubLevel(stats.getSubLevel()),
				(stats.getBase()*100/ stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getFoundation(), (stats.getFoundation()/stats.getLevel().getProgressBySubLevel(stats.getSubLevel())))
		);
		ctx.getSource().sendFeedback(message, true);

		return 1;
	}

	private static int resetCultivationToPlayer(CommandContext<CommandSource> ctx) throws CommandSyntaxException {

		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);

		cultivation.copyFrom(new Cultivation());

		target.sendMessage(new StringTextComponent("Your cultivation has been reset!"), Util.DUMMY_UUID);
		ctx.getSource().sendFeedback(new StringTextComponent("Target player cultivation has been reset!"), true);
		return 1;
	}

}
