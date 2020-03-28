package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CastSkillMessageHandler implements IMessageHandler<CastSkillMessage, IMessage> {

	@Override
	public IMessage onMessage(CastSkillMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				ISkillCap skillCap = ctx.getServerHandler().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
				if (message.casting)
					skillCap.setCasting(true);
				else {
					skillCap.setDoneCasting(true);
					skillCap.setCasting(false);
				}
			});
		}
		return null;
	}
}
