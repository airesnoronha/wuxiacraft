package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SelectSkillMessage implements IMessage {

	public int selectSkill;
	public String sender;

	public SelectSkillMessage(int selectSkill, String sender) {
		this.selectSkill = selectSkill;
		this.sender = sender;
	}

	public SelectSkillMessage() {
		this.sender = "";
		this.selectSkill = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectSkill = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectSkill);
		buf.writeInt(this.sender.getBytes().length);
		buf.writeBytes(this.sender.getBytes());
	}
}
