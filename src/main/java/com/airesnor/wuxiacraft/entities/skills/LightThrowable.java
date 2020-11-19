package com.airesnor.wuxiacraft.entities.skills;

import com.airesnor.wuxiacraft.blocks.WuxiaBlocks;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LightThrowable extends EntityThrowable implements IEntityAdditionalSpawnData {

	public int duration;

	public int strength;

	@SuppressWarnings("unused")
	public LightThrowable(World worldIn) {
		super(worldIn);
	}

	public LightThrowable(World worldIn, EntityLivingBase owner, int duration, int strength) {
		super(worldIn, owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ);
		this.duration = duration;
		this.strength = strength;
		setSize(0.3f, 0.3f);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos lightPos = result.getBlockPos().offset(result.sideHit);
			if (world.isAirBlock(lightPos)) {
				IBlockState target = WuxiaBlocks.WEAK_LIGHT_BLOCK.getDefaultState();
				switch (this.strength) {
					case 1:
						target = WuxiaBlocks.MEDIUM_LIGHT_BLOCK.getDefaultState();
						break;
					case 2:
						target = WuxiaBlocks.STRONG_LIGHT_BLOCK.getDefaultState();
						break;
					case 3:
						target = WuxiaBlocks.MAXIMA_LIGHT_BLOCK.getDefaultState();
						break;
				}
				world.setBlockState(lightPos, target);
			}
			setDead();
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
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.ticksExisted >= this.duration) {
			this.setDead();
		}
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.duration);
		buffer.writeInt(this.strength);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.004f;
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.duration = additionalData.readInt();
		this.strength = additionalData.readInt();
	}
}
