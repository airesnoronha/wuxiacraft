package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
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

public class CapabilityRequestMessage implements IMessage {

	public UUID sender;

	public CapabilityRequestMessage(UUID sender) {
		this.sender = sender;
	}

	public CapabilityRequestMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.sender = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		packetBuffer.writeUniqueId(this.sender);
	}

	public static class Handler implements IMessageHandler<CapabilityRequestMessage, UnifiedCapabilitySyncMessage> {

		@Override
		public UnifiedCapabilitySyncMessage onMessage(CapabilityRequestMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				WorldServer world = ctx.getServerHandler().player.getServerWorld();
				EntityPlayer player = world.getPlayerEntityByUUID(message.sender);
				if(player != null) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
					return new UnifiedCapabilitySyncMessage(cultivation, cultTech, skillCap, true);
				}
			}
			return null;
		}
	}

}
