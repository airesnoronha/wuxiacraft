package com.airesnor.wuxiacraft.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class HealthCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "health";
	}

	@Override
	@Nonnull
	public String getUsage(ICommandSender sender) {
		return "/health [set:heal] <player> <amount> or /health get <player>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) sender;
			if (!playerMP.world.isRemote) {
				boolean wrongUsage = true;
				if (args.length == 2) {
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					if (targetPlayer != null) {
						if (args[0].equalsIgnoreCase("get")) {
							float maxHealth = targetPlayer.getMaxHealth();
							float currentHealth = targetPlayer.getHealth();
							if (targetPlayer.getUniqueID().equals(playerMP.getUniqueID())) {
								TextComponentString text = new TextComponentString("Your Health:");
								sender.sendMessage(text);
								text = new TextComponentString(String.format("%.1f / %.1f", currentHealth, maxHealth));
								sender.sendMessage(text);
								wrongUsage = false;
							} else {
								TextComponentString text = new TextComponentString(String.format("%s's Health:", targetPlayer.getName()));
								sender.sendMessage(text);
								text = new TextComponentString(String.format("%.1f / %.1f", currentHealth, maxHealth));
								sender.sendMessage(text);
								wrongUsage = false;
							}
						}
					} else {
						TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
						wrongUsage = true;
					}
				} else if (args.length == 3) {
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					float health = 0;
					if (targetPlayer != null) {
						if (args[2].equalsIgnoreCase("max")) {
							try {
								health = targetPlayer.getMaxHealth();
							} catch (NullPointerException e) {
								TextComponentString text = new TextComponentString("Invalid Max Health");
								sender.sendMessage(text);
							}
						} else {
							health = Float.parseFloat(args[2]);
						}
						if (args[0].equalsIgnoreCase("set")) {
							targetPlayer.setHealth(health);
							TextComponentString text = new TextComponentString(String.format("%s's health set to %.1f", targetPlayer.getName(), health));
							sender.sendMessage(text);
							wrongUsage = false;
						} else if (args[0].equalsIgnoreCase("heal")) {
							targetPlayer.heal(health);
							TextComponentString text = new TextComponentString(String.format("%s's health healed by %.1f", targetPlayer.getName(), health));
							sender.sendMessage(text);
							wrongUsage = false;
						}
					} else {
						TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
						wrongUsage = true;
					}
				}
				if (wrongUsage) {
					TextComponentString text = new TextComponentString("Invalid arguments, use /health [set:heal] <player> <amount> or /health get <player>");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		} else throw new CommandException("Not used correctly!");
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if (args[0].toLowerCase().startsWith(args[0]))
				completions.add("set");
			if (args[0].toLowerCase().startsWith(args[0]))
				completions.add("heal");
			if (args[0].toLowerCase().startsWith(args[0]))
				completions.add("get");
		}else if(args.length == 2) {
			for(String player : server.getPlayerList().getOnlinePlayerNames()) {
				if(player.toLowerCase().startsWith(args[1])) {
					completions.add(player);
				}
			}
		}
		return completions;
	}
}
