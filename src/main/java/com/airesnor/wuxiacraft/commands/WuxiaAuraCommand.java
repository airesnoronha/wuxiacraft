package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.aura.Auras;
import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.UnifiedCapabilitySyncMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WuxiaAuraCommand extends CommandBase {

	@Override
	public String getName() {
		return "wuxiaaura";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/wuxiaaura <player> [(add:rem <auraname>) : clear]";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("aura");
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		boolean wrongUsage = false;
		if (args.length == 2) {
			EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
			if (target != null) {
				if ("clear".equalsIgnoreCase(args[1])) {
					IAuraCap auraCap = CultivationUtils.getAuraFromEntity(target);
					auraCap.clearAuraLocations();
					NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(CultivationUtils.getCultivationFromEntity(target),
							CultivationUtils.getCultTechFromEntity(target), CultivationUtils.getSkillCapFromEntity(target),
							CultivationUtils.getAuraFromEntity(target), false), target);
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Cleared all auras for target player!"));
				} else {
					wrongUsage = true;
				}
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Couldn't find target player!"));
			}
		} else if (args.length == 3) {
			EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
			if (target != null) {
				ResourceLocation location = new ResourceLocation(args[2]);
				if(Auras.AURAS.containsKey(location)) {
					if ("add".equalsIgnoreCase(args[1])) {
						IAuraCap auraCap = CultivationUtils.getAuraFromEntity(target);
						auraCap.addAuraLocation(location);
						NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(CultivationUtils.getCultivationFromEntity(target),
								CultivationUtils.getCultTechFromEntity(target), CultivationUtils.getSkillCapFromEntity(target),
								CultivationUtils.getAuraFromEntity(target), false), target);
						sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Aura added with success!"));
					} else if ("rem".equalsIgnoreCase(args[1])) {
						IAuraCap auraCap = CultivationUtils.getAuraFromEntity(target);
						auraCap.remAuraLocation(location);
						NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(CultivationUtils.getCultivationFromEntity(target),
								CultivationUtils.getCultTechFromEntity(target), CultivationUtils.getSkillCapFromEntity(target),
								CultivationUtils.getAuraFromEntity(target), false), target);
						sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Aura removed with success!"));
					} else {
						wrongUsage = true;
					}
				} else {
					sender.sendMessage(new TextComponentString(TextFormatting.RED + "Couldn't find target aura!"));
				}
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "Couldn't find target player!"));
			}
		} else {
			wrongUsage = true;
		}
		if (wrongUsage) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Invalid arguments, use " + this.getUsage(sender)));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (String player : server.getPlayerList().getOnlinePlayerNames()) {
				if (player.toLowerCase().startsWith(args[0].toLowerCase())) {
					completions.add(player);
				}
			}
		} else if (args.length == 2) {
			if ("clear".toLowerCase().startsWith(args[1].toLowerCase())) {
				completions.add("clear");
			}
			if ("add".toLowerCase().startsWith(args[1].toLowerCase())) {
				completions.add("add");
			}
			if ("rem".toLowerCase().startsWith(args[1].toLowerCase())) {
				completions.add("rem");
			}
		}else if (args.length == 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("rem"))) {
			for (ResourceLocation location : Auras.AURAS.keySet()) {
				if (location.toString().toLowerCase().startsWith(args[2].toLowerCase())) {
					completions.add(location.toString());
				}
			}
		}
		return completions;
	}
}
