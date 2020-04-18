package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SkillCapMessage;
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
public class SkillsCommand extends CommandBase {
	public SkillsCommand() {
		super();
	}

	@Override
	@Nonnull
	public List<String> getAliases() {
		List<String> aliases = new ArrayList<>();
		aliases.add("skills");
		return aliases;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	@Nonnull
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		List<String> completion = new ArrayList<>();
		if (args.length == 1) {
			if ("add".startsWith(args[0]))
				completion.add("add");
			if ("rem".startsWith(args[0]))
				completion.add("rem");
			if ("reset".startsWith(args[0]))
				completion.add("reset");
			if ("reset_cd".startsWith(args[0]))
				completion.add("reset_cd");
		} else if (args.length == 2) {
			if (args[0].equals("add") || args[0].equals("rem")) {
				for (Skill skill : Skills.SKILLS) {
					if (skill.getUName().startsWith(args[1]))
						completion.add(skill.getUName());
				}
			}
		}
		return completion;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

	@Override
	@Nonnull
	public String getName() {
		return "skills";
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public String getUsage(ICommandSender sender) {
		return "/skills";
	}

	@Override
	@ParametersAreNonnullByDefault
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			if (!player.world.isRemote) {
				if (args.length == 0) {
					if (skillCap.getKnownSkills().isEmpty()) {
						TextComponentString text = new TextComponentString("You don't know any skill yet.");
						sender.sendMessage(text);
					} else {
						TextComponentString text = new TextComponentString("Known skills: ");
						sender.sendMessage(text);
						if (skillCap.getKnownSkills() != null) {
							for (Skill skill : skillCap.getKnownSkills()) {
								text = new TextComponentString("Skill: " + skill.getName());
								sender.sendMessage(text);
							}
						}
					}
				} else if (args.length == 1) {
					if (args[0].equals("reset")) {
						skillCap.getKnownSkills().clear();
						skillCap.getSelectedSkills().clear();
						skillCap.setActiveSkill(-1);
						NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, false, player.getName()), player);
					}
					if (args[0].equals("reset_cd")) {
						skillCap.resetCooldown();
						NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, true, player.getName()), player);
					}
				} else if (args.length == 2) {
					if (args[0].equals("add")) {
						for (Skill skill : Skills.SKILLS) {
							if (args[1].equals(skill.getUName())) {
								skillCap.addSkill(skill);
								TextComponentString text = new TextComponentString("Added skill: " + skill.getName());
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, false, player.getName()), player);
								break;
							}
						}
					}
					if (args[0].equals("rem")) {
						for (Skill skill : Skills.SKILLS) {
							if (args[1].equals(skill.getUName())) {
								if (skillCap.getKnownSkills().contains(skill)) {
									skillCap.removeSkill(skill);
									TextComponentString text = new TextComponentString("Removed skill: " + skill.getName());
									sender.sendMessage(text);
									NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, false, player.getName()), player);
								} else {
									TextComponentString text = new TextComponentString("You don't even know such skill: " + skill.getName());
									sender.sendMessage(text);
								}
								break;
							}
						}
					}
				} else {
					TextComponentString text = new TextComponentString("Invalid arguments, use /skills [add:rem] skill_name");
					text.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(text);
				}
			}
		}
		else {
			if(args.length > 1){
				EntityPlayerMP target = server.getPlayerList().getPlayerByUsername(args[0]);
				if (target == null) {
					String message = String.format("Player %s not found", args[1]);
					TextComponentString text = new TextComponentString(message);
					sender.sendMessage(text);
				} else {
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(target);
					if(args.length == 2 ) {
						if(args[1].equals("reset")) {
							skillCap.getKnownSkills().clear();
							skillCap.getSelectedSkills().clear();
							skillCap.setActiveSkill(-1);
							NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, false, target.getName()), target);
							TextComponentString text = new TextComponentString("Reset all skills of player " + target.getDisplayNameString() );
							sender.sendMessage(text);
						}
						else if(args[1].equals("reset_cd")) {
							skillCap.resetCooldown();
							skillCap.resetCastProgress();
							NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, true, target.getName()), target);
							TextComponentString text = new TextComponentString("Reset cd and cast progress for player " + target.getDisplayNameString() );
							sender.sendMessage(text);
						}
					}
					else if(args.length == 3) {
						if (args[1].equals("add")) {
							for (Skill skill : Skills.SKILLS) {
								if (args[2].equals(skill.getUName())) {
									skillCap.addSkill(skill);
									TextComponentString text = new TextComponentString("Added skill: " + skill.getName() + "to player " + target.getDisplayNameString());
									sender.sendMessage(text);
									NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, false, target.getName()), target);
									break;
								}
							}
						}
						if (args[1].equals("rem")) {
							for (Skill skill : Skills.SKILLS) {
								if (args[2].equals(skill.getUName())) {
									if (skillCap.getKnownSkills().contains(skill)) {
										skillCap.removeSkill(skill);
										TextComponentString text = new TextComponentString("Removed skill: " + skill.getName() + " from " + target.getDisplayNameString());
										sender.sendMessage(text);
										NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap, false, target.getName()), target);
									} else {
										TextComponentString text = new TextComponentString(target.getDisplayName() + " don't even know such skill: " + skill.getName());
										sender.sendMessage(text);
									}
									break;
								}
							}
						}
					}
					else {
						TextComponentString text = new TextComponentString("Invalid arguments, use /skills <player> [add:rem] skill_name");
						text.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(text);
					}
				}
			}
			else {
				TextComponentString text = new TextComponentString("Invalid arguments, use /skills <player> [add:rem] skill_name");
				text.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(text);
			}
		}
	}
}
