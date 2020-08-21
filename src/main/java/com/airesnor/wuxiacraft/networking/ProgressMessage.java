package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
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

public class ProgressMessage implements IMessage {

	public int op;
	public double amount;
	public boolean techniques;
	public boolean allowBreakTrough;
	public Cultivation.System system;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public ProgressMessage() {
		this.op = 0;
		this.amount = 0;
		this.techniques = false;
		this.allowBreakTrough = false;
		this.system = Cultivation.System.ESSENCE;
		this.senderUUID = null;
	}

	public ProgressMessage(int op, Cultivation.System system, double amount, boolean techniques, boolean allowBreakTrough, UUID senderUUID) {
		this.op = op;
		this.amount = amount;
		this.techniques = techniques;
		this.allowBreakTrough = allowBreakTrough;
		this.system = system;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.op = buf.readInt();
		this.system = Cultivation.System.valueOf(packetBuffer.readString(10));
		this.amount = buf.readDouble();
		this.techniques = buf.readBoolean();
		this.allowBreakTrough = buf.readBoolean();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.op);
		packetBuffer.writeString(this.system.toString());
		buf.writeDouble(this.amount);
		buf.writeBoolean(this.techniques);
		buf.writeBoolean(this.allowBreakTrough);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<ProgressMessage, IMessage> {

		@Override
		public IMessage onMessage(ProgressMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if(player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						switch (message.op) {
							case 0:
								CultivationUtils.cultivatorAddProgress(player, message.system, message.amount, message.techniques, message.allowBreakTrough);
								ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
								cultTech.getTechniqueBySystem(message.system).getTechnique().cultivationEffect.activate(player); //this runs server side
								break;
							case 1:
								try {
									cultivation.addEssenceProgress(-message.amount, false);
								} catch (Cultivation.RequiresTribulation trib) {
									WuxiaCraft.logger.error("I don't think it'll require tribulation");
								}
								break;
							case 2:
								cultivation.setEssenceProgress(message.amount);
						}
					}
				});
			}
			return null;
		}
	}

}
