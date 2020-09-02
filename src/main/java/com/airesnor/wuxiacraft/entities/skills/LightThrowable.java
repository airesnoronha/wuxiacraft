package com.airesnor.wuxiacraft.entities.skills;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class LightThrowable extends EntityThrowable implements IEntityAdditionalSpawnData {

	public int duration;

	public int particles;

	private EntityLivingBase owner;

	public LightThrowable(World worldIn) {
		super(worldIn);
		setNoGravity(true);
	}

	public LightThrowable(World worldIn, double x, double y, double z, int duration, int particles, EntityLivingBase owner) {
		super(worldIn, x, y, z);
		this.duration = duration;
		this.particles = particles;
		this.owner = owner;
		setNoGravity(true);
		setSize(0.3f, 0.3f);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos lightPos = result.getBlockPos().offset(result.sideHit);
			if (world.isAirBlock(lightPos)) {

			}
		}
	}

	@Override
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.duration);
		buffer.writeInt(this.particles);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.duration = additionalData.readInt();
		this.particles = additionalData.readInt();
	}
}
