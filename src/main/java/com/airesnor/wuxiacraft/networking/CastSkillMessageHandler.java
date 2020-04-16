package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CastSkillMessageHandler implements IMessageHandler<CastSkillMessage, IMessage> {

	/**
	 * Server must know this only because of storage
	 * Cooldown is set by ActivateSkillMessageHandler
	 */
	@Override
	public IMessage onMessage(CastSkillMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
				if (message.casting)
					skillCap.setCasting(true);
				else {
					skillCap.setCasting(false);
					skillCap.resetCastProgress();
				}
			});
		}
		return null;
	}
}
