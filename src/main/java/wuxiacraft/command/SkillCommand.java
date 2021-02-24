package wuxiacraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.init.WuxiaSkills;

public class SkillCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("wuxiaskill")
				.then(Commands.literal("add").then(Commands.argument("target", EntityArgument.player())
						.then(Commands.argument("skillName", StringArgumentType.string()))
						.executes(SkillCommand::addSkill)))
				.then(Commands.literal("reset").then(Commands.argument("target", EntityArgument.player())
						.executes(SkillCommand::resetSkill)))
				.then(Commands.literal("remove").then(Commands.argument("target", EntityArgument.player())
						.then(Commands.argument("skillName", StringArgumentType.string()))
						.executes(SkillCommand::removeSkill)))
		);
	}

	public static int addSkill(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		String skillName = StringArgumentType.getString(ctx, "skillName");
		Skill skill = WuxiaSkills.getSkillByName(skillName);

		ICultivation cultivation = Cultivation.get(target);

		if (skill != null) {
			cultivation.addKnownSkill(skill);
		}
		return 1;
	}

	public static int resetSkill(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);

		cultivation.resetKnownSkills();
		return 1;
	}

	public static int removeSkill(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		String skillName = StringArgumentType.getString(ctx, "skillName");
		Skill skill = WuxiaSkills.getSkillByName(skillName);

		ICultivation cultivation = Cultivation.get(target);

		if (skill != null) {
			cultivation.removeKnownSkill(skill);
		}
		return 1;
	}
}
