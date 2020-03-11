package com.airesnor.wuxiacraft.commands;

import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SkillCapMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SkillsCommand extends CommandBase {
	public SkillsCommand() {
		super();
	}

	@Override
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
	public String getName() {
		return "skills";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/skills";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = getCommandSenderAsPlayer(sender);
			if (!player.world.isRemote) {
				if (args.length == 0) {
					ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
					if (skillCap == null) {
						TextComponentString text = new TextComponentString("There is no way to learn skills yet.");
						sender.sendMessage(text);
					} else {
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
					}
				} else if (args.length == 1) {
					ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
					if (args[0].equals("reset")) {
						skillCap.getKnownSkills().clear();
						skillCap.getSelectedSkills().clear();
						skillCap.setActiveSkill(-1);
						NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), player);
					}
					if (args[0].equals("reset_cd")) {
						skillCap.resetCooldown();
						NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), player);
					}
				} else if (args.length == 2) {
					ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
					if (args[0].equals("add")) {
						for (Skill skill : Skills.SKILLS) {
							if (args[1].equals(skill.getUName())) {
								skillCap.addSkill(skill);
								TextComponentString text = new TextComponentString("Added skill: " + skill.getName());
								sender.sendMessage(text);
								NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), player);
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
									NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), player);
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
	}
}
