package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CultTechsCommand extends CommandBase {
	@Override
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("culttech");
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayerMP) {
			if (!((EntityPlayerMP) sender).world.isRemote) {
				ICultTech cultTech = CultivationUtils.getCultTechFromEntity((EntityLivingBase) sender);
				if (args.length == 0) {
					if (cultTech.getKnownTechniques().size() > 0) {
						String message = "You know " + cultTech.getKnownTechniques().size() + " techniques.";
						sender.sendMessage(new TextComponentString(message));
						for (KnownTechnique t : cultTech.getKnownTechniques()) {
							message = String.format("%s %.0f", t.getTechnique().getName(), t.getProgress());
							sender.sendMessage(new TextComponentString(message));
						}
					} else {
						String message = "You don't know any techniques yet";
						sender.sendMessage(new TextComponentString(message));
					}
				} else if (args.length == 1) {
					if (args[0].equals("reset")) {
						cultTech.getKnownTechniques().clear();
						String message = "You forgot all techniques.";
						sender.sendMessage(new TextComponentString(message));
					}
				}
			}
		}
	}
}
