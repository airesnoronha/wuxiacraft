package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AdvCultLevel extends CommandBase {

	@Override
	public String getName() {
		return "advcultlevel";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/advcultlevel";
	}

	@Override
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
					CultivationUtils.cultivatorAddProgress(player, cultivation, cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));
					NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int) cultivation.getCurrentProgress(), (int) cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), player);
					EventHandler.applyModifiers(player, cultivation);
				} else if (args.length == 1) {
					int levels = Integer.parseInt(args[0], 10);
					for (int i = 0; i < levels; i++) {
						CultivationUtils.cultivatorAddProgress(player, cultivation, cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel()));
					}
					NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int) cultivation.getCurrentProgress(), (int) cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), player);
					EventHandler.applyModifiers(player, cultivation);
				} else {
					TextComponentString text = new TextComponentString("Invalid arguments, use /advcult levels");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
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
