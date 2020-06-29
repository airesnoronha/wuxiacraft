package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
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

	public static class Handler implements IMessageHandler<RemoveTechniqueMessage, IMessage> {
		@Override
		public IMessage onMessage(RemoveTechniqueMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if(player != null) {
						ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
						cultTech.remTechnique(message.toBeRemoved);
					}
				});
			}
			return null;
		}
	}

}
