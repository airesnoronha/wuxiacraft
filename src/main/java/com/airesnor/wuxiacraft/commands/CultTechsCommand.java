package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.networking.CultTechMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
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
	
	@SuppressWarnings("SpellCheckingInspection")
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

	@SuppressWarnings("SpellCheckingInspection")
	@Override
	public String getName() {
		return "cultivationtechniques";
	}

	@SuppressWarnings("SpellCheckingInspection")
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
					if (cultTech.getBodyTechnique() != null) {
						String message = String.format("%s %.2f", cultTech.getBodyTechnique().getTechnique().getName(),
								cultTech.getBodyTechnique().getProficiency());
						sender.sendMessage(new TextComponentString(message));
					} else {
						String message = "You don't know any techniques yet";
						sender.sendMessage(new TextComponentString(message));
					}
					if (cultTech.getEssenceTechnique() != null) {
						String message = String.format("%s %.2f", cultTech.getEssenceTechnique().getTechnique().getName(),
								cultTech.getEssenceTechnique().getProficiency());
						sender.sendMessage(new TextComponentString(message));
					} else {
						String message = "You don't know any techniques yet";
						sender.sendMessage(new TextComponentString(message));
					}
					if (cultTech.getDivineTechnique() != null) {
						String message = String.format("%s %.2f", cultTech.getDivineTechnique().getTechnique().getName(),
								cultTech.getDivineTechnique().getProficiency());
						sender.sendMessage(new TextComponentString(message));
					} else {
						String message = "You don't know any techniques yet";
						sender.sendMessage(new TextComponentString(message));
					}
				} else if (args.length == 1) {
					if (args[0].equals("reset")) {
						cultTech.copyFrom(new CultTech());
						String message = "You forgot all techniques.";
						sender.sendMessage(new TextComponentString(message));
						NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP) sender);
					}
				}
			}
		}
	}
}
