package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RespondCultivationLevelMessage implements IMessage {

	public CultivationLevel responderLevel;
	public int responderSubLevel;
	public String askerName;

	public RespondCultivationLevelMessage(CultivationLevel responderLevel, int responderSubLevel, String askerName) {
		this.responderLevel = responderLevel;
		this.responderSubLevel = responderSubLevel;
		this.askerName = askerName;
	}

	public RespondCultivationLevelMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.responderSubLevel = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[30];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultlevelname = new String(bytes, 0, length);
		this.responderLevel = CultivationLevel.valueOf(cultlevelname);
		length = buf.readInt();
		bytes = new byte[50];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		this.askerName = new String(bytes, 0, length);

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.responderSubLevel);
		byte[] bytes = this.responderLevel.name().getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		bytes = this.askerName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}
}
