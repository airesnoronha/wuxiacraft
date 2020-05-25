package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
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
public class AdvCultLevel extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "advcultlevel";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/advcultlevel";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("advcult");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);
			if (!player.world.isRemote) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				if (args.length == 0) {
					CultivationUtils.cultivatorAddProgress(player, cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()), true, true, true);
					NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), player);
					EventHandler.applyModifiers(player);
				} else if (args.length == 1) {
					int levels = Integer.parseInt(args[0], 10);
					for (int i = 0; i < levels; i++) {
						CultivationUtils.cultivatorAddProgress(player, cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()), true, true, true);
					}
					NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), player);
					EventHandler.applyModifiers(player);
				} else {
					TextComponentString text = new TextComponentString("Invalid arguments, use /advcult levels");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		}
		else {
			if(args.length > 0) {
				boolean wrongUsage = false;
				EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
				if(player != null) {
					int levels = 1;
					if (args.length == 2) {
						try {
							levels = parseInt(args[1], 10);
						} catch (NumberFormatException e) {
							TextComponentString text = new TextComponentString("Couldn't recognize number " + args[1]);
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
							wrongUsage = true;
						}
					}
					if (args.length > 2) {
						wrongUsage = true;
					}
					if (!wrongUsage) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						for (int i = 0; i < levels; i++) {
							CultivationUtils.cultivatorAddProgress(player, cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()), true, true, true);
						}
					} else {
						TextComponentString text = new TextComponentString("Invalid arguments, use /advcult <player> [levels]");
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				} else {
					TextComponentString text = new TextComponentString("Couldn't find player " + args[0] + "!");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			} else {
				TextComponentString text = new TextComponentString("Invalid arguments, use /advcult <player> [levels]");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	@ParametersAreNonnullByDefault
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return new ArrayList<>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
