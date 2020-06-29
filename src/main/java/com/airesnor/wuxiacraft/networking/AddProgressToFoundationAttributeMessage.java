package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
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

public class AddProgressToFoundationAttributeMessage implements IMessage {

	public double amount;
	public int attribute;
	public UUID sender;

	public AddProgressToFoundationAttributeMessage(double amount, int attribute, UUID sender) {
		this.amount = amount;
		this.attribute = attribute;
		this.sender = sender;
	}

	public AddProgressToFoundationAttributeMessage() {
		this.amount = 0;
		this.attribute = 0;
		this.sender = UUID.randomUUID();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.amount = buf.readDouble();
		this.attribute = buf.readInt();
		this.sender = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeDouble(this.amount);
		buf.writeInt(this.attribute);
		packetBuffer.writeUniqueId(this.sender);
	}

	public static class Handler implements IMessageHandler<AddProgressToFoundationAttributeMessage, IMessage> {

		@Override
		public IMessage onMessage(AddProgressToFoundationAttributeMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					double amount = message.amount;
					EntityPlayer player = world.getPlayerEntityByUUID(message.sender);
					if(player!= null ) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
						if(cultivation.getCurrentProgress() < amount) {
							amount = cultivation.getCurrentProgress();
						}
						amount = Math.max(0, amount);
						cultivation.addProgress(-amount, false);
						cultivation.setProgress(Math.max(0, cultivation.getCurrentProgress()));
						int attribute = MathUtils.clamp(message.attribute, 0, 5);
						switch (attribute) {
							case 0:
								foundation.addAgilityProgress(amount);
								break;
							case 1:
								foundation.addConstitutionProgress(amount);
								break;
							case 2:
								foundation.addDexterityProgress(amount);
								break;
							case 3:
								foundation.addResistanceProgress(amount);
								break;
							case 4:
								foundation.addSpiritProgress(amount);
								break;
							case 5:
								foundation.addStrengthProgress(amount);
								break;
						}
						foundation.keepMaxLevel(cultivation);
					}
				});
			}
			return null;
		}
	}

}
