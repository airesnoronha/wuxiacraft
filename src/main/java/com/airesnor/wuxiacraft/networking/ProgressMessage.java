package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProgressMessage implements IMessage {

	public int op;
	public double amount;

	public ProgressMessage() {
		this.op = 0;
		this.amount = 0;
	}

	public ProgressMessage(int op, double amount) {
		this.op = op;
		this.amount = amount;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.op = buf.readInt();
		this.amount = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.op);
		buf.writeDouble(this.amount);
	}
}
