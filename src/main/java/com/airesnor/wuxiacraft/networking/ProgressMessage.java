package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ProgressMessage implements IMessage {

	public int op;
	public double amount;
	public boolean allowBreakTrough;
	public boolean ignoreBottleneck;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public ProgressMessage() {
		this.op = 0;
		this.amount = 0;
		allowBreakTrough = false;
		ignoreBottleneck = false;
		this.senderUUID = null;
	}

	public ProgressMessage(int op, double amount, boolean allowBreakTrough, boolean ignoreBottleneck, UUID senderUUID) {
		this.op = op;
		this.amount = amount;
		this.allowBreakTrough = allowBreakTrough;
		this.ignoreBottleneck = ignoreBottleneck;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.op = buf.readInt();
		this.amount = buf.readDouble();
		this.allowBreakTrough = buf.readBoolean();
		this.ignoreBottleneck = buf.readBoolean();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.op);
		buf.writeDouble(this.amount);
		buf.writeBoolean(this.allowBreakTrough);
		buf.writeBoolean(this.ignoreBottleneck);
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
