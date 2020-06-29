package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.utils.SkillUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpawnParticleMessage implements IMessage {

	private EnumParticleTypes particleType;
	private boolean ignoreRange;
	private double posX, posY, posZ;
	private double motionX, motionY, motionZ;
	private int[] arguments;

	public SpawnParticleMessage() {
	}

	public SpawnParticleMessage(EnumParticleTypes particleType, boolean ignoreRange, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int... arguments) {
		this.particleType = particleType;
		this.ignoreRange = ignoreRange;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.arguments = arguments;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());

		if (this.particleType == null) {
			this.particleType = EnumParticleTypes.BARRIER;
		}
		this.ignoreRange = buf.readBoolean();
		this.posX = buf.readDouble();
		this.posY = buf.readDouble();
		this.posZ = buf.readDouble();
		this.motionX = buf.readDouble();
		this.motionY = buf.readDouble();
		this.motionZ = buf.readDouble();
		int i = this.particleType.getArgumentCount();
		this.arguments = new int[i];
		for (int j = 0; j < i; j++) {
			if (buf.isReadable(4)) {
				this.arguments[j] = buf.readInt();
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.particleType.getParticleID());
		buf.writeBoolean(this.ignoreRange);
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
		buf.writeDouble(this.motionX);
		buf.writeDouble(this.motionY);
		buf.writeDouble(this.motionZ);
		int i = this.particleType.getArgumentCount();
		for (int j = 0; j < i; ++j) {
			if (this.arguments.length > j)
				buf.writeInt(this.arguments[j]);
		}
	}

	public EnumParticleTypes getParticleType() {
		return particleType;
	}

	public boolean isIgnoreRange() {
		return ignoreRange;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public double getMotionX() {
		return motionX;
	}

	public double getMotionY() {
		return motionY;
	}

	public double getMotionZ() {
		return motionZ;
	}

	public int[] getArguments() {
		return arguments;
	}

	public BlockPos getPos() {
		return new BlockPos(this.posX, this.posY, this.posZ);
	}

	public static class Handler implements IMessageHandler<SpawnParticleMessage, IMessage> {
		@Override
		public IMessage onMessage(SpawnParticleMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				return handleClientSide(message);
			}
			else if (ctx.side == Side.SERVER) {
				if(ctx.getServerHandler() != null) {
					final EntityPlayerMP player = ctx.getServerHandler().player;
					player.getServerWorld().addScheduledTask(() -> {
						SkillUtils.sendMessageWithinRange(player.getServerWorld(), message.getPos(), 64d, message);
					});
				}
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		public IMessage handleClientSide(SpawnParticleMessage message) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Minecraft.getMinecraft().world.spawnParticle(message.getParticleType(),
						message.isIgnoreRange(),
						message.getPosX(), message.getPosY(), message.getPosZ(),
						message.getMotionX(), message.getMotionY(), message.getMotionZ(),
						message.getArguments());
			});
			return null;
		}
	}

}
