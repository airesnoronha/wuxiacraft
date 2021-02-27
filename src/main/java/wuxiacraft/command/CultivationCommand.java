package wuxiacraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.SystemStats;
import wuxiacraft.network.CultivationSyncMessage;
import wuxiacraft.network.WuxiaPacketHandler;

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
				.then(Commands.literal("set").then(
						Commands.argument("target", EntityArgument.player()).then(Commands.argument("system", StringArgumentType.word())
								.then(Commands.argument("level", StringArgumentType.word()).then(Commands.argument("rank", IntegerArgumentType.integer(0, 8))
										.executes(CultivationCommand::setCultivationLevel))))
				))
				.then(Commands.literal("refill").then(
						Commands.argument("target", EntityArgument.player())
								.executes(CultivationCommand::refillEnergies)
				))
		);
	}

	/**
	 * Sends a probably detailed information to the requester about a certain person
	 *
	 * @param ctx everything about the command
	 * @return idk yet
	 * @throws CommandSyntaxException it's better to let it into the signature, since probably commands api may handle it better
	 */
	private static int getCultivationFromPlayer(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		//it says it's never null so i guess arguments already give errors when it's not null
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);

		SystemStats stats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
		StringTextComponent message = new StringTextComponent(String.format("Body Level: %s\nRank: %d\nCultivation Base: %.1f/ %.1f (%.2f%%)\nFoundation: %.1f (%.3f)\nEnergy: %.1f %.1f (%.0f%%)",
				stats.getLevel().levelName, stats.getSubLevel() + 1, stats.getBase(), stats.getLevel().getProgressBySubLevel(stats.getSubLevel()),
				(stats.getBase() * 100 / stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getFoundation(), (stats.getFoundation() / stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getEnergy(), cultivation.getMaxBodyEnergy(), (stats.getEnergy() * 100 / cultivation.getMaxBodyEnergy()))
		);
		//this one was tricky, like y not sendMessage()
		ctx.getSource().sendFeedback(message, true);

		stats = cultivation.getStatsBySystem(CultivationLevel.System.DIVINE);
		message = new StringTextComponent(String.format("Divine Level: %s\nRank: %d\nCultivation Base: %.1f/ %.1f (%.2f%%)\nFoundation: %.1f (%.3f)\nEnergy: %.1f %.1f (%.0f%%)",
				stats.getLevel().levelName, stats.getSubLevel() + 1, stats.getBase(), stats.getLevel().getProgressBySubLevel(stats.getSubLevel()),
				(stats.getBase() * 100 / stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getFoundation(), (stats.getFoundation() / stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getEnergy(), cultivation.getMaxDivineEnergy(), (stats.getEnergy() * 100 / cultivation.getMaxDivineEnergy()))
		);
		ctx.getSource().sendFeedback(message, true);

		stats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
		message = new StringTextComponent(String.format("Essence Level: %s\nRank: %d\nCultivation Base: %.1f/ %.1f (%.2f%%)\nFoundation: %.1f (%.3f)\nEnergy: %.1f %.1f (%.0f%%)",
				stats.getLevel().levelName, stats.getSubLevel() + 1, stats.getBase(), stats.getLevel().getProgressBySubLevel(stats.getSubLevel()),
				(stats.getBase() * 100 / stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getFoundation(), (stats.getFoundation() / stats.getLevel().getProgressBySubLevel(stats.getSubLevel())),
				stats.getEnergy(), cultivation.getMaxEssenceEnergy(), (stats.getEnergy() * 100 / cultivation.getMaxEssenceEnergy()))
		);
		ctx.getSource().sendFeedback(message, true);

		return 1;
	}

	private static int setCultivationLevel(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		String systemName = StringArgumentType.getString(ctx, "system");
		String levelName = StringArgumentType.getString(ctx, "level");
		int rank = IntegerArgumentType.getInteger(ctx, "rank");

		ICultivation cultivation = Cultivation.get(target);
		CultivationLevel.System system = CultivationLevel.System.BODY;
		if (systemName.equalsIgnoreCase("divine")) system = CultivationLevel.System.DIVINE;
		else if (systemName.equalsIgnoreCase("essence")) system = CultivationLevel.System.ESSENCE;

		CultivationLevel level = CultivationLevel.getLevelBySystem(system, levelName);

		cultivation.getStatsBySystem(system).setLevel(level);
		cultivation.getStatsBySystem(system).setSubLevel(rank);

		target.sendMessage(new StringTextComponent("Your cultivation level has been set"), Util.DUMMY_UUID);
		ctx.getSource().sendFeedback(new StringTextComponent("Cultivation level was set successfully!"), true);

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

	private static int refillEnergies(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);
		cultivation.getStatsBySystem(CultivationLevel.System.BODY).setEnergy(cultivation.getMaxBodyEnergy());
		cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).setEnergy(cultivation.getMaxDivineEnergy());
		cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).setEnergy(cultivation.getMaxEssenceEnergy());
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new CultivationSyncMessage(cultivation));
		return 1;
	}

}
