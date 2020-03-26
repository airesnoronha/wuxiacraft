package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SuppressCultivationMessage implements IMessage {

	public boolean suppress;

	public SuppressCultivationMessage(boolean suppress) {
		this.suppress = suppress;
	}

	public SuppressCultivationMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.suppress = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.suppress);
	}
}
