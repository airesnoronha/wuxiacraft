package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class AskCultivationLevelMessage implements IMessage {

	public CultivationLevel askerLevel;
	public int askerSubLevel;
	public UUID askerUUID;

	public AskCultivationLevelMessage(CultivationLevel askerLevel, int askerSubLevel, UUID askerUUID) {
		this.askerLevel = askerLevel;
		this.askerSubLevel = askerSubLevel;
		this.askerUUID = askerUUID;
	}

	public AskCultivationLevelMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.askerSubLevel = buf.readInt();
		int length = buf.readInt();
		byte[] bytes = new byte[30];
		buf.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultlevelname = new String(bytes, 0, length);
		this.askerLevel = CultivationLevel.REGISTERED_LEVELS.get(cultlevelname);
		this.askerUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.askerSubLevel);
		byte[] bytes = this.askerLevel.levelName.getBytes();
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
		packetBuffer.writeUniqueId(this.askerUUID);
	}

}
