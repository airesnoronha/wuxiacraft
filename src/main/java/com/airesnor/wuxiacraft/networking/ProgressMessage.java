package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ProgressMessage implements IMessage {

	public int op;
	public double amount;
	public boolean techniques;
	public boolean allowBreakTrough;
	public boolean ignoreBottleneck;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public ProgressMessage() {
		this.op = 0;
		this.amount = 0;
		this.techniques = false;
		allowBreakTrough = false;
		ignoreBottleneck = false;
		this.senderUUID = null;
	}

	public ProgressMessage(int op, double amount, boolean techniques, boolean allowBreakTrough, boolean ignoreBottleneck, UUID senderUUID) {
		this.op = op;
		this.amount = amount;
		this.techniques = techniques;
		this.allowBreakTrough = allowBreakTrough;
		this.ignoreBottleneck = ignoreBottleneck;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.op = buf.readInt();
		this.amount = buf.readDouble();
		this.techniques = buf.readBoolean();
		this.allowBreakTrough = buf.readBoolean();
		this.ignoreBottleneck = buf.readBoolean();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.op);
		buf.writeDouble(this.amount);
		buf.writeBoolean(this.techniques);
		buf.writeBoolean(this.allowBreakTrough);
		buf.writeBoolean(this.ignoreBottleneck);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<ProgressMessage, IMessage> {

		@Override
		public IMessage onMessage(ProgressMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if(player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						switch (message.op) {
							case 0:
								CultivationUtils.cultivatorAddProgress(player, message.amount, message.techniques, message.allowBreakTrough, message.ignoreBottleneck);
								break;
							case 1:
								cultivation.addProgress(-message.amount, false);
								break;
							case 2:
								cultivation.setProgress(message.amount);
						}
					}
				});
			}
			return null;
		}
	}

}
