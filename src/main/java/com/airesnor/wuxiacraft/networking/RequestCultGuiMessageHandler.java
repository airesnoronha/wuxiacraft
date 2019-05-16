package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestCultGuiMessageHandler implements IMessageHandler {
	@Override
	public IMessage onMessage(IMessage message, MessageContext ctx) {
		if(ctx.side == Side.SERVER) {
			if(((RequestCultGuiMessage)message).requested) {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(()-> {
					BlockPos pos = ctx.getServerHandler().player.getPosition();
					ctx.getServerHandler().player.openGui(WuxiaCraft.instance, GuiHandler.CULTIVATION_GUI_ID,ctx.getServerHandler().player.world,pos.getX(), pos.getY(), pos.getZ());
				});
			}
		}
		return null;
	}
}
