package com.airesnor.wuxiacraft.entities.skills;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public class FireThowable extends EntityThrowable {

    private EntityLivingBase owner;

    private float damage;

    public FireThowable(World worldIn) {
        super(worldIn);
    }

    public FireThowable(World worldIn, EntityPlayer owner, float damage) {
        super(worldIn, owner.posX, owner.posY + owner.getEyeHeight() - 0.1, owner.posZ);
        this.setSize(0.3f, 0.3f);
        this.setNoGravity(true);
        this.owner = owner;
        this.thrower = this.owner;
        this.damage = damage;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.ticksExisted >= 15 || this.inWater) {
            this.setDead();
            return;
        }

        if(this.ticksExisted >= 2 && this.world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) this.world;
            worldServer.spawnParticle(EnumParticleTypes.FLAME, false, this.posX, this.posY, this.posZ, 3, this.motionX, 0, 0, 0.005d, 0);

            AxisAlignedBB expandedBoundingBox = this.getEntityBoundingBox().grow(1, 1, 1);
            worldServer.getEntitiesInAABBexcluding(this.owner, expandedBoundingBox, input -> !this.equals(input)).forEach(this::setEntityOnFire);
        }

    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(!this.world.isRemote) {
            if(result.typeOfHit == RayTraceResult.Type.ENTITY && !result.entityHit.equals(this.owner)) {
                this.attackEntityOnDirectHit((EntityLivingBase) result.entityHit);
                this.setDead();
            } else if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
                this.setHitBlockOnFire(result);
                if(this.canNotPassThroughHitBlock(result)) {
                    if(this.world instanceof WorldServer) {
                        WorldServer worldServer = (WorldServer) this.world;
                        worldServer.spawnParticle(EnumParticleTypes.FLAME, false, this.posX, this.posY, this.posZ, 10, this.width, this.height, this.width, this.rand.nextGaussian() / 10, 0);
                    }
                    this.setDead();
                }
            }
        }
    }

    private void attackEntityOnDirectHit(EntityLivingBase hitEntity) {
        hitEntity.setFire(5);
        hitEntity.attackEntityFrom(DamageSource.IN_FIRE, this.damage);
        if(this.owner != null) {
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
        if(fire.getBlock().canPlaceBlockOnSide(this.world, positionHit, result.sideHit) && this.world.getBlockState(result.getBlockPos()).getMaterial().getCanBurn() && (!this.world.getBlockState(positionHit.offset(result.sideHit)).getMaterial().isLiquid())) {
            BlockPos blockPosForFire = positionHit.offset(result.sideHit);
            this.world.setBlockState(blockPosForFire, fire, 11);
        }
    }

    private void setEntityOnFire(@Nullable Entity target) {
        if(!target.isImmuneToFire() && !target.equals(this.owner)) {
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
