package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class EnergyMessageHandler implements IMessageHandler<EnergyMessage, IMessage> {
	@Override
	public IMessage onMessage(EnergyMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
				switch (message.op) {
					case 0:
						cultivation.addEnergy(message.amount);
						break;
					case 1:
						cultivation.remEnergy(message.amount);
						break;
					case 2:
						cultivation.setEnergy(message.amount);
						break;
				}
			});
		}
		return null;
	}
}
