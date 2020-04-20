package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SpeedHandicapMessage implements IMessage {

	public int handicap;
	public float maxSpeed;
	public float hasteLimit;
	public float jumpLimit;
	public UUID senderUUID;

	public SpeedHandicapMessage(int handicap, float maxSpeed, float hasteLimit, float jumpLimit, UUID senderUUID) {
		this.handicap = handicap;
		this.maxSpeed = maxSpeed;
		this.hasteLimit = hasteLimit;
		this.jumpLimit = jumpLimit;
		this.senderUUID = senderUUID;
	}

	public SpeedHandicapMessage() {
		this.handicap = 0;
		this.maxSpeed = 0;
		this.hasteLimit = 0;
		this.jumpLimit = 0;
		this.senderUUID = null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.handicap = buf.readInt();
		this.maxSpeed = buf.readFloat();
		this.hasteLimit = buf.readFloat();
		this.jumpLimit = buf.readFloat();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.handicap);
		buf.writeFloat(this.maxSpeed);
		buf.writeFloat(this.hasteLimit);
		buf.writeFloat(this.jumpLimit);
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
