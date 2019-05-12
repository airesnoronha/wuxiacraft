package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CultivationMessage implements IMessage {

	public CultivationLevel messageLevel;
	public int messageSubLevel;
	public int messageProgress;
	public int messageEnergy;

	public CultivationMessage(CultivationLevel messageLevel, int messageSubLevel, int messageProgress, int messageEnergy) {
		this.messageLevel = messageLevel;
		this.messageSubLevel = messageSubLevel;
		this.messageProgress = messageProgress;
		this.messageEnergy = messageEnergy;
	}

	public CultivationMessage () {
		this.messageLevel = CultivationLevel.BODY_REFINEMENT;
		this.messageSubLevel = 0;
		this.messageProgress = 0;
		this.messageEnergy = 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.messageSubLevel = buf.readInt();
		this.messageProgress = buf.readInt();
		this.messageEnergy = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[30];
		buf.readBytes(bytes, 0,length);
		bytes[length] = '\0';
		String cultlevelname = new String(bytes,0, length);
		this.messageLevel = CultivationLevel.valueOf(cultlevelname);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] bytes = messageLevel.name().getBytes();
		buf.writeInt(this.messageSubLevel);
		buf.writeInt(this.messageProgress);
		buf.writeInt(this.messageEnergy);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
