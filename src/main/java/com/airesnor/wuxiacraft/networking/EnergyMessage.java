package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class EnergyMessage implements IMessage {

	public int op; // 0 -- add, 2 --rem, 3--set
	public float amount;

	public EnergyMessage(int op, float amount) {
		this.op = op;
		this.amount = amount;
	}

	public EnergyMessage() {
		this.op = 0;
		this.amount = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.op = buf.readInt();
		this.amount = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.op);
		buf.writeFloat(this.amount);
	}
}
