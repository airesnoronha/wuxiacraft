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
		this.selectedSkill = selectedSkill;
		this.senderID = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.selectedSkill = buf.readInt();
			this.senderID = buf.readInt();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectedSkill);
		buf.writeInt(this.senderID);
	}
}
