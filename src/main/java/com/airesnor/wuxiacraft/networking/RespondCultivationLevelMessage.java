package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class RespondCultivationLevelMessage implements IMessage {

	public CultivationLevel responderLevel;
	public int responderSubLevel;
	public String responderName;
	public UUID responderUUID;

	public RespondCultivationLevelMessage(CultivationLevel responderLevel, int responderSubLevel, UUID responderUUID) {
		this.responderLevel = responderLevel;
		this.responderSubLevel = responderSubLevel;
		this.responderUUID = responderUUID;
	}

	public RespondCultivationLevelMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.responderSubLevel = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[30];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultlevelname = new String(bytes, 0, length);
		this.responderLevel = CultivationLevel.REGISTERED_LEVELS.get(cultlevelname);
		this.responderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.responderSubLevel);
		byte[] bytes = this.responderLevel.levelName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		packetBuffer.writeUniqueId(this.responderUUID);
	}
}
