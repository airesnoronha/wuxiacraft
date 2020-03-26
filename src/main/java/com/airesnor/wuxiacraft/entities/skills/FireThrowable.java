package com.airesnor.wuxiacraft.entities.skills;

import com.airesnor.wuxiacraft.networking.SpawnParticleMessage;
import com.airesnor.wuxiacraft.utils.SkillUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public class FireThrowable extends EntityThrowable {

	private EntityLivingBase owner;

	private float damage;

	private int duration;

	private int particles;

	public FireThrowable(World worldIn) {
		super(worldIn);
	}

	public FireThrowable(World worldIn, EntityLivingBase owner, float damage) {
		this(worldIn, owner, damage, 20, 3, 0.4f);
	}

	public FireThrowable(World worldIn, EntityLivingBase owner, float damage, int duration, int particles, float radius) {
		super(worldIn, owner.posX, owner.posY + owner.getEyeHeight() - 0.1, owner.posZ);
		this.setSize(radius, radius);
		this.setNoGravity(true);
		this.owner = owner;
		this.thrower = this.owner;
		this.damage = damage;
		this.duration = duration;
		this.particles = particles;
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

		if (this.ticksExisted >= this.duration || this.inWater) {
			this.setDead();
			return;
		}

		if (this.ticksExisted >= 2 && this.world instanceof WorldServer) {
			WorldServer worldServer = (WorldServer) this.world;
			for(int i = 0; i < particles; i++) {
				double px = this.posX - this.width/2 + this.world.rand.nextFloat()*this.width;
				double py = this.posY + this.world.rand.nextFloat()*this.height;
				double pz = this.posZ - this.width/2 + this.world.rand.nextFloat()*this.width;
				SpawnParticleMessage spm = new SpawnParticleMessage(EnumParticleTypes.FLAME, false, px,py,pz, this.motionX * 0.1f, this.motionY * 0.1f, this.motionZ * 0.1f, 0);
				SkillUtils.sendMessageWithinRange(worldServer, getPosition(), 64, spm);
			}

			AxisAlignedBB expandedBoundingBox = this.getEntityBoundingBox().grow(1, 1, 1);
			worldServer.getEntitiesInAABBexcluding(this.owner, expandedBoundingBox, input -> !this.equals(input)).forEach(this::setEntityOnFire);
		}

	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY && !result.entityHit.equals(this.owner)) {
				this.attackEntityOnDirectHit((EntityLivingBase) result.entityHit);
				this.setDead();
			} else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
				this.setHitBlockOnFire(result);
				if (this.canNotPassThroughHitBlock(result)) {
					if (this.world instanceof WorldServer) {
						WorldServer worldServer = (WorldServer) this.world;
						worldServer.spawnParticle(EnumParticleTypes.FLAME, false, this.posX, this.posY, this.posZ, this.particles * 3, this.width, this.height, this.width, this.rand.nextGaussian() / 10, 0);
					}
					this.setDead();
				}
			}
		}
	}

	private void attackEntityOnDirectHit(EntityLivingBase hitEntity) {
		hitEntity.setFire(5);
		hitEntity.attackEntityFrom(DamageSource.causeMobDamage(owner).setMagicDamage().setFireDamage().setProjectile(), this.damage);
		if (this.owner != null) {
			hitEntity.setLastAttackedEntity(this.owner);
		}
	}

	private boolean canNotPassThroughHitBlock(RayTraceResult result) {
		BlockPos hitBlockPos = result.getBlockPos();
		IBlockState hitState = this.world.getBlockState(hitBlockPos);
		return hitState.getMaterial().blocksMovement();
	}

	private void setHitBlockOnFire(RayTraceResult result) {
		BlockPos positionHit = result.getBlockPos();
		IBlockState fire = Blocks.FIRE.getDefaultState();
		// set the block this is on on fire
		if (fire.getBlock().canPlaceBlockOnSide(this.world, positionHit, result.sideHit) && this.world.getBlockState(result.getBlockPos()).getMaterial().getCanBurn() && (!this.world.getBlockState(positionHit.offset(result.sideHit)).getMaterial().isLiquid())) {
			BlockPos blockPosForFire = positionHit.offset(result.sideHit);
			this.world.setBlockState(blockPosForFire, fire, 11);
		}
	}

	private void setEntityOnFire(@Nullable Entity target) {
		if (!target.isImmuneToFire() && !target.equals(this.owner)) {
			target.setFire(5);
		}
	}

	@Override
	public boolean isInvisible() {
		return true;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}
}
