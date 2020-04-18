package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivateSkillMessage implements IMessage {

	public int selectedSkill = 0;
	public String sender;

	@SuppressWarnings("unused")
	public ActivateSkillMessage() {
	}

	public ActivateSkillMessage(int selectedSkill, String sender) {
		this.selectedSkill = selectedSkill;
		this.sender = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectedSkill = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte [length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectedSkill);
		byte[] bytes = this.sender.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
