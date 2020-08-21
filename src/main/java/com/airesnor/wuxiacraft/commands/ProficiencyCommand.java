package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.networking.CultTechMessage;
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
public class ProficiencyCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "proficiency";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/proficiency [get <player>]:[set:add <player> <system> <amount>]";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		//noinspection SpellCheckingInspection
		aliases.add("profi");
		return aliases;
	}
	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) sender;
			if (!playerMP.world.isRemote) {
				boolean wrongUsage = true;
				if(args.length == 2) {
					if("get".equalsIgnoreCase(args[0])) {
						EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
						if (targetPlayer != null) {
							ICultTech cultTech = CultivationUtils.getCultTechFromEntity(targetPlayer);
							if(cultTech.getBodyTechnique() != null) {
								TextComponentString text = new TextComponentString(String.format("Body proficiency: %.2f", cultTech.getBodyTechnique().getProficiency()));
								sender.sendMessage(text);
							}
							if(cultTech.getDivineTechnique() != null) {
								TextComponentString text = new TextComponentString(String.format("Divine proficiency: %.2f", cultTech.getDivineTechnique().getProficiency()));
								sender.sendMessage(text);
							}
							if(cultTech.getEssenceTechnique() != null) {
								TextComponentString text = new TextComponentString(String.format("Essence proficiency: %.2f", cultTech.getEssenceTechnique().getProficiency()));
								sender.sendMessage(text);
							}
							wrongUsage = false;
						} else {
							TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					}
				}
				else if (args.length == 4) {
					EntityPlayerMP targetPlayer = server.getPlayerList().getPlayerByUsername(args[1]);
					if (targetPlayer != null) {
						ICultTech cultTech = CultivationUtils.getCultTechFromEntity(targetPlayer);
						try {
							double amount = Double.parseDouble(args[3]);
							TextComponentString text;
							switch(args[2]) {
								case "body":
									if (args[0].equalsIgnoreCase("set")) {
										if(cultTech.getBodyTechnique() != null) {
											cultTech.setBodyTechnique(new KnownTechnique(cultTech.getBodyTechnique().getTechnique(), amount));
										}
										wrongUsage = false;
									} else if (args[0].equalsIgnoreCase("add")) {
										cultTech.progress(amount, Cultivation.System.BODY);
										wrongUsage = false;
									}
									if(!wrongUsage) {
										text = new TextComponentString("Your proficiency has been modified ...");
										text.getStyle().setColor(TextFormatting.GRAY);
										targetPlayer.sendMessage(text);
										NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), targetPlayer);
									}
									break;
								case "divine":
									if (args[0].equalsIgnoreCase("set")) {
										if(cultTech.getDivineTechnique() != null) {
											cultTech.setDivineTechnique(new KnownTechnique(cultTech.getDivineTechnique().getTechnique(), amount));
										}
										wrongUsage = false;
									} else if (args[0].equalsIgnoreCase("add")) {
										cultTech.progress(amount, Cultivation.System.DIVINE);
										wrongUsage = false;
									}
									if(!wrongUsage) {
										text = new TextComponentString("Your proficiency has been modified ...");
										text.getStyle().setColor(TextFormatting.GRAY);
										targetPlayer.sendMessage(text);
										NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), targetPlayer);
									}
									break;
								case "essence":
									if (args[0].equalsIgnoreCase("set")) {
										if(cultTech.getEssenceTechnique() != null) {
											cultTech.setEssenceTechnique(new KnownTechnique(cultTech.getEssenceTechnique().getTechnique(), amount));
										}
										wrongUsage = false;
									} else if (args[0].equalsIgnoreCase("add")) {
										cultTech.progress(amount, Cultivation.System.ESSENCE);
										wrongUsage = false;
									}
									if(!wrongUsage) {
										text = new TextComponentString("Your proficiency has been modified ...");
										text.getStyle().setColor(TextFormatting.GRAY);
										targetPlayer.sendMessage(text);
										NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), targetPlayer);
									}
									break;
								case "three":
									if (args[0].equalsIgnoreCase("set")) {
										if(cultTech.getBodyTechnique() != null) {
											cultTech.setBodyTechnique(new KnownTechnique(cultTech.getBodyTechnique().getTechnique(), amount));
										}
										if(cultTech.getDivineTechnique() != null) {
											cultTech.setDivineTechnique(new KnownTechnique(cultTech.getDivineTechnique().getTechnique(), amount));
										}
										if(cultTech.getEssenceTechnique() != null) {
											cultTech.setEssenceTechnique(new KnownTechnique(cultTech.getEssenceTechnique().getTechnique(), amount));
										}
										wrongUsage = false;
									} else if (args[0].equalsIgnoreCase("add")) {
										cultTech.progress(amount, Cultivation.System.BODY);
										cultTech.progress(amount, Cultivation.System.DIVINE);
										cultTech.progress(amount, Cultivation.System.ESSENCE);
										wrongUsage = false;
									}
									if(!wrongUsage) {
										text = new TextComponentString("Your proficiency has been modified ...");
										text.getStyle().setColor(TextFormatting.GRAY);
										targetPlayer.sendMessage(text);
										NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), targetPlayer);
									}
									break;
							}
						} catch (NumberFormatException e) {
							TextComponentString text = new TextComponentString("Couldn't read number: " + args[3]);
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else {
						TextComponentString text = new TextComponentString("Couldn't find player " + args[1]);
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
						wrongUsage = true;
					}
				}
				if (wrongUsage) {
					TextComponentString text = new TextComponentString("Invalid arguments, use " + this.getUsage(sender));
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		} else throw new CommandException("Not used correctly!");
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if ("add".toLowerCase().startsWith(args[0].toLowerCase())) completions.add("add");
			if ("set".toLowerCase().startsWith(args[0].toLowerCase())) completions.add("set");
			if ("get".toLowerCase().startsWith(args[0].toLowerCase())) completions.add("get");
		} else if (args.length == 2) {
			for(String name : server.getOnlinePlayerNames()) {
				if(name.toLowerCase().startsWith(args[1].toLowerCase())) {
					completions.add(name);
				}
			}
		} else if(args.length == 3) {
			if ("body".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("body");
			else if ("divine".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("divine");
			else if ("essence".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("essence");
			else if ("three".toLowerCase().startsWith(args[2].toLowerCase())) completions.add("three");
		}
		return completions;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
