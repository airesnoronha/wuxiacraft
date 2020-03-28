package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
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

public class ResetCultCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "resetcult";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/resetcult";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> list = new ArrayList<>();
		list.add("resetcult");
		return list;
	}

	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			if(sender instanceof EntityPlayerMP) {
				EntityPlayerMP player = getCommandSenderAsPlayer(sender);
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				cultivation.setProgress(0);
				cultivation.setEnergy(0);
				cultivation.setCurrentSubLevel(0);
				cultivation.setCurrentLevel(CultivationLevel.BODY_REFINEMENT);
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int) cultivation.getCurrentProgress(), (int) cultivation.getEnergy(), cultivation.getPillCooldown(), false), player);
				EventHandler.applyModifiers(player, cultivation);
			}
			else {
				TextComponentString text = new TextComponentString("You ain't a player my friend.");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		}
		if(args.length == 1) {
			EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
			if(player != null) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				cultivation.setProgress(0);
				cultivation.setEnergy(0);
				cultivation.setCurrentSubLevel(0);
				cultivation.setCurrentLevel(CultivationLevel.BODY_REFINEMENT);
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int) cultivation.getCurrentProgress(), (int) cultivation.getEnergy(), cultivation.getPillCooldown(), false), player);
				EventHandler.applyModifiers(player, cultivation);
			} else {
				TextComponentString text = new TextComponentString("Couldn't find player " + args[0]);
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
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return new ArrayList<>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
