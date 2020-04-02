package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ActivatePartialSkillMessageHandler implements IMessageHandler<ActivatePartialSkillMessage, IMessage> {

	@Override
	public IMessage onMessage(ActivatePartialSkillMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				if ("barrageMinorBeam".equals(message.skillName)) {
					Skills.BARRAGE_MINOR_BEAM.activate(ctx.getServerHandler().player);
					CultivationUtils.getCultivationFromEntity(ctx.getServerHandler().player).remEnergy(message.energy);
				}
			});
		}
		return null;
	}
}
