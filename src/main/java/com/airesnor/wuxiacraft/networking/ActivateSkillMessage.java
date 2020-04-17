package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivateSkillMessage implements IMessage {

	public int selectedSkill = 0;
	public int senderID;

	@SuppressWarnings("unused")
	public ActivateSkillMessage() {
	}

	public ActivateSkillMessage(int selectedSkill, int sender) {
		this.selectedSkill = 0;
		this.senderID = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectedSkill =  buf.readInt();
		this.senderID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectedSkill);
		buf.writeInt(this.senderID);
	}
}
