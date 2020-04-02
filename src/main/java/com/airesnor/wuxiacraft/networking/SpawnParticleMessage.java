package com.airesnor.wuxiacraft.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
}
