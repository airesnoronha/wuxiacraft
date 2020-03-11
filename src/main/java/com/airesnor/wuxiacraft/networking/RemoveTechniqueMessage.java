package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RemoveTechniqueMessage implements IMessage {

	Technique toBeRemoved;

	public RemoveTechniqueMessage(Technique toBeRemoved) {
		this.toBeRemoved = toBeRemoved;
	}

	public RemoveTechniqueMessage() {
		this.toBeRemoved = null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		if (length > 0) {
			byte[] bytes = new byte[length];
			buf.readBytes(bytes, 0, length);
			String name = new String(bytes);
			this.toBeRemoved = Techniques.getTechniqueByUName(name);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (this.toBeRemoved != null) {
			buf.writeInt(this.toBeRemoved.getUName().length());
			buf.writeBytes(this.toBeRemoved.getUName().getBytes());
		} else {
			buf.writeInt(0);
		}
	}
}
