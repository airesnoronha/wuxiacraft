package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ActivatePartialSkillMessage implements IMessage {

	public String skillName;
	public float energy;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public ActivatePartialSkillMessage() {
	}

	public ActivatePartialSkillMessage(String skillName, float energy, UUID senderUUID) {
		this.skillName = skillName;
		this.energy = energy;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.energy = buf.readFloat();
		int length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.skillName = new String(bytes);
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeFloat(energy);
		byte[] bytes = skillName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		packetBuffer.writeUniqueId(senderUUID);
	}
}
