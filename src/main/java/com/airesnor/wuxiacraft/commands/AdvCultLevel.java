package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
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
		return "/advcultlevel [<player>] [<system>] [<count>]";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("advcult");
		return aliases;
	}

	@Override
	@ParametersAreNonnullByDefault
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		switch (args.length) {
			case 1:
				for (String name : server.getOnlinePlayerNames()) {
					if(name.startsWith(args[0])) {
						completions.add(name);
					}
				}
			case 2:
				String [] systems = new String [] { "body", "divine", "essence", "three"};
				for(String system : systems) {
					if(system.startsWith(args[0])) {
						completions.add(system);
					}
				}
				break;
		}
		return completions;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		boolean wrongUsage = false;
		boolean execute = true;
		boolean consoleError = false;
		EntityPlayerMP target = null;
		int levelCount = 1;
		String system = "";
		if(args.length == 0) {
			system = "three";
			if (sender instanceof EntityPlayerMP) {
				try {
					target = getCommandSenderAsPlayer(sender);
				} catch (PlayerNotFoundException e) {
					TextComponentString text = new TextComponentString("You ain't a player, what are you?");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
					execute = false;
				}
			} else {
				consoleError = true;
				execute = false;
			}
		}
		else if (args.length == 1) {
			if (sender instanceof EntityPlayerMP) {
				if ("body".equalsIgnoreCase(args[0])
						|| "divine".equalsIgnoreCase(args[0]) ||
						"essence".equalsIgnoreCase(args[0]) ||
						"three".equalsIgnoreCase(args[0])) {
					system = args[0].toLowerCase();
					try {
						target = getCommandSenderAsPlayer(sender);
					} catch (PlayerNotFoundException e) {
						TextComponentString text = new TextComponentString("You ain't a player, what are you?");
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
						execute = false;
					}
				} else {
					system = "three".toLowerCase();
					try {
						levelCount = parseInt(args[0], 0);
					} catch (NumberInvalidException e) {
						WuxiaCraft.logger.error("Couldn't parse number, assuming 1 for execution sake");
					}
				}
			} else {
				consoleError = true;
				execute = false;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("body") ||
					args[0].equalsIgnoreCase("divine") ||
					args[0].equalsIgnoreCase("essence") ||
					args[0].equalsIgnoreCase("three")) {
				system = args[0].toLowerCase();
				if (sender instanceof EntityPlayerMP) {
					try {
						target = getCommandSenderAsPlayer(sender);
					} catch (PlayerNotFoundException e) {
						TextComponentString text = new TextComponentString("You ain't a player, what are you?");
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
						execute = false;
					}
					try {
						levelCount = parseInt(args[1], 0);
					} catch (NumberInvalidException e) {
						WuxiaCraft.logger.error("Couldn't parse number, assuming 1 for execution sake");
					}
				} else {
					execute = false;
					consoleError = true;
				}
			} else {
				for (String name : server.getOnlinePlayerNames()) {
					if (args[0].equalsIgnoreCase(name)) {
						target = server.getPlayerList().getPlayerByUsername(name);
						break;
					}
				}
				if (target == null) {
					TextComponentString text = new TextComponentString("Couldn't find target player");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
					execute = false;
				} else {
					if (args[1].equalsIgnoreCase("body") ||
							args[1].equalsIgnoreCase("divine") ||
							args[1].equalsIgnoreCase("essence") ||
							args[1].equalsIgnoreCase("three")) {
						system = args[1].toLowerCase();

					} else {
						system = "three".toLowerCase();
						try {
							levelCount = parseInt(args[1], 0);
						} catch (NumberInvalidException e) {
							WuxiaCraft.logger.error("Couldn't parse number, assuming 1 for execution sake");
						}
					}
				}
			}

		} else if (args.length == 3) {
			target = server.getPlayerList().getPlayerByUsername(args[0]);
			if(target != null) {
				if (args[1].equalsIgnoreCase("body") ||
						args[1].equalsIgnoreCase("divine") ||
						args[1].equalsIgnoreCase("essence") ||
						args[1].equalsIgnoreCase("three")) {
					system = args[1].toLowerCase();
					try {
						levelCount = parseInt(args[2], 0);
					} catch (NumberInvalidException e) {
						WuxiaCraft.logger.error("Couldn't parse number, assuming 1 for execution sake");
					}
				} else {
					execute = false;
					wrongUsage = true;
				}
			} else {
				TextComponentString text = new TextComponentString("Couldn't find target player");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
				execute = false;
			}
		}
		if (execute) {
			if (target != null) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(target);
				switch (system) {
					case "body":
						for (int i = 0; i < levelCount; i++) {
							double amount = cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel());
							try {
								cultivation.addBodyProgress(amount, true);
							} catch (Cultivation.RequiresTribulation trib) {
								CultivationUtils.callTribulation(target, trib.tribulationStrength, trib.system,
										trib.level, trib.sublevel);
							}
						}
						break;
					case "divine":
						for (int i = 0; i < levelCount; i++) {
							double amount = cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel());
							try {
								cultivation.addDivineProgress(amount, true);
							} catch (Cultivation.RequiresTribulation trib) {
								CultivationUtils.callTribulation(target, trib.tribulationStrength, trib.system,
										trib.level, trib.sublevel);
							}
						}
						break;
					case "essence":
						for (int i = 0; i < levelCount; i++) {
							double amount = cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel());
							try {
								cultivation.addEssenceProgress(amount, true);
							} catch (Cultivation.RequiresTribulation trib) {
								CultivationUtils.callTribulation(target, trib.tribulationStrength, trib.system,
										trib.level, trib.sublevel);
							}
						}
						break;
					case "three":
						for (int i = 0; i < levelCount; i++) {
							double amount = cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel());
							try {
								cultivation.addBodyProgress(amount, true);
							} catch (Cultivation.RequiresTribulation trib) {
								CultivationUtils.callTribulation(target, trib.tribulationStrength, trib.system,
										trib.level, trib.sublevel);
							}
							amount = cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel());
							try {
								cultivation.addDivineProgress(amount, true);
							} catch (Cultivation.RequiresTribulation trib) {
								CultivationUtils.callTribulation(target, trib.tribulationStrength, trib.system,
										trib.level, trib.sublevel);
							}
							amount = cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel());
							try {
								cultivation.addEssenceProgress(amount, true);
							} catch (Cultivation.RequiresTribulation trib) {
								CultivationUtils.callTribulation(target, trib.tribulationStrength, trib.system,
										trib.level, trib.sublevel);
							}
						}
						break;
				}
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), target);
				TextComponentString text = new TextComponentString("You gained some levels, cherish!");
				text.getStyle().setColor(TextFormatting.GRAY);
				target.sendMessage(text);
				text = new TextComponentString("Levels added successfully!");
				sender.sendMessage(text);
			} else {
				TextComponentString text = new TextComponentString("There is no target player!");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		} else if (consoleError) {
			TextComponentString text = new TextComponentString("Consoles can't cultivate (yet) ...");
			text.getStyle().setColor(TextFormatting.RED);
			sender.sendMessage(text);
		} else if (wrongUsage) {
			TextComponentString text = new TextComponentString("Wrong usage, use " + getUsage(sender));
			text.getStyle().setColor(TextFormatting.RED);
			sender.sendMessage(text);
		}
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
