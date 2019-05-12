package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SpeedHandicapMessage implements IMessage {

	public int handicap;

	public SpeedHandicapMessage(int handicap) {
		this.handicap = handicap;
	}

	public SpeedHandicapMessage() {
		this.handicap = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.handicap = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.handicap);
	}
}
