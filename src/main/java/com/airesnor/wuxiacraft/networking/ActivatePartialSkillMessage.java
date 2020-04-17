package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivatePartialSkillMessage implements IMessage {

	public String skillName;
	public float energy;
	public int senderID;

	@SuppressWarnings("unused")
	public ActivatePartialSkillMessage() {
	}

	public ActivatePartialSkillMessage(String skillName, float energy, int senderID) {
		this.skillName = skillName;
		this.energy = energy;
		this.senderID = senderID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.energy = buf.readFloat();
		int length = buf.readInt();
		byte [] bytes = new byte [length];
		buf.readBytes(bytes, 0 ,length);
		this.skillName = new String(bytes);
		this.senderID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(energy);
		byte [] bytes = skillName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		buf.writeInt(this.senderID);
	}
}
