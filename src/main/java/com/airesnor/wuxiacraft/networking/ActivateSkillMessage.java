package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ActivateSkillMessage implements IMessage {

	public int selectedSkill = 0;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public ActivateSkillMessage() {
	}

	public ActivateSkillMessage(int selectedSkill, UUID senderUUID) {
		this.selectedSkill = selectedSkill;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.selectedSkill = buf.readInt();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.selectedSkill);
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
