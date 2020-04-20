package com.airesnor.wuxiacraft.entities.skills;

import com.airesnor.wuxiacraft.utils.TreeUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WaterBladeThrowable extends EntityThrowable {

	private EntityLivingBase owner;

	private float damage;

	private int duration;

	public float prevRotationRoll;
	public float rotationRoll;

	@SuppressWarnings("unused")
	public WaterBladeThrowable(World worldIn) {
		super(worldIn);
	}

	@SuppressWarnings("unused")
	public WaterBladeThrowable(World worldIn, EntityLivingBase owner, float damage) {
		this(worldIn, owner, damage, 30);
	}


	public WaterBladeThrowable(World worldIn, EntityLivingBase owner, float damage, int duration) {
		super(worldIn, owner.posX, owner.posY + owner.getEyeHeight() - 0.1, owner.posZ);
		this.setSize(1f, 0.05f);
		this.setNoGravity(true);
		this.owner = owner;
		this.thrower = this.owner;
		this.damage = damage;
		this.duration = duration;
		this.rotationRoll = -90f + this.world.rand.nextFloat()*180f;
		this.prevRotationRoll = rotationRoll;
	}

	@Override
	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
		this.posX -= 0.25 * Math.sin(this.rotationYaw / 180f);
		this.posZ -= 0.25 * Math.cos(this.rotationYaw / 180f);
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
				if (TreeUtils.isLog(this.world, result.getBlockPos())) {
					this.world.destroyBlock(result.getBlockPos(), true);
				}
				else if (this.world.getBlockState(result.getBlockPos()).getBlock().equals(Blocks.CACTUS)) {
					this.world.destroyBlock(result.getBlockPos(), true);
				}
				else if(this.world.getBlockState(result.getBlockPos()).getBlock().equals(Blocks.GRASS)) {
					this.world.setBlockState(result.getBlockPos(), Blocks.DIRT.getDefaultState());
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
			worldServer.spawnParticle(EnumParticleTypes.WATER_DROP, false, this.posX, this.posY, this.posZ, 15, this.width, this.height, this.width, 0.005d, 0);
		}

		this.rotateRoll();

		if(this.world instanceof WorldServer) {
			WorldServer worldServer = (WorldServer) this.world;
			if(worldServer.getBlockState(this.getPosition()).getBlock() == Blocks.FIRE) {
				this.world.setBlockToAir(this.getPosition());
				this.setDead();
			}
		}
	}

	private void rotateRoll() {
		this.prevRotationRoll = this.rotationRoll;
		this.rotationRoll += 2.5f;
		if(this.rotationRoll > -180f) this.rotationRoll -= 360f;
	}
}
