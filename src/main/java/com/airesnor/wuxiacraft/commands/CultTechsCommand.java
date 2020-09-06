package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
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
		return "/culttech <player> get:(set <culttech>):(rem <system>):reset";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		boolean wrongUsage = false;
		if (args.length == 2 || args.length == 3) {
			if (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("rem")) {
				EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
				if (target != null) {
					ICultTech cultTech = CultivationUtils.getCultTechFromEntity(target);
					if (args[1].equalsIgnoreCase("get") && args.length == 2) {
						sender.sendMessage(new TextComponentString("Techniques from player " + target.getName() + ":"));
						if (cultTech.getBodyTechnique() != null) {
							sender.sendMessage(new TextComponentString(String.format("Body: %s (%s)", cultTech.getBodyTechnique().getTechnique().getName(), cultTech.getBodyTechnique().getCurrentCheckpoint())));
						} else {
							sender.sendMessage(new TextComponentString("Don't know any body techniques"));
						}
						if (cultTech.getDivineTechnique() != null) {
							sender.sendMessage(new TextComponentString(String.format("Divine: %s (%s)", cultTech.getDivineTechnique().getTechnique().getName(), cultTech.getDivineTechnique().getCurrentCheckpoint())));
						} else {
							sender.sendMessage(new TextComponentString("Don't know any techniques techniques"));
						}
						if (cultTech.getEssenceTechnique() != null) {
							sender.sendMessage(new TextComponentString(String.format("Essence: %s (%s)", cultTech.getEssenceTechnique().getTechnique().getName(), cultTech.getEssenceTechnique().getCurrentCheckpoint())));
						} else {
							sender.sendMessage(new TextComponentString("Don't know any essence techniques"));
						}
					} else if (args[1].equalsIgnoreCase("set") && args.length == 3) {
						Technique targetTechnique = Techniques.getTechniqueByUName(args[2]);
						if (targetTechnique != null) {
							Cultivation.System system = targetTechnique.getSystem();
							switch (system) {
								case BODY:
									cultTech.setBodyTechnique(new KnownTechnique(targetTechnique, 0));
									break;
								case DIVINE:
									cultTech.setDivineTechnique(new KnownTechnique(targetTechnique, 0));
									break;
								case ESSENCE:
									cultTech.setEssenceTechnique(new KnownTechnique(targetTechnique, 0));
									break;
							}
							sender.sendMessage(new TextComponentString("Cultivation technique set with success"));
						} else {
							sender.sendMessage(new TextComponentString(TextFormatting.RED + "Couldn't find target technique"));
						}
					} else if (args[1].equalsIgnoreCase("rem") && args.length == 3) {
						if ("body".equalsIgnoreCase(args[2])) {
							cultTech.setBodyTechnique(null);
							sender.sendMessage(new TextComponentString("Removed body technique from target player!"));
						} else if ("divine".equalsIgnoreCase(args[2])) {
							cultTech.setDivineTechnique(null);
							sender.sendMessage(new TextComponentString("Removed divine technique from target player!"));
						} else if ("essence".equalsIgnoreCase(args[2])) {
							cultTech.setEssenceTechnique(null);
							sender.sendMessage(new TextComponentString("Removed essence technique from target player!"));
						} else {
							sender.sendMessage(new TextComponentString(TextFormatting.RED + "Couldn't find target system!"));
						}
					} else {
						wrongUsage = true;
					}
				} else {
					sender.sendMessage(new TextComponentString(TextFormatting.RED + "Couldn't find target player!"));
				}
			}
		} else {
			wrongUsage = true;
		}
		if (wrongUsage) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Wrong usage, please use " + this.getUsage(sender)));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			for (String name : server.getOnlinePlayerNames()) {
				if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
					completions.add(name);
				}
			}
		} else if (args.length == 2) {
			String[] tabs = new String[]{"get", "set", "rem"};
			for (String tab : tabs) {
				if (tab.toLowerCase().startsWith(args[1].toLowerCase())) {
					completions.add(tab);
				}
			}
		} else if (args.length == 3 && args[2].equalsIgnoreCase("get")) {
			for (Technique technique : Techniques.TECHNIQUES) {
				if (technique.getUName().toLowerCase().startsWith(args[2])) {
					completions.add(technique.getUName());
				}
			}
		} else if (args.length == 3 && args[2].equalsIgnoreCase("set")) {
			String[] systems = new String[]{"body", "divine", "essence"};
			for (String system : systems) {
				if (system.toLowerCase().startsWith(args[1].toLowerCase())) {
					completions.add(system);
				}
			}
		}
		return completions;
	}
}
