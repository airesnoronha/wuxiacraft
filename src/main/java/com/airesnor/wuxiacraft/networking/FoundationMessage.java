package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.formation.FormationCultivationHelper;
import com.airesnor.wuxiacraft.formation.FormationTileEntity;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class FoundationMessage implements IMessage {

	public int op;
	public double amount;
	public Cultivation.System system;
	public UUID senderUUID;

	@SuppressWarnings("unused")
	public FoundationMessage() {
		this.op = 0;
		this.amount = 0;
		this.system = Cultivation.System.ESSENCE;
		this.senderUUID = null;
	}

	public FoundationMessage(int op, Cultivation.System system, double amount, UUID senderUUID) {
		this.op = op;
		this.amount = amount;
		this.system = system;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.op = buf.readInt();
		this.system = Cultivation.System.valueOf(packetBuffer.readString(10));
		this.amount = buf.readDouble();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.op);
		packetBuffer.writeString(this.system.toString());
		buf.writeDouble(this.amount);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<FoundationMessage, IMessage> {

		@Override
		public IMessage onMessage(FoundationMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if (player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
						switch (message.op) {
							case 0: //add
								cultivation.addSystemFoundation(message.amount, message.system);
								break;
							case 1: //remove
								cultivation.addSystemFoundation(-message.amount, message.system);
								break;
							case 2: //set
								cultivation.setSystemFoundation(message.amount, message.system);
						}
					}
				});
			}
			return null;
		}
	}

}
