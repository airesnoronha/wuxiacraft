package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.SystemContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.networking.CultivationSyncMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;

import java.math.BigDecimal;

public class StatCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("stat")
				.requires(commandSourceStack -> commandSourceStack.hasPermission(2))
				.then(Commands.argument("target", EntityArgument.player())
						.then(Commands.literal("get")
								.executes(StatCommand::getStats)
						)
						.then(Commands.literal("set")
								.then(Commands.argument("stat", EnumArgument.enumArgument(PlayerStat.class))
										.then(Commands.argument("amount", IntegerArgumentType.integer())
												.executes(StatCommand::setStat)
										)
								)
								.then(Commands.argument("element", ElementArgument.id())
										.then(Commands.argument("stat", EnumArgument.enumArgument(PlayerElementalStat.class))
												.then(Commands.argument("amount", IntegerArgumentType.integer())
														.executes(StatCommand::setElementalStat)
												)
										)
								)
								.then(Commands.argument("system", EnumArgument.enumArgument(System.class))
										.then(Commands.argument("stat", EnumArgument.enumArgument(PlayerSystemStat.class))
												.then(Commands.argument("amount", IntegerArgumentType.integer())
														.executes(StatCommand::setSystemStat)
												)
										)
								)
						)
				)
		);
	}

	public static void syncClientCultivation(ServerPlayer player) {
		ICultivation cultivation = Cultivation.get(player);
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CultivationSyncMessage(cultivation));
		cultivation.calculateStats();
	}

	public static int getStats(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");
		message.append("Player Stats: ").append("\n");
		for (var stat : PlayerStat.values()) {
			String statName = stat.name();
			message.append(statName).append(": ").append(String.format("%.1f", cultivation.getStat(stat))).append("\n");
		}
		ctx.getSource().sendSuccess(message, true);
		return 1;
	}

	public static int setStat(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		int amount = IntegerArgumentType.getInteger(ctx, "amount");
		var playerStat = ctx.getArgument("stat", PlayerStat.class);

		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");
		cultivation.setStat(playerStat, BigDecimal.valueOf(amount));

		message.append("Successfully set the target's " + playerStat.name() + " stat.");
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int setElementalStat(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		var element = ElementArgument.getAspectLocation(ctx, "element");
		var stat = ctx.getArgument("stat", PlayerElementalStat.class);
		int amount = IntegerArgumentType.getInteger(ctx, "amount");

		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");
		cultivation.setStat(element, stat, BigDecimal.valueOf(amount));

		message.append(new TranslatableComponent("wuxiacraft.command.elemental_stat",
				stat.name(),
				new TranslatableComponent(element.getNamespace() + ".element." + element.getPath())));
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int setSystemStat(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		System system = ctx.getArgument("system", System.class);
		PlayerSystemStat stat = ctx.getArgument("stat", PlayerSystemStat.class);
		int amount = IntegerArgumentType.getInteger(ctx, "amount");

		ICultivation cultivation = Cultivation.get(target);
		SystemContainer systemData = cultivation.getSystemData(system);
		TextComponent message = new TextComponent("");

		systemData.setStat(stat, BigDecimal.valueOf(amount));

		message.append("Successfully set the target's " + stat + " stat.");
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}
}
