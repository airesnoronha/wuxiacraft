package wuxiacraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.init.WuxiaSkills;
import wuxiacraft.network.CultivationSyncMessage;
import wuxiacraft.network.WuxiaPacketHandler;

import java.util.List;

public class SkillCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("wuxiaskill")
				.then(Commands.literal("add").then(Commands.argument("target", EntityArgument.player())
						.then(Commands.argument("skillName", StringArgumentType.string())
						.executes(SkillCommand::addSkill))))
				.then(Commands.literal("reset").then(Commands.argument("target", EntityArgument.player())
						.executes(SkillCommand::resetSkill)))
				.then(Commands.literal("remove").then(Commands.argument("target", EntityArgument.player())
						.then(Commands.argument("skillName", StringArgumentType.string())
						.executes(SkillCommand::removeSkill))))
				.then(Commands.literal("get").then(Commands.argument("target", EntityArgument.player())
						.executes(SkillCommand::getSkills)))
				.then(Commands.literal("selected").then(Commands.argument("target", EntityArgument.player())
						.executes(SkillCommand::getSelectedSkills)))
				.then(Commands.literal("select").then(Commands.argument("target", EntityArgument.player())
						.then(Commands.argument("index", IntegerArgumentType.integer())
						.executes(SkillCommand::selectSkill))))
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
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new CultivationSyncMessage(cultivation));
		return 1;
	}

	public static int resetSkill(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		ICultivation cultivation = Cultivation.get(target);

		cultivation.resetKnownSkills();
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new CultivationSyncMessage(cultivation));
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
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new CultivationSyncMessage(cultivation));
		return 1;
	}

	public static int getSkills(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");

		ICultivation cultivation = Cultivation.get(target);
		List<Skill> knownSkills = cultivation.getAllKnownSkills();
		ctx.getSource().sendFeedback(new StringTextComponent("Skills list:"), true);
		for(Skill skill : knownSkills) {
			ctx.getSource().sendFeedback(new StringTextComponent(knownSkills.indexOf(skill) + ": " + skill.name), true);
		}
		return 1;
	}

	public static int getSelectedSkills(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");

		ICultivation cultivation = Cultivation.get(target);
		List<Skill> knownSkills = cultivation.getAllKnownSkills();
		List<Integer> selectedSkills = cultivation.getSelectedSkills();
		ctx.getSource().sendFeedback(new StringTextComponent("Skills list:"), true);
		for(Integer index : selectedSkills) {
			ctx.getSource().sendFeedback(new StringTextComponent(selectedSkills.indexOf(index) + ": " + knownSkills.get(index).name), true);
		}
		return 1;
	}

	public static int selectSkill(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
		int index = IntegerArgumentType.getInteger(ctx, "index");

		ICultivation cultivation = Cultivation.get(target);
		List<Skill> knownSkills = cultivation.getAllKnownSkills();

		if(index < knownSkills.size()) {
			if(!cultivation.getSelectedSkills().contains(index)) {
				cultivation.getSelectedSkills().add(index);
				ctx.getSource().sendFeedback(new StringTextComponent("Skill selected!"), true);
			} else {
				ctx.getSource().sendErrorMessage(new StringTextComponent("Skill was already selected"));
			}
		} else {
			ctx.getSource().sendErrorMessage(new StringTextComponent("Index was out of bounds!"));
		}
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new CultivationSyncMessage(cultivation));

		return 1;
	}
}
