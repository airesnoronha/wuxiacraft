package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ProgressMessageHandler implements IMessageHandler<ProgressMessage, IMessage> {
	@Override
	public IMessage onMessage(ProgressMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
				switch (message.op) {
					case 0:
						CultivationUtils.cultivatorAddProgress(player, cultivation, message.amount);
						break;
					case 1:
						cultivation.addProgress(-message.amount);
						break;
					case 2:
						cultivation.setProgress(message.amount);
				}
			});
		}
		return null;
	}
}
