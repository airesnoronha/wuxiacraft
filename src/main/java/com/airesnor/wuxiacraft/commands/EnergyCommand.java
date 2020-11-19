package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.EntityLivingBase;
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
public class EnergyCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "energy";
	}

	@Override
	@ParametersAreNonnullByDefault
	@Nonnull
	public String getUsage(ICommandSender sender) {
		return "/energy set <player> <amount>";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		boolean wrongUsage = false;
		if (args.length == 3) {
			EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
			if (targetPlayer != null) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(targetPlayer);
				if (args[0].equalsIgnoreCase("set")) {
					double amount = 0;
					if (args[2].equalsIgnoreCase("max")) {
						//cultivation.setEnergy(cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()));
						amount = CultivationUtils.getMaxEnergy(targetPlayer);
					} else {
						try {
							amount = parseDouble(args[2], 0);
						} catch (NumberInvalidException e) {
							WuxiaCraft.logger.error("Couldn't parse number, setting 0 for execution sake ...");
						}
					}
					cultivation.setEnergy(amount);
					NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), targetPlayer);
					TextComponentString text = new TextComponentString("Your energy has been altered ... ");
					text.getStyle().setColor(TextFormatting.GRAY);
					sender.sendMessage(text);
				} else {
					wrongUsage = true;
				}
			} else {
				TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
			if (wrongUsage) {
				TextComponentString text = new TextComponentString("Invalid arguments, use /energy set <player> <amount>");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		}
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if ("set".startsWith(args[0]))
				completions.add("set");
		} else if (args.length == 2) {
			for (String player : server.getPlayerList().getOnlinePlayerNames()) {
				if (player.toLowerCase().startsWith(args[1].toLowerCase())) {
					completions.add(player);
				}
			}
		} else if (args.length == 3) {
			if ("max".startsWith(args[2]))
				completions.add("max");
		}
		return completions;
	}
}
