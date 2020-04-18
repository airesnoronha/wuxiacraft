package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class EnergyMessage implements IMessage {

	public int op; // 0 -- add, 2 --rem, 3--set
	public float amount;
	public String sender;

	public EnergyMessage(int op, float amount, String sender) {
		this.op = op;
		this.amount = amount;
		this.sender = sender;
	}

	public EnergyMessage() {
		this.op = 0;
		this.amount = 0;
		this.sender = "";
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.op = buf.readInt();
		this.amount = buf.readFloat();
		int length = buf.readInt();
		byte [] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.op);
		buf.writeFloat(this.amount);
		buf.writeInt(this.sender.getBytes().length);
		buf.writeBytes(this.sender.getBytes());
	}
}
