package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SuppressCultivationMessageHandler implements IMessageHandler<SuppressCultivationMessage, IMessage> {

	@Override
	public IMessage onMessage(SuppressCultivationMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				EntityPlayerMP player = ctx.getServerHandler().player;
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				cultivation.setSuppress(message.suppress);
			});
		}
		return null;
	}
}
