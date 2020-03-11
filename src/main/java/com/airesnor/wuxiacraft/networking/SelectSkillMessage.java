package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SelectSkillMessage implements IMessage {

	public int selectSkill;

	public SelectSkillMessage(int selectSkill) {
		this.selectSkill = selectSkill;
	}

	public SelectSkillMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectSkill = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectSkill);
	}
}
