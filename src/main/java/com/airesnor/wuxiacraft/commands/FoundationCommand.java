package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.Foundation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class FoundationCommand extends CommandBase {

	@Override
	@Nonnull
	public String getName() {
		return "foundation";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/foundation";
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("foundt");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);
			boolean wrongUsage = true;
			if (!player.world.isRemote) {
				if (args.length == 1) {
					if (args[0].equals("get")) {
						wrongUsage = false;
						IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
						TextComponentString text = new TextComponentString("Player " + player.getName() + " foundation: ");
						sender.sendMessage(text);
						text = new TextComponentString("  Agi: " + foundation.getAgility());
						sender.sendMessage(text);
						text = new TextComponentString("  Con: " + foundation.getConstitution());
						sender.sendMessage(text);
						text = new TextComponentString("  Dex: " + foundation.getDexterity());
						sender.sendMessage(text);
						text = new TextComponentString("  Res: " + foundation.getResistance());
						sender.sendMessage(text);
						text = new TextComponentString("  Spi: " + foundation.getSpirit());
						sender.sendMessage(text);
						text = new TextComponentString("  Str: " + foundation.getStrength());
						sender.sendMessage(text);
					}
					if (args[0].equals("reset")) {
						wrongUsage = false;
						IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
						foundation.copyFrom(new Foundation());
						TextComponentString text = new TextComponentString("Foundation for player "+player.getName()+" was reseted.");
						sender.sendMessage(text);
					}
				} else if (args.length == 2) {
					if (args[1].equals("get")) {
						wrongUsage = false;
						EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
						if (target != null) {
							IFoundation foundation = CultivationUtils.getFoundationFromEntity(target);
							TextComponentString text = new TextComponentString("Player " + target.getName() + " foundation: ");
							sender.sendMessage(text);
							text = new TextComponentString("  Agi: " + foundation.getAgility());
							sender.sendMessage(text);
							text = new TextComponentString("  Con: " + foundation.getConstitution());
							sender.sendMessage(text);
							text = new TextComponentString("  Dex: " + foundation.getDexterity());
							sender.sendMessage(text);
							text = new TextComponentString("  Res: " + foundation.getResistance());
							sender.sendMessage(text);
							text = new TextComponentString("  Spi: " + foundation.getSpirit());
							sender.sendMessage(text);
							text = new TextComponentString("  Str: " + foundation.getStrength());
							sender.sendMessage(text);
						} else {
							TextComponentString text = new TextComponentString("Couldn't find player " + args[0]);
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					}
					if (args[1].equals("reset")) {
						wrongUsage = false;
						EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
						if (target != null) {
							IFoundation foundation = CultivationUtils.getFoundationFromEntity(target);
							foundation.copyFrom(new Foundation());
							TextComponentString text = new TextComponentString("Foundation for player "+target.getName()+" was reseted.");
							sender.sendMessage(text);
						} else {
							TextComponentString text = new TextComponentString("Couldn't find player " + args[0]);
							text.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(text);
						}
					}
				} else if (args.length == 3) {
					if(args[0].equals("set") || args[0].equals("add") || args[0].equals("rem") ) {
						wrongUsage = false;
						int op = 0;
						if(args[0].equals("add")) op = 1;
						else if(args[0].equals("rem")) op = 2;
						int targetAttr = -1;
						if(args[1].equals("agi")) targetAttr = 0;
						else if(args[1].equals("con")) targetAttr = 1;
						else if(args[1].equals("dex")) targetAttr = 2;
						else if(args[1].equals("res")) targetAttr = 3;
						else if(args[1].equals("spi")) targetAttr = 4;
						else if(args[1].equals("str")) targetAttr = 5;
						else if(args[1].equals("all")) targetAttr = 6;
						if(targetAttr == -1) {
							wrongUsage = true;
						} else {
							long value = -1;
							try {
								value = parseLong(args[2]);
							} catch (NumberFormatException e) {
								TextComponentString text = new TextComponentString("Couldn't recognize number " + args[2]);
								text.getStyle().setColor(TextFormatting.RED);
								sender.sendMessage(text);
								wrongUsage = true;
							}
							if(value >= 0) {
								setAttrValue(op, targetAttr, value, player);
							}
						}
					}
				} else if (args.length == 4) {
					EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
					if (target != null) {
						if (args[1].equals("set") || args[1].equals("add") || args[1].equals("rem")) {
							int op = 0;
							if (args[1].equals("add")) op = 1;
							else if (args[1].equals("rem")) op = 2;
							int targetAttr = -1;
							if (args[2].equals("agi")) targetAttr = 0;
							else if (args[2].equals("con")) targetAttr = 1;
							else if (args[2].equals("dex")) targetAttr = 2;
							else if (args[2].equals("res")) targetAttr = 3;
							else if (args[2].equals("spi")) targetAttr = 4;
							else if (args[2].equals("str")) targetAttr = 5;
							else if (args[2].equals("all")) targetAttr = 6;
							if (targetAttr == -1) {
								wrongUsage = true;
							} else {
								long value = -1;
								try {
									value = parseLong(args[3]);
								} catch (NumberFormatException e) {
									TextComponentString text = new TextComponentString("Couldn't recognize number " + args[3]);
									text.getStyle().setColor(TextFormatting.RED);
									sender.sendMessage(text);
									wrongUsage = true;
								}
								if (value >= 0) {
									setAttrValue(op, targetAttr, value, player);
								}
							}
							wrongUsage = false;
						}
					} else {
						TextComponentString text = new TextComponentString("Couldn't find player " + args[0]);
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				}
				if (wrongUsage) {
					TextComponentString text = new TextComponentString("Invalid arguments, use /foundt [player] <get>:<reset>:(<set|add|rem> <attr> <value>)");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		} else {
			if (args.length > 0) {
				boolean wrongUsage = true;
				EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
				if (player != null) {
					if (args.length == 2) {
						if (args[1].equals("get")) {
							wrongUsage = false;
							IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
							TextComponentString text = new TextComponentString("Player " + player.getName() + " foundation: ");
							sender.sendMessage(text);
							text = new TextComponentString("  Agi: " + foundation.getAgility());
							sender.sendMessage(text);
							text = new TextComponentString("  Con: " + foundation.getConstitution());
							sender.sendMessage(text);
							text = new TextComponentString("  Dex: " + foundation.getDexterity());
							sender.sendMessage(text);
							text = new TextComponentString("  Res: " + foundation.getResistance());
							sender.sendMessage(text);
							text = new TextComponentString("  Spi: " + foundation.getSpirit());
							sender.sendMessage(text);
							text = new TextComponentString("  Str: " + foundation.getStrength());
							sender.sendMessage(text);
						}
						if (args[1].equals("get")) {
							wrongUsage = false;
							IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
							foundation.copyFrom(new Foundation());
							TextComponentString text = new TextComponentString("Foundation for player "+player.getName()+" was reseted.");
							sender.sendMessage(text);
						}
					}else if (args.length == 4) {
						if(args[1].equals("set") || args[1].equals("add") || args[1].equals("rem") ) {
							wrongUsage = false;
							int op = 0;
							if(args[1].equals("add")) op = 1;
							else if(args[1].equals("rem")) op = 2;
							int targetAttr = -1;
							if(args[2].equals("agi")) targetAttr = 0;
							else if(args[2].equals("con")) targetAttr = 1;
							else if(args[2].equals("dex")) targetAttr = 2;
							else if(args[2].equals("res")) targetAttr = 3;
							else if(args[2].equals("spi")) targetAttr = 4;
							else if(args[2].equals("str")) targetAttr = 5;
							else if(args[2].equals("all")) targetAttr = 6;
							if(targetAttr == -1) {
								wrongUsage = true;
							} else {
								long value =-1;
								try {
									value = parseLong(args[2]);
								} catch (NumberFormatException e) {
									TextComponentString text = new TextComponentString("Couldn't recognize number " + args[2]);
									text.getStyle().setColor(TextFormatting.RED);
									sender.sendMessage(text);
									wrongUsage = true;
								}
								if(value >= 0) {
									setAttrValue(op, targetAttr, value, player);
								}
							}
						}
					}
					if (wrongUsage)  {
						TextComponentString text = new TextComponentString("Invalid arguments, use /foundt [player] <get>:<reset>:(<set|add|rem> <attr> <value>)");
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				} else {
					TextComponentString text = new TextComponentString("Couldn't find player " + args[0] + "!");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			} else {
				TextComponentString text = new TextComponentString("Invalid arguments, use /foundt [player] <get>:<reset>:(<set|add|rem> <attr> <value>)");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	@ParametersAreNonnullByDefault
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return new ArrayList<>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	private void setAttrValue(int op, int targetAttr, long value, EntityPlayerMP target) {
		IFoundation foundation = CultivationUtils.getFoundationFromEntity(target);
		long newValue = 0;
		switch (targetAttr) {
			case 0:
				newValue = foundation.getAgility();
				if(op == 0) newValue = value;
				else if(op == 1) newValue += value;
				else if(op == 2) newValue = Math.max(0, newValue - value);
				foundation.setAgility(newValue);
				break;
			case 1:
				newValue = foundation.getConstitution();
				if(op == 0) newValue = value;
				else if(op == 1) newValue += value;
				else if(op == 2) newValue = Math.max(0, newValue - value);
				foundation.setConstitution(newValue);
				break;
			case 2:
				newValue = foundation.getDexterity();
				if(op == 0) newValue = value;
				else if(op == 1) newValue += value;
				else if(op == 2) newValue = Math.max(0, newValue - value);
				foundation.setDexterity(newValue);
				break;
			case 3:
				newValue = foundation.getResistance();
				if(op == 0) newValue = value;
				else if(op == 1) newValue += value;
				else if(op == 2) newValue = Math.max(0, newValue - value);
				foundation.setResistance(newValue);
				break;
			case 4:
				newValue = foundation.getSpirit();
				if(op == 0) newValue = value;
				else if(op == 1) newValue += value;
				else if(op == 2) newValue = Math.max(0, newValue - value);
				foundation.setSpirit(newValue);
				break;
			case 5:
				newValue = foundation.getStrength();
				if(op == 0) newValue = value;
				else if(op == 1) newValue += value;
				else if(op == 2) newValue = Math.max(0, newValue - value);
				foundation.setStrength(newValue);
				break;
			case 6:
				setAttrValue(op, 0, value, target);
				setAttrValue(op, 1, value, target);
				setAttrValue(op, 2, value, target);
				setAttrValue(op, 3, value, target);
				setAttrValue(op, 4, value, target);
				setAttrValue(op, 5, value, target);
				break;
		}
	}
}
