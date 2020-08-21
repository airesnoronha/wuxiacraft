package com.airesnor.wuxiacraft.entities.effects;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EntityLevelUpHalo extends EntityWeatherEffect implements IEntityAdditionalSpawnData {

	@SuppressWarnings("unused")
	public EntityLevelUpHalo(World worldIn) {
		super(worldIn);
	}

	public EntityLevelUpHalo(World worldIn, double x, double y, double z ) {
		super(worldIn);
		this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(!this.world.isRemote && this.ticksExisted == 2) {
			this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 200F, 1.6F + this.rand.nextFloat() * 0.2F);
			this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 20.0F, 1.2F + this.rand.nextFloat() * 0.2F);
		}
		if(this.ticksExisted > 30) {
			this.setDead();
		}
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass ==1 ;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {

	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {

	}
}
