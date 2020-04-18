package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RemoveTechniqueMessage implements IMessage {

	Technique toBeRemoved;
	String sender;

	public RemoveTechniqueMessage(Technique toBeRemoved, String sender) {
		this.toBeRemoved = toBeRemoved;
		this.sender = sender;
	}

	public RemoveTechniqueMessage() {
		this.toBeRemoved = null;
		this.sender = "";
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int techniqueId = buf.readInt();
		this.toBeRemoved = Techniques.TECHNIQUES.get(techniqueId);
		int length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(Techniques.TECHNIQUES.indexOf(this.toBeRemoved));
		buf.writeInt(this.sender.getBytes().length);
		buf.writeBytes(this.sender.getBytes());
	}
}
