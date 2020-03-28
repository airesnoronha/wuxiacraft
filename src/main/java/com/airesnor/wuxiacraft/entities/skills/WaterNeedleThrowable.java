package com.airesnor.wuxiacraft.entities.skills;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class WaterNeedleThrowable extends EntityThrowable {

	private EntityLivingBase owner;

	private float damage;

	private int duration;

	public WaterNeedleThrowable(World worldIn) {
		super(worldIn);
	}

	public WaterNeedleThrowable(World worldIn, EntityLivingBase owner, float damage) {
		this(worldIn, owner, damage, 30);
	}

	public WaterNeedleThrowable(World worldIn, EntityLivingBase owner, float damage, int duration) {
		super(worldIn, owner.posX, owner.posY + owner.getEyeHeight() - 0.1, owner.posZ);
		this.setSize(0.05f, 0.05f);
		this.setNoGravity(true);
		this.owner = owner;
		this.thrower = this.owner;
		this.damage = damage;
		this.duration = duration;
	}

	@Override
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
	}


	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY && !result.entityHit.equals(this.owner)) {
				if(result.entityHit instanceof EntityLivingBase) {
					this.attackEntityOnDirectHit((EntityLivingBase) result.entityHit);
					this.setDead();
				}
			} else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				if (canNotPassThroughHitBlock(result)) {
					this.setDead();
				}
			}
		}
	}

	private void attackEntityOnDirectHit(EntityLivingBase hitEntity) {
		hitEntity.attackEntityFrom(DamageSource.causeMobDamage(owner).setMagicDamage().setProjectile(), this.damage);
		if (this.owner != null) {
			hitEntity.setLastAttackedEntity(this.owner);
		}
	}

	private boolean canNotPassThroughHitBlock(RayTraceResult result) {
		BlockPos hitBlockPos = result.getBlockPos();
		IBlockState hitState = this.world.getBlockState(hitBlockPos);
		return hitState.getMaterial().blocksMovement();
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.ticksExisted >= this.duration) {
			this.setDead();
			return;
		}

		if (this.ticksExisted >= 2 && this.world instanceof WorldServer) {
			WorldServer worldServer = (WorldServer) this.world;
			worldServer.spawnParticle(EnumParticleTypes.WATER_DROP, false, this.posX, this.posY, this.posZ, 3, this.width, this.height, this.width, 0.005d, 0);
		}
	}
}
