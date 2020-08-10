package com.airesnor.wuxiacraft.entities.skills;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.ParametersAreNonnullByDefault;

public class SoulArrowThrowable extends EntityThrowable implements IEntityAdditionalSpawnData {

	public float strength;
	public EntityLivingBase owner;
	public int duration;

	@SuppressWarnings("unused")
	public SoulArrowThrowable(World worldIn) {
		super(worldIn);
	}

	public SoulArrowThrowable(World worldIn, EntityLivingBase owner, float strength, int duration) {
		super(worldIn, owner.posX, owner.posY + owner.getEyeHeight() - 0.1, owner.posZ);
		this.strength = strength;
		this.duration = duration;
		this.owner = owner;
		this.setNoGravity(true);
		this.handleWaterMovement();
		this.setSize(0.05f, 0.05f);
	}


	@Override
	@ParametersAreNonnullByDefault
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
	}

	@Override
	public void onUpdate() {
		if (this.inWater) {
			this.motionX *= 1.0f / 0.81f;
			this.motionY *= 1.0f / 0.81f;
			this.motionZ *= 1.0f / 0.81f;
		}
		super.onUpdate();
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.ticksExisted >= this.duration) {
			this.setDead();
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.ENTITY && !result.entityHit.equals(this.owner)) {
			if (result.entityHit instanceof EntityLivingBase) {
				if (!this.world.isRemote) {
					this.attackEntityOnDirectHit((EntityLivingBase) result.entityHit);
				}
				this.setDead();
			}
		} //else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
		//if (canNotPassThroughHitBlock(result)) {
		//	this.setDead();
		//}
		//}
	}

	private void attackEntityOnDirectHit(EntityLivingBase hitEntity) {
		hitEntity.attackEntityFrom(DamageSource.causeMobDamage(owner).setMagicDamage().setProjectile(), this.strength);
		if (this.owner != null) {
			hitEntity.setLastAttackedEntity(this.owner);
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(this.duration);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		this.duration = additionalData.readInt();
	}
}
