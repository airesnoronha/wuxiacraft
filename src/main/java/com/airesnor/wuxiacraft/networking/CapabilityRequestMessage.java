package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.cultivation.IBarrier;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
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

	public UUID senderUUID;

	public CapabilityRequestMessage(UUID senderUUID) {
		this.senderUUID = senderUUID;
	}

	public CapabilityRequestMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<CapabilityRequestMessage, UnifiedCapabilitySyncMessage> {

		@Override
		public UnifiedCapabilitySyncMessage onMessage(CapabilityRequestMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				WorldServer world = ctx.getServerHandler().player.getServerWorld();
				EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
				if(player != null) {
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
					IAuraCap auraCap = CultivationUtils.getAuraFromEntity(player);
					IBarrier barrier = CultivationUtils.getBarrierFromEntity(player);
					return new UnifiedCapabilitySyncMessage(cultivation, cultTech, skillCap, auraCap, barrier, true);
				}
			}
			return null;
		}
	}

}
