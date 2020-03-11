package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RespondCultivationLevelMessageHandler implements IMessageHandler {
	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			if (message instanceof RespondCultivationLevelMessage) {
				RespondCultivationLevelMessage rclm = (RespondCultivationLevelMessage) message;
				String responderName = ctx.getServerHandler().player.getName();
				String askerName = rclm.askerName;
				rclm = new RespondCultivationLevelMessage(rclm.responderLevel, rclm.responderSubLevel, responderName);
				EntityPlayerMP asker = (EntityPlayerMP) ctx.getServerHandler().player.getServerWorld().getPlayerEntityByName(askerName);
				NetworkWrapper.INSTANCE.sendTo(rclm, asker);
			}
		}
		if (ctx.side == Side.CLIENT) {
			return handleMessageClient(message, ctx);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	private IMessage handleMessageClient(IMessage message, MessageContext ctx) {
		if (message instanceof RespondCultivationLevelMessage) {
			RespondCultivationLevelMessage rclm = (RespondCultivationLevelMessage) message;
			ICultivation cultivation = new Cultivation();
			cultivation.setCurrentLevel(rclm.responderLevel);
			cultivation.setCurrentSubLevel(rclm.responderSubLevel);
			String name = rclm.askerName;
			RendererHandler.knownCultivations.put(name, cultivation);
		}
		return null;
	}
}
