package com.lazydragonstudios.wuxiacraft.command;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.SystemContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.lazydragonstudios.wuxiacraft.networking.CultivationSyncMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.EnumArgument;

import java.math.BigDecimal;

public class CultivationCommand {

	//this class is looking like flutter

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("cult")
				.requires(commandSourceStack -> commandSourceStack.hasPermission(2))
				.then(Commands.argument("target", EntityArgument.player())
						.then(Commands.literal("get")
								.executes(CultivationCommand::getCultivation)
						)
						.then(Commands.literal("reset")
								.then(Commands.literal("confirm")
										.then(Commands.literal("yes")
												.executes(CultivationCommand::resetCultivation)
										)
										.then(Commands.literal("no")
												.executes(ctx -> 1)
										)
								)
						)
						.then(Commands.literal("set")
								.then(Commands.argument("system", EnumArgument.enumArgument(System.class))
										.then(Commands.argument("stage", StageArgument.id())
												.executes(CultivationCommand::setCultivation)
										)
								)
						)
						.then(Commands.literal("tech")
								.then(Commands.literal("add")
										.then(Commands.argument("aspect", AspectArgument.id())
												.then(Commands.argument("amount", IntegerArgumentType.integer())
														.executes(CultivationCommand::addTechniqueAspectProficiency)
												)
										)
								)
								.then(Commands.literal("remove")
										.then(Commands.argument("aspect", AspectArgument.id())
												.then(Commands.argument("amount", IntegerArgumentType.integer())
														.executes(CultivationCommand::removeTechniqueAspectProficiency)
												)
										)
								)
								.then(Commands.literal("clear")
										.then(Commands.argument("aspect", AspectArgument.id())
												.executes(CultivationCommand::clearTechniqueAspectProficiency)
										)
								)
								.then(Commands.literal("reset")
										.then(Commands.literal("confirm")
												.then(Commands.literal("yes")
														.executes(CultivationCommand::resetTechniqueAspects)
												)
												.then(Commands.literal("no")
														.executes(ctx -> 1)
												)
										)
								)
						)
				)
		);
	}

	/**
	 * A helper procedure to simplify the line actually because this might get repeated a lot in this file
	 *
	 * @param player the player to be synchronized
	 */
	public static void syncClientCultivation(ServerPlayer player) {
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CultivationSyncMessage(Cultivation.get(player)));
	}


	public static int getCultivation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");
		for (var system : System.values()) {
			String systemName = system.name();
			message.append(systemName).append(" stats: ").append("\n");
			SystemContainer systemData = cultivation.getSystemData(system);
			message.append("Realm: ").append(systemData.getRealm().name).append("\n");
			message.append("Stage: ").append(new TranslatableComponent(systemData.currentStage.getNamespace() + ".stage." + systemData.currentStage.getPath())).append("\n");
			message.append("CultivationBase: ").append(String.format("%.1f", systemData.getStat(PlayerSystemStat.CULTIVATION_BASE))).append("\n");
			message.append("Energy: ").append(String.format("%.1f", systemData.getStat(PlayerSystemStat.ENERGY))).append("\n\n");
		}
		ctx.getSource().sendSuccess(message, true);
		return 1;
	}

	public static int resetCultivation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);
		cultivation.deserialize(new Cultivation().serialize());
		TextComponent message = new TextComponent("Cultivation successfully reset, I feel sorry about that dude!");
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int setCultivation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		System system = ctx.getArgument("system", System.class);
		ResourceLocation stageLocation = StageArgument.getStageLocation(ctx, "stage");
		ICultivation cultivation = Cultivation.get(target);
		var systemData = cultivation.getSystemData(system);
		systemData.currentStage = stageLocation;
		systemData.calculateStats(cultivation);
		Component message = new TranslatableComponent("wuxiacraft.command.set_stage",
				new TranslatableComponent(stageLocation.getNamespace() + ".stage." + stageLocation.getPath()));
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int addTechniqueAspectProficiency(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ResourceLocation aspectLocation = AspectArgument.getAspectLocation(ctx, "aspect");
		int amount = IntegerArgumentType.getInteger(ctx, "amount");

		ICultivation cultivation = Cultivation.get(target);

		cultivation.getAspects().addAspectProficiency(aspectLocation, new BigDecimal(amount), cultivation);
		var message = new TranslatableComponent("wuxiacraft.command.add_proficiency",
				new TranslatableComponent(aspectLocation.getNamespace() + "aspect" + aspectLocation.getPath() + ".name"));
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int removeTechniqueAspectProficiency(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ResourceLocation aspectLocation = AspectArgument.getAspectLocation(ctx, "aspect");
		int amount = IntegerArgumentType.getInteger(ctx, "amount");

		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");

		cultivation.getAspects().subtractAspectProficiency(aspectLocation, new BigDecimal(amount));
		message.append("Successfully subtracted aspect proficiency to target player");
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int clearTechniqueAspectProficiency(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
		ResourceLocation aspectLocation = AspectArgument.getAspectLocation(ctx, "aspect");

		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");

		cultivation.getAspects().setAspectAndProficiency(aspectLocation, BigDecimal.ZERO);
		message.append("Successfully cleared aspect proficiency from target player");
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

	public static int resetTechniqueAspects(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer target = EntityArgument.getPlayer(ctx, "target");

		ICultivation cultivation = Cultivation.get(target);
		TextComponent message = new TextComponent("");
		var aspects = new AspectContainer();
		cultivation.getAspects().deserialize(aspects.serialize(), cultivation);
		message.append("Successfully cleared all technique aspects from target player");
		ctx.getSource().sendSuccess(message, true);
		syncClientCultivation(target);
		return 1;
	}

}
