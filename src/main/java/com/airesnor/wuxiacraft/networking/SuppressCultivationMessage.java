package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SuppressCultivationMessage implements IMessage {

	public boolean suppress;
	public UUID senderUUID;

	public SuppressCultivationMessage(boolean suppress, UUID senderUUID) {
		this.suppress = suppress;
		this.senderUUID = senderUUID;
	}

	public SuppressCultivationMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.suppress = buf.readBoolean();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeBoolean(this.suppress);
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
