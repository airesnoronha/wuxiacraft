package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class RequestAuraForOtherPlayerMessage implements IMessage {

	private UUID requester;
	private UUID target;

	public RequestAuraForOtherPlayerMessage() {
	}

	public RequestAuraForOtherPlayerMessage(UUID requester, UUID target) {
		this.requester = requester;
		this.target = target;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		this.requester = pb.readUniqueId();
		this.target = pb.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer pb = new PacketBuffer(buf);
		pb.writeUniqueId(this.requester);
		pb.writeUniqueId(this.target);
	}

	public static class Handler implements IMessageHandler<RequestAuraForOtherPlayerMessage, IMessage> {

		@Override
		public IMessage onMessage(RequestAuraForOtherPlayerMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				WorldServer server = ctx.getServerHandler().player.getServerWorld();
				EntityPlayerMP requester = (EntityPlayerMP) server.getPlayerEntityByUUID(message.requester);
				EntityPlayerMP target = (EntityPlayerMP) server.getPlayerEntityByUUID(message.target);
				if (requester != null && target != null) {
					IAuraCap auraCap = CultivationUtils.getAuraFromEntity(target);
					NetworkWrapper.INSTANCE.sendTo(new RespondAuraForOtherPlayerMessage(target.getUniqueID(), auraCap), requester);
				}
			}
			return null;
		}
	}
}
