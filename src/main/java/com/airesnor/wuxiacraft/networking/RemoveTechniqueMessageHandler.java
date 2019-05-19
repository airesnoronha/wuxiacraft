package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RemoveTechniqueMessageHandler implements IMessageHandler {
	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(()->{
				EntityPlayerMP player = ctx.getServerHandler().player;
				ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
				cultTech.remTechnique(((RemoveTechniqueMessage)message).toBeRemoved);
			});
		}
		return null;
	}
}