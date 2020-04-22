package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SelectSkillMessage implements IMessage {

	public int selectSkill;
	public UUID senderUUID;

	public SelectSkillMessage(int selectSkill, UUID senderUUID) {
		this.selectSkill = selectSkill;
		this.senderUUID = senderUUID;
	}

	public SelectSkillMessage() {
		this.senderUUID = null;
		this.selectSkill = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.selectSkill = buf.readInt();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.selectSkill);
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
