package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ShrinkEntityItemMessage implements IMessage {
	public String entityItemUID;

	public ShrinkEntityItemMessage(String entityItemUID) {
		this.entityItemUID = entityItemUID;
	}

	public ShrinkEntityItemMessage() {
		this.entityItemUID = "";
	}


	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		byte [] bytes = new byte [length];
		buf.readBytes(bytes, 0, length);
		this.entityItemUID = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte [] bytes = entityItemUID.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
