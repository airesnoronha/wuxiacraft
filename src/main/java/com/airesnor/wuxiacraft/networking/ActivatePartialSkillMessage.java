package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivatePartialSkillMessage implements IMessage {

	public String skillName;
	public float energy;
	public String sender;

	@SuppressWarnings("unused")
	public ActivatePartialSkillMessage() {
	}

	public ActivatePartialSkillMessage(String skillName, float energy, String sender) {
		this.skillName = skillName;
		this.energy = energy;
		this.sender = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.energy = buf.readFloat();
		int length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.skillName = new String(bytes);
		length = buf.readInt();
		bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(energy);
		byte[] bytes = skillName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		bytes = sender.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
