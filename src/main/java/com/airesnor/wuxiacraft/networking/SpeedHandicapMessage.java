package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class SpeedHandicapMessage implements IMessage {

	public int handicap;
	public float maxSpeed;
	public float hasteLimit;
	public float jumpLimit;
	public float stepAssistLimit;
	public UUID senderUUID;

	public SpeedHandicapMessage(int handicap, float maxSpeed, float hasteLimit, float jumpLimit, float stepAssistLimit, UUID senderUUID) {
		this.handicap = handicap;
		this.maxSpeed = maxSpeed;
		this.hasteLimit = hasteLimit;
		this.jumpLimit = jumpLimit;
		this.stepAssistLimit = stepAssistLimit;
		this.senderUUID = senderUUID;
	}

	public SpeedHandicapMessage() {
		this.handicap = 0;
		this.maxSpeed = 0;
		this.hasteLimit = 0;
		this.jumpLimit = 0;
		this.stepAssistLimit = 0;
		this.senderUUID = null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		this.handicap = buf.readInt();
		this.maxSpeed = buf.readFloat();
		this.hasteLimit = buf.readFloat();
		this.jumpLimit = buf.readFloat();
		this.stepAssistLimit = buf.readFloat();
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
		buf.writeInt(this.handicap);
		buf.writeFloat(this.maxSpeed);
		buf.writeFloat(this.hasteLimit);
		buf.writeFloat(this.jumpLimit);
		buf.writeFloat(this.stepAssistLimit);
		packetBuffer.writeUniqueId(this.senderUUID);
	}

	public static class Handler implements IMessageHandler<SpeedHandicapMessage, SpeedHandicapMessage> {
		@Override
		public SpeedHandicapMessage onMessage(SpeedHandicapMessage message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				final WorldServer world = ctx.getServerHandler().player.getServerWorld();
				world.addScheduledTask(() -> {
					EntityPlayer player = world.getPlayerEntityByUUID(message.senderUUID);
					if (player != null) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);

						cultivation.setHandicap(message.handicap);
						cultivation.setMaxSpeed(message.maxSpeed);
						cultivation.setHasteLimit(message.hasteLimit);
						cultivation.setJumpLimit(message.jumpLimit);
						cultivation.setStepAssistLimit(message.stepAssistLimit);
					}
					EventHandler.applyModifiers(player);
				});
			}
			if (ctx.side == Side.CLIENT) {
				return handleClientMessage(message, ctx);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		public static SpeedHandicapMessage handleClientMessage(SpeedHandicapMessage message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
				cultivation.setHandicap(WuxiaCraftConfig.speedHandicap);
				cultivation.setMaxSpeed(WuxiaCraftConfig.maxSpeed);
				cultivation.setHasteLimit(WuxiaCraftConfig.blockBreakLimit);
				cultivation.setJumpLimit(WuxiaCraftConfig.jumpLimit);
				cultivation.setStepAssistLimit(WuxiaCraftConfig.stepAssistLimit);
				UUID playerUUID = Minecraft.getMinecraft().player.getUniqueID();
				NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.jumpLimit, WuxiaCraftConfig.stepAssistLimit, playerUUID));
			});
			return null;
		}
	}

}
