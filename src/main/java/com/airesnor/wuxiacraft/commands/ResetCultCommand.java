package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ResetCultCommand extends CommandBase {

	@Override
	public String getName() {
		return "resetcult";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/resetcult";
	}

	@Override
	public List<String> getAliases() {
		List<String> list = new ArrayList<>();
		list.add("resetcult");
		return list;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);
			if (!player.world.isRemote) {
				if(args.length == 0) {
					ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
					cultivation.setProgress(0);
					cultivation.setEnergy(0);
					cultivation.setCurrentSubLevel(0);
					cultivation.setCurrentLevel(CultivationLevel.BODY_REFINEMENT);
					NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int)cultivation.getCurrentProgress(), (int)cultivation.getEnergy()), (EntityPlayerMP) player);
					EventHandler.applyModifiers(player, cultivation);
				}
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
