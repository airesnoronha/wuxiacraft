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

public class SuppressCultivationMessage implements IMessage {

	public boolean suppress;
	public UUID senderUUID;

	public SuppressCultivationMessage(boolean suppress, UUID senderUUID) {
		this.suppress = suppress;
		this.senderUUID = senderUUID;
	}

	public SuppressCultivationMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.suppress = buf.readBoolean();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeBoolean(this.suppress);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<SuppressCultivationMessage, IMessage> {

		@Override
		public IMessage onMessage(SuppressCultivationMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if(player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						cultivation.setSuppress(message.suppress);
					}
				});
			}
			return null;
		}
	}

}
