package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestCultGuiMessage implements IMessage {
	public boolean requested;

	public RequestCultGuiMessage(boolean requested) {
		this.requested = requested;
	}

	public RequestCultGuiMessage() {
		this.requested = false;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.requested = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.requested);
	}

	public static class Handler implements IMessageHandler<RequestCultGuiMessage, IMessage> {

		@Override
		public IMessage onMessage(RequestCultGuiMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				if (message.requested) {
					ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
						BlockPos pos = ctx.getServerHandler().player.getPosition();
						ctx.getServerHandler().player.openGui(WuxiaCraft.instance, GuiHandler.CULTIVATION_GUI_ID, ctx.getServerHandler().player.world, pos.getX(), pos.getY(), pos.getZ());
					});
				}
			}
			return null;
		}
	}

}
