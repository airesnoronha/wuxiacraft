package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class RemoveTechniqueMessage implements IMessage {

	Technique toBeRemoved;
	UUID senderUUID;

	public RemoveTechniqueMessage(Technique toBeRemoved, UUID senderUUID) {
		this.toBeRemoved = toBeRemoved;
		this.senderUUID = senderUUID;
	}

	public RemoveTechniqueMessage() {
		this.toBeRemoved = null;
		this.senderUUID = null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		int techniqueId = buf.readInt();
		this.toBeRemoved = Techniques.TECHNIQUES.get(techniqueId);
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(Techniques.TECHNIQUES.indexOf(this.toBeRemoved));
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
