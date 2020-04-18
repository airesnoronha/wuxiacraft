package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProgressMessage implements IMessage {

	public int op;
	public double amount;
	public boolean allowBreakTrough;
	public boolean ignoreBottleneck;
	public String sender;

	@SuppressWarnings("unused")
	public ProgressMessage() {
		this.op = 0;
		this.amount = 0;
		allowBreakTrough = false;
		ignoreBottleneck = false;
		this.sender = "";
	}

	public ProgressMessage(int op, double amount, boolean allowBreakTrough, boolean ignoreBottleneck, String sender) {
		this.op = op;
		this.amount = amount;
		this.allowBreakTrough = allowBreakTrough;
		this.ignoreBottleneck = ignoreBottleneck;
		this.sender = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.op = buf.readInt();
		this.amount = buf.readDouble();
		this.allowBreakTrough = buf.readBoolean();
		this.ignoreBottleneck = buf.readBoolean();
		int length = buf.readInt();
		byte [] bytes = new byte [length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.op);
		buf.writeDouble(this.amount);
		buf.writeBoolean(this.allowBreakTrough);
		buf.writeBoolean(this.ignoreBottleneck);
		buf.writeInt(this.sender.getBytes().length);
		buf.writeBytes(this.sender.getBytes());
	}
}
