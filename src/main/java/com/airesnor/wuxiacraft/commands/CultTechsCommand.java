package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class CultTechsCommand extends CommandBase {
	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("culttech");
		return aliases;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public String getName() {
		return "cultivationtechniques";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/culttech";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayerMP) {
			if(!((EntityPlayerMP) sender).world.isRemote) {
				ICultTech cultTech= ((EntityPlayerMP) sender).getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
				if(cultTech!=null) {
					if(args.length == 0) {
						if(cultTech.getKnownTechniques().size() > 0) {
							String message = "You know " + cultTech.getKnownTechniques().size() + " techniques.";
							sender.sendMessage(new TextComponentString(message));
							for(KnownTechnique t : cultTech.getKnownTechniques()) {
								message = String.format("%s %.0f", t.getTechnique().getName(), t.getProgress());
								sender.sendMessage(new TextComponentString(message));
							}
						}
						else {
							String message = "You don't know any techniques yet";
							sender.sendMessage(new TextComponentString(message));
						}
					}
					else if(args.length == 1) {
						if(args[0].equals("reset")) {
							cultTech.getKnownTechniques().clear();
							String message = "You forgot all techniques.";
							sender.sendMessage(new TextComponentString(message));
						}
					}
				}
			}
		}
	}
}
