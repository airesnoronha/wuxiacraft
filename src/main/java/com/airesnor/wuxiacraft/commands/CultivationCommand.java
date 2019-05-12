package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CultivationCommand extends CommandBase {

	@Override
	public String getName() {
		return "cultivation";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/cultivation or /cultivation target";
	}

	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("cult");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayerMP) {
			EntityPlayerMP player =  getCommandSenderAsPlayer(sender);
			if(!player.world.isRemote)
			{
				if(args.length == 0) {
					ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
					String message = cultivation != null? String.format("You are at %s", cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel())) : "You don't cultivate yet";
					TextComponentString text = new TextComponentString(message);
					sender.sendMessage(text);
					message = cultivation != null? String.format("Progress: %d/%d", (int)cultivation.getCurrentProgress(),(int)cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel())) : "You don't cultivate yet";
					text = new TextComponentString(message);
					sender.sendMessage(text);
					message = cultivation != null? String.format("Energy: %d/%d", (int)cultivation.getEnergy(),(int)cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel())) : "You don't cultivate yet";
					text = new TextComponentString(message);
					sender.sendMessage(text);
					//message = cultivation != null? String.format("Player->%s Minecraft->%s", player.getUniqueID().toString(), Minecraft.getMinecraft().player.getUniqueID().toString()) : "You don't cultivate yet";
					//text = new TextComponentString(message);// + Minecraft.getMinecraft().player.getDisplayNameString());
					//sender.sendMessage(text);
				}
				else if (args.length == 1) {
					EntityPlayer target = server.getPlayerList().getPlayerByUsername(args[0]);
					if(target == null) {
						String message = String.format("Player %s not found", args[0]);
						TextComponentString text = new TextComponentString(message);
						sender.sendMessage(text);
					} else {
						ICultivation cultivation = target.getCapability(CultivationProvider.CULTIVATION_CAP, null);
						String message = String.format("%s is at %s",target.getDisplayNameString(), cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel()));
						TextComponentString text = new TextComponentString(message);
						sender.sendMessage(text);
					}
				}
				else {
					TextComponentString text = new TextComponentString("Invalid arguments, use /cult target_player");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
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
