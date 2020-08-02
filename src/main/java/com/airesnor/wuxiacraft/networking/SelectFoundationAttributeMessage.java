package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class SelectFoundationAttributeMessage implements IMessage {

	public int attribute;
	public UUID sender;

	public SelectFoundationAttributeMessage(int attribute, UUID sender) {
		this.attribute = attribute;
		this.sender = sender;
	}

	public SelectFoundationAttributeMessage() {
		this.attribute = -1;
		this.sender = UUID.randomUUID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.attribute = packetBuffer.readInt();
		this.sender = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		packetBuffer.writeInt(this.attribute);
		packetBuffer.writeUniqueId(this.sender);
	}

	public static class Handler implements IMessageHandler<SelectFoundationAttributeMessage, IMessage> {

		@Override
		public IMessage onMessage(SelectFoundationAttributeMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {/*
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.sender);
					if(player!= null) {
						IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
						int attribute = MathUtils.clamp(message.attribute, -1, 5);
						foundation.selectAttribute(attribute);
					}
				});
			*/}
			return null;
		}
	}

}
