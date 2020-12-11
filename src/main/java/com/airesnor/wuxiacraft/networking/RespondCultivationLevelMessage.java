package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EntityRenderHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class RespondCultivationLevelMessage implements IMessage {

	public ICultivation targetCultivation;
	public UUID responderUUID;

	public RespondCultivationLevelMessage(ICultivation cultivation, UUID responderUUID) {
		this.targetCultivation = cultivation;
		this.responderUUID = responderUUID;
	}

	@SuppressWarnings("unused")
	public RespondCultivationLevelMessage() {
		targetCultivation = new Cultivation();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		NBTTagCompound cultivationTag = ByteBufUtils.readTag(buf);
		//noinspection ConstantConditions
		CultivationProvider.CULTIVATION_CAP.getStorage().readNBT(CultivationProvider.CULTIVATION_CAP, this.targetCultivation, null, cultivationTag);

		this.responderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		//noinspection ConstantConditions
		NBTTagCompound cultivationTag = (NBTTagCompound) CultivationProvider.CULTIVATION_CAP.getStorage().writeNBT(CultivationProvider.CULTIVATION_CAP, this.targetCultivation, null);
		ByteBufUtils.writeTag(buf, cultivationTag);
		packetBuffer.writeUniqueId(this.responderUUID);
	}

	public static class Handler implements IMessageHandler<RespondCultivationLevelMessage, IMessage> {
		@Override
		public IMessage onMessage(RespondCultivationLevelMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				return handleMessageClient(message);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		private IMessage handleMessageClient(RespondCultivationLevelMessage message) {
			EntityRenderHandler.knownCultivations.put(message.responderUUID, message.targetCultivation);
			EntityRenderHandler.knownCultivationGetTimes.put(message.responderUUID, System.currentTimeMillis());
			return null;
		}
	}

}
