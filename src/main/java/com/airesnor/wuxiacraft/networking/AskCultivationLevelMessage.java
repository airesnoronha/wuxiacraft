package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class AskCultivationLevelMessage implements IMessage {

	public UUID askerUUID;

	public AskCultivationLevelMessage(UUID askerUUID) {
		this.askerUUID = askerUUID;
	}

	public AskCultivationLevelMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.askerUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		packetBuffer.writeUniqueId(this.askerUUID);
	}

	public static class Handler implements IMessageHandler<AskCultivationLevelMessage, RespondCultivationLevelMessage> {

		@Override
		public RespondCultivationLevelMessage onMessage(AskCultivationLevelMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.askerUUID);
					if(player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
						final double scanFactor = cultTech.getScanFactor(cultivation.getDivineModifier());
						AxisAlignedBB range = new AxisAlignedBB(player.getPosition()).grow(Math.max(8, Math.min(scanFactor * 0.4, 96)));
						world.getEntitiesWithinAABB(EntityPlayerMP.class, range, target -> {
							ICultivation targetCultivation = CultivationUtils.getCultivationFromEntity(target);
							ICultTech targetCultTech = CultivationUtils.getCultTechFromEntity(target);
							return scanFactor > targetCultTech.getResistFactor(targetCultivation.getDivineModifier());
						}).forEach(target -> {
							NetworkWrapper.INSTANCE.sendTo(new RespondCultivationLevelMessage(CultivationUtils.getCultivationFromEntity(target), target.getUniqueID()), (EntityPlayerMP) player);
						});
					}
				});
			}
			return null;
		}
	}

}
