package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class AddProgressToFoundationAttributeMessage implements IMessage {

	public double amount;
	public int attribute;
	public UUID sender;

	public AddProgressToFoundationAttributeMessage(double amount, int attribute, UUID sender) {
		this.amount = amount;
		this.attribute = attribute;
		this.sender = sender;
	}

	public AddProgressToFoundationAttributeMessage() {
		this.amount = 0;
		this.attribute = 0;
		this.sender = UUID.randomUUID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.amount = buf.readDouble();
		this.attribute = buf.readInt();
		this.sender = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeDouble(this.amount);
		buf.writeInt(this.attribute);
		packetBuffer.writeUniqueId(this.sender);
	}
}
