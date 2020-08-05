package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
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
public class CultivationCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "cultivation";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/cult [get player]:[set [<player>] <system> <cultivation_level> <rank>]";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("cult");
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		boolean wrongUsage = false;
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("get")) {
				EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[1]);
				if (target == null) {
					TextComponentString text = new TextComponentString("Couldn't find target player!");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				} else {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(target);
					TextComponentString text = new TextComponentString(String.format("Body level: %s\n",
							cultivation.getBodyLevel().getLevelName(cultivation.getBodySubLevel())));
					text.appendText(String.format("Progress: %.0f/%.0f\n", cultivation.getBodyProgress(),
							cultivation.getBodyLevel().getProgressBySubLevel(cultivation.getBodySubLevel())));
					text.appendText(String.format("Divine level: %s\n",
							cultivation.getDivineLevel().getLevelName(cultivation.getDivineSubLevel())));
					text.appendText(String.format("Progress: %.0f/%.0f\n", cultivation.getDivineProgress(),
							cultivation.getDivineLevel().getProgressBySubLevel(cultivation.getDivineSubLevel())));
					text.appendText(String.format("Essence level: %s\n",
							cultivation.getEssenceLevel().getLevelName(cultivation.getEssenceSubLevel())));
					text.appendText(String.format("Progress: %.0f/%.0f\n", cultivation.getEssenceProgress(),
							cultivation.getEssenceLevel().getProgressBySubLevel(cultivation.getEssenceSubLevel())));
					text.appendText(String.format("Energy: %.0f/%.0f (%d%%)\n", cultivation.getEnergy(),
							cultivation.getMaxEnergy(), (int) (cultivation.getEnergy() * 100 / CultivationUtils.getMaxEnergy(target))));
					sender.sendMessage(text);
				}
			} else {
				wrongUsage = true;
			}
		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("set")) {
				if (sender instanceof EntityPlayerMP) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity((EntityPlayerMP) sender);
					if (args[1].equalsIgnoreCase("body")) {
						boolean levelFound = false;
						for (BaseSystemLevel level : BaseSystemLevel.BODY_LEVELS) {
							if (level.levelName.equalsIgnoreCase(args[2])) {
								int rank = 0;
								try {
									rank = parseInt(args[3], 0);
								} catch (NumberInvalidException e) {
									e.printStackTrace();
								}
								cultivation.setBodyLevel(level);
								cultivation.setBodySubLevel(rank);
								TextComponentString text = new TextComponentString("Your level was set to " + level.getLevelName(rank));
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), (EntityPlayerMP) sender);
								levelFound = true;
								break;
							}
						}
						if (!levelFound) {
							TextComponentString text = new TextComponentString("Couldn't find target level!");
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else if (args[1].equalsIgnoreCase("divine")) {
						boolean levelFound = false;
						for (BaseSystemLevel level : BaseSystemLevel.DIVINE_LEVELS) {
							if (level.levelName.equalsIgnoreCase(args[2])) {
								int rank = 0;
								try {
									rank = parseInt(args[3], 0);
								} catch (NumberInvalidException e) {
									e.printStackTrace();
								}
								cultivation.setDivineLevel(level);
								cultivation.setDivineSubLevel(rank);
								TextComponentString text = new TextComponentString("Your level was set to " + level.getLevelName(rank));
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), (EntityPlayerMP) sender);
								levelFound = true;
								break;
							}
						}
						if (!levelFound) {
							TextComponentString text = new TextComponentString("Couldn't find target level!");
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else if (args[1].equalsIgnoreCase("essence")) {
						boolean levelFound = false;
						for (BaseSystemLevel level : BaseSystemLevel.ESSENCE_LEVELS) {
							if (level.levelName.equalsIgnoreCase(args[2])) {
								int rank = 0;
								try {
									rank = parseInt(args[3], 0);
								} catch (NumberInvalidException e) {
									e.printStackTrace();
								}
								cultivation.setEssenceLevel(level);
								cultivation.setEssenceSubLevel(rank);
								TextComponentString text = new TextComponentString("Your level was set to " + level.getLevelName(rank));
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), (EntityPlayerMP) sender);
								levelFound = true;
								break;
							}
						}
						if (!levelFound) {
							TextComponentString text = new TextComponentString("Couldn't find target level!");
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else {
						wrongUsage = true;
					}
				} else {
					TextComponentString text = new TextComponentString("Consoles don't cultivate! o.O");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		} else if (args.length == 5) {
			if (args[0].equalsIgnoreCase("set")) {
				EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[1]);
				if (target == null) {
					TextComponentString text = new TextComponentString("Couldn't find target player!");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				} else {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(target);
					if (args[2].equalsIgnoreCase("body")) {
						boolean levelFound = false;
						for (BaseSystemLevel level : BaseSystemLevel.BODY_LEVELS) {
							if (level.levelName.equalsIgnoreCase(args[3])) {
								int rank = 0;
								try {
									rank = parseInt(args[4], 0);
								} catch (NumberInvalidException e) {
									e.printStackTrace();
								}
								cultivation.setBodyLevel(level);
								cultivation.setBodySubLevel(rank);
								TextComponentString text = new TextComponentString("Your level was set to " + level.getLevelName(rank));
								text.getStyle().setColor(TextFormatting.GRAY);
								target.sendMessage(text);
								text = new TextComponentString("Target player level was set to " + level.getLevelName(rank));
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), target);
								levelFound = true;
								break;
							}
						}
						if (!levelFound) {
							TextComponentString text = new TextComponentString("Couldn't find target level!");
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else if (args[2].equalsIgnoreCase("divine")) {
						boolean levelFound = false;
						for (BaseSystemLevel level : BaseSystemLevel.DIVINE_LEVELS) {
							if (level.levelName.equalsIgnoreCase(args[3])) {
								int rank = 0;
								try {
									rank = parseInt(args[4], 0);
								} catch (NumberInvalidException e) {
									e.printStackTrace();
								}
								cultivation.setDivineLevel(level);
								cultivation.setDivineSubLevel(rank);
								TextComponentString text = new TextComponentString("Your level was set to " + level.getLevelName(rank));
								text.getStyle().setColor(TextFormatting.GRAY);
								target.sendMessage(text);
								text = new TextComponentString("Target player level was set to " + level.getLevelName(rank));
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), target);
								levelFound = true;
								break;
							}
						}
						if (!levelFound) {
							TextComponentString text = new TextComponentString("Couldn't find target level!");
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else if (args[2].equalsIgnoreCase("essence")) {
						boolean levelFound = false;
						for (BaseSystemLevel level : BaseSystemLevel.ESSENCE_LEVELS) {
							if (level.levelName.equalsIgnoreCase(args[3])) {
								int rank = 0;
								try {
									rank = parseInt(args[4], 0);
								} catch (NumberInvalidException e) {
									e.printStackTrace();
								}
								cultivation.setEssenceLevel(level);
								cultivation.setEssenceSubLevel(rank);
								TextComponentString text = new TextComponentString("Your level was set to " + level.getLevelName(rank));
								text.getStyle().setColor(TextFormatting.GRAY);
								target.sendMessage(text);
								text = new TextComponentString("Target player level was set to " + level.getLevelName(rank));
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), target);
								levelFound = true;
								break;
							}
						}
						if (!levelFound) {
							TextComponentString text = new TextComponentString("Couldn't find target level!");
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					} else {
						wrongUsage = true;
					}
				}
			}

		} else {
			wrongUsage = true;
		}
		if (wrongUsage) {
			TextComponentString text = new TextComponentString("Invalid arguments. use " + this.getUsage(sender));
			text.getStyle().setColor(TextFormatting.RED);
			sender.sendMessage(text);
		}
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if ("get".toLowerCase().startsWith(args[0].toLowerCase()))
				completions.add("get");
			if ("set".toLowerCase().startsWith(args[0].toLowerCase()))
				completions.add("set");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				if ("body".toLowerCase().startsWith(args[1].toLowerCase()))
					completions.add("body");
				if ("divine".toLowerCase().startsWith(args[1].toLowerCase()))
					completions.add("divine");
				if ("essence".toLowerCase().startsWith(args[1].toLowerCase()))
					completions.add("essence");
			}
			for (String name : server.getOnlinePlayerNames()) {
				if (name.toLowerCase().startsWith(args[1].toLowerCase()))
					completions.add(name);
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set")) {
				if (args[1].equalsIgnoreCase("body")) { // if you skip name so you target self
					for (BaseSystemLevel level : BaseSystemLevel.BODY_LEVELS) {
						if (level.levelName.toLowerCase().startsWith(args[2].toLowerCase()))
							completions.add(level.levelName);
					}
				} else if (args[1].equalsIgnoreCase("divine")) {
					for (BaseSystemLevel level : BaseSystemLevel.DIVINE_LEVELS) {
						if (level.levelName.toLowerCase().startsWith(args[2].toLowerCase()))
							completions.add(level.levelName);
					}
				} else if (args[1].equalsIgnoreCase("essence")) {
					for (BaseSystemLevel level : BaseSystemLevel.ESSENCE_LEVELS) {
						if (level.levelName.toLowerCase().startsWith(args[2].toLowerCase()))
							completions.add(level.levelName);
					}
				} else { //but if with name then here
					if ("body".toLowerCase().startsWith(args[2].toLowerCase()))
						completions.add("body");
					if ("divine".toLowerCase().startsWith(args[2].toLowerCase()))
						completions.add("divine");
					if ("essence".toLowerCase().startsWith(args[2].toLowerCase()))
						completions.add("essence");
				}
			}
		} else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("set")) {
				if (args[2].equalsIgnoreCase("body")) { // skip name checking
					for (BaseSystemLevel level : BaseSystemLevel.BODY_LEVELS) {
						if (level.levelName.toLowerCase().startsWith(args[3].toLowerCase()))
							completions.add(level.levelName);
					}
				} else if (args[2].equalsIgnoreCase("divine")) {
					for (BaseSystemLevel level : BaseSystemLevel.DIVINE_LEVELS) {
						if (level.levelName.toLowerCase().startsWith(args[3].toLowerCase()))
							completions.add(level.levelName);
					}
				} else if (args[2].equalsIgnoreCase("essence")) {
					for (BaseSystemLevel level : BaseSystemLevel.ESSENCE_LEVELS) {
						if (level.levelName.toLowerCase().startsWith(args[3].toLowerCase()))
							completions.add(level.levelName);
					}
				}
			}
		} //5th arg is a number
		return completions;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
