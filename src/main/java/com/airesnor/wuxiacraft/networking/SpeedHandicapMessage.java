package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SpeedHandicapMessage implements IMessage {

	public int handicap;
	public float maxSpeed;
	public float hasteLimit;
	public float jumpLimit;

	public SpeedHandicapMessage(int handicap, float maxSpeed, float hasteLimit, float jumpLimit) {
		this.handicap = handicap;
		this.maxSpeed = maxSpeed;
		this.hasteLimit = hasteLimit;
		this.jumpLimit = jumpLimit;
	}

	public SpeedHandicapMessage() {
		this.handicap = 0;
		this.maxSpeed = 0;
		this.hasteLimit = 0;
		this.jumpLimit = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.handicap = buf.readInt();
		this.maxSpeed = buf.readFloat();
		this.hasteLimit = buf.readFloat();
		this.jumpLimit = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.handicap);
		buf.writeFloat(this.maxSpeed);
		buf.writeFloat(this.hasteLimit);
		buf.writeFloat(this.jumpLimit);
	}
}
