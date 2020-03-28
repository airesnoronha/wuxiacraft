package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkillCapMessageHandler implements IMessageHandler<SkillCapMessage, IMessage> {
	@Override
	public IMessage onMessage(SkillCapMessage message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			handleClientMessage(message, ctx);
		} else if (ctx.side == Side.SERVER) {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					EntityPlayer player = ctx.getServerHandler().player;
					ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
					if (skillCap != null) {
						skillCap.getKnownSkills().clear();
						for (Skill skill : message.skillCap.getKnownSkills()) {
							skillCap.addSkill(skill);
						}
						skillCap.resetCooldown();
						skillCap.resetCastProgress();
						skillCap.setCooldown(message.skillCap.getCooldown());
						skillCap.setCastProgress(message.skillCap.getCastProgress());
						skillCap.getSelectedSkills().clear();
						for (Skill skill : message.skillCap.getSelectedSkills()) {
							skillCap.addSelectedSkill(skill);
						}
						skillCap.setActiveSkill(message.skillCap.getActiveSkill());
					}
				});
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	private void handleClientMessage(SkillCapMessage message, MessageContext ctx) {
		if (message instanceof SkillCapMessage) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				EntityPlayer player = Minecraft.getMinecraft().player;
				ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
				if (skillCap != null) {
					skillCap.getKnownSkills().clear();
					for (Skill skill : message.skillCap.getKnownSkills()) {
						skillCap.addSkill(skill);
					}
					skillCap.setCooldown(message.skillCap.getCooldown());
					skillCap.setCastProgress(message.skillCap.getCastProgress());
					skillCap.getSelectedSkills().clear();
					for (Skill skill : message.skillCap.getSelectedSkills()) {
						skillCap.addSelectedSkill(skill);
					}
					skillCap.setActiveSkill(message.skillCap.getActiveSkill());
				}
			});
		}
	}
}
