package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RespondCultivationLevelMessageHandler implements IMessageHandler<RespondCultivationLevelMessage, IMessage> {
	@Override
	public IMessage onMessage(RespondCultivationLevelMessage message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			return handleMessageClient(message, ctx);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	private IMessage handleMessageClient(RespondCultivationLevelMessage message, MessageContext ctx) {
		ICultivation cultivation = new Cultivation();
		cultivation.setCurrentLevel(message.responderLevel);
		cultivation.setCurrentSubLevel(message.responderSubLevel);
		String name = message.responderName;
		RendererHandler.knownCultivations.put(name, cultivation);
		return null;
	}
}
