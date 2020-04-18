package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SuppressCultivationMessage implements IMessage {

	public boolean suppress;
	public String sender;

	public SuppressCultivationMessage(boolean suppress, String sender) {
		this.suppress = suppress;
		this.sender = sender;
	}

	public SuppressCultivationMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.suppress = buf.readBoolean();
		int length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.suppress);
		buf.writeInt(this.sender.getBytes().length);
		buf.writeBytes(this.sender.getBytes());
	}
}
