package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
}
