package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SelectSkillMessageHandler implements IMessageHandler {

	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if (message instanceof SelectSkillMessage) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				ISkillCap skillCap = ctx.getServerHandler().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
				selectSkill(skillCap, ((SelectSkillMessage) message).selectSkill);
			});
		}
		return null;
	}

	public static void selectSkill(ISkillCap skillCap, int i) {
		skillCap.setActiveSkill(Math.min(skillCap.getSelectedSkills().size() - 1, Math.max(0, i)));
		if (skillCap.getSelectedSkills().size() == 0) {
			skillCap.setActiveSkill(-1);
		}
	}
}
