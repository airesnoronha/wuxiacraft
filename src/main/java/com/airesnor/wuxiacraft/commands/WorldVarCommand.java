package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.world.data.WorldVariables;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WorldVarCommand extends CommandBase {

	@Override
	public String getName() {
		return "worldvar";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/worldvar [dim] <var_name> [value]";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return super.getRequiredPermissionLevel();
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (int dim : DimensionManager.getIDs()) {
				if (("" + dim).startsWith(args[0])) {
					completions.add("" + dim);
				}
			}
			if ("tribMult".toLowerCase().startsWith(args[0].toLowerCase())) {
				completions.add("tribMult");
			} else if ("maxSpeed".toLowerCase().startsWith(args[0].toLowerCase())) {
				completions.add("maxSpeed");
			}
		} else if (args.length == 2) {
			if ("tribMult".toLowerCase().startsWith(args[0].toLowerCase())) {
				completions.add("tribMult");
			} else if ("maxSpeed".toLowerCase().startsWith(args[0].toLowerCase())) {
				completions.add("maxSpeed");
			}
		}
		return completions;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		boolean wrongUsage = false;
		if (args.length == 1) {
			if ("tribMult".equalsIgnoreCase(args[0])) {
				int dim = 0;
				if (sender instanceof EntityPlayerMP) {
					dim = ((EntityPlayerMP) sender).world.provider.getDimension();
				}
				TextComponentString text = new TextComponentString(String.format("Tribulation Multipliers: %d", WorldVariables.get(DimensionManager.getWorld(dim)).getTribulationMultiplier()));
				sender.sendMessage(text);
			}
			if ("maxSpeed".equalsIgnoreCase(args[0])) {
				TextComponentString text = new TextComponentString(String.format("Max Server Speed: %.1f", WuxiaCraftConfig.maxServerSpeed));
				sender.sendMessage(text);
			}
		} else if (args.length == 2) {
			if ("tribMult".equalsIgnoreCase(args[0])) {
				int dim = 0;
				if (sender instanceof EntityPlayerMP) {
					dim = ((EntityPlayerMP) sender).world.provider.getDimension();
				}
				int multiplier = 10;
				try {
					multiplier = parseInt(args[1]);
				} catch (NumberFormatException e) {
					WuxiaCraft.logger.error("Couldn't parse argument multiplier number");
				}
				WorldVariables.get(DimensionManager.getWorld(dim)).setTribulationMultiplier(multiplier);
				TextComponentString text = new TextComponentString(String.format("Setting Tribulation Multipliers to : %d", WorldVariables.get(DimensionManager.getWorld(dim)).getTribulationMultiplier()));
				sender.sendMessage(text);
			} else if ("tribMult".equalsIgnoreCase(args[1])) {
				int dim = 0;
				try {
					dim = parseInt(args[0]);
				} catch (NumberFormatException e) {
					WuxiaCraft.logger.error("Couldn't parse argument number");
				}
				TextComponentString text = new TextComponentString(String.format("Tribulation Multipliers: %d", WorldVariables.get(DimensionManager.getWorld(dim)).getTribulationMultiplier()));
				sender.sendMessage(text);
			} else if("maxSpeed".equalsIgnoreCase(args[0])) {
				float limiter = 10f;
				try {
					limiter = (float)parseDouble(args[1]);
				} catch (NumberFormatException e) {
					WuxiaCraft.logger.error("Couldn't parse argument multiplier number");
				}
				WuxiaCraftConfig.maxServerSpeed = limiter;
				WuxiaCraftConfig.syncFromFields();
				TextComponentString text = new TextComponentString(String.format("Setting Max Server Speed to: %.1f", WuxiaCraftConfig.maxServerSpeed));
				sender.sendMessage(text);
			}
		} else if (args.length == 3) {
			if ("tribMult".equalsIgnoreCase(args[1])) {
				int dim = 0;
				try {
					dim = parseInt(args[0]);
				} catch (NumberFormatException e) {
					WuxiaCraft.logger.error("Couldn't parse argument world number");
				}
				int multiplier = 10;
				try {
					multiplier = parseInt(args[2]);
				} catch (NumberFormatException e) {
					WuxiaCraft.logger.error("Couldn't parse argument multiplier number");
				}
				WorldVariables.get(DimensionManager.getWorld(dim)).setTribulationMultiplier(multiplier);
				TextComponentString text = new TextComponentString(String.format("Setting Tribulation Multipliers to : %d", WorldVariables.get(DimensionManager.getWorld(dim)).getTribulationMultiplier()));
				sender.sendMessage(text);
			}
		} else {
			wrongUsage = true;
		}
		if (wrongUsage) {
			TextComponentString text = new TextComponentString(TextFormatting.RED + "Invalid args, usage: " + this.getUsage(sender));
			sender.sendMessage(text);
		}

	}
}
