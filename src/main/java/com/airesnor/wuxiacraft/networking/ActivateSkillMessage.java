package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivateSkillMessage implements IMessage {

	public int selectedSkill = 0;

	public ActivateSkillMessage() {
	}

	public ActivateSkillMessage(int selectedSkill) {
		this.selectedSkill = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectedSkill =  buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectedSkill);
	}
}
