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

public class EnergyMessage implements IMessage {

	public int op; // 0 -- add, 2 --rem, 3--set
	public float amount;
	public UUID senderUUID;

	public EnergyMessage(int op, float amount, UUID senderUUID) {
		this.op = op;
		this.amount = amount;
		this.senderUUID = senderUUID;
	}

	public EnergyMessage() {
		this.op = 0;
		this.amount = 0;
		this.senderUUID = null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.op = buf.readInt();
		this.amount = buf.readFloat();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.op);
		buf.writeFloat(this.amount);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<EnergyMessage, IMessage> {

		@Override
		public IMessage onMessage(EnergyMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if(player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						switch (message.op) {
							case 0:
								cultivation.addEnergy(message.amount);
								break;
							case 1:
								cultivation.remEnergy(message.amount);
								break;
							case 2:
								cultivation.setEnergy(message.amount);
								break;
						}
					}
				});
			}
			return null;
		}
	}

}
