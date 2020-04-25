package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SelectFoundationAttributeMessage implements IMessage {

	public int attribute;
	public UUID sender;

	public SelectFoundationAttributeMessage(int attribute, UUID sender) {
		this.attribute = attribute;
		this.sender = sender;
	}

	public SelectFoundationAttributeMessage() {
		this.attribute = -1;
		this.sender = UUID.randomUUID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.attribute = packetBuffer.readInt();
		this.sender = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		packetBuffer.writeInt(this.attribute);
		packetBuffer.writeUniqueId(this.sender);
	}
}
