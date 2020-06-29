package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class RespondCultivationLevelMessage implements IMessage {

	public CultivationLevel responderLevel;
	public int responderSubLevel;
	public String responderName;
	public UUID responderUUID;

	public RespondCultivationLevelMessage(CultivationLevel responderLevel, int responderSubLevel, UUID responderUUID) {
		this.responderLevel = responderLevel;
		this.responderSubLevel = responderSubLevel;
		this.responderUUID = responderUUID;
	}

	public RespondCultivationLevelMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.responderSubLevel = buf.readInt();
		String cultlevelname = ByteBufUtils.readUTF8String(buf);
		this.responderLevel = CultivationLevel.REGISTERED_LEVELS.get(cultlevelname);
		this.responderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.responderSubLevel);
		ByteBufUtils.writeUTF8String(buf, this.responderLevel.levelName);
		packetBuffer.writeUniqueId(this.responderUUID);
	}

	public static class Handler implements IMessageHandler<RespondCultivationLevelMessage, IMessage> {
		@Override
		public IMessage onMessage(RespondCultivationLevelMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				return handleMessageClient(message, ctx);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		private IMessage handleMessageClient(RespondCultivationLevelMessage message, MessageContext ctx) {
			ICultivation cultivation = new Cultivation();
			cultivation.setCurrentLevel(message.responderLevel);
			cultivation.setCurrentSubLevel(message.responderSubLevel);
			UUID uuid = message.responderUUID;
			RendererHandler.knownCultivations.put(uuid, cultivation);
			return null;
		}
	}

}
