package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.blocks.Cauldron;
import com.airesnor.wuxiacraft.networking.SpawnParticleMessage;
import com.airesnor.wuxiacraft.utils.SkillUtils;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.Random;

public class CauldronTileEntity extends TileEntity implements ITickable {

	private boolean hasFirewood;
	private boolean hasWater;

	private boolean isLit;
	private float timeLit;

	private float burnSpeed;
	private float temperature;

	private float maxTemperature;

	public CauldronTileEntity() {
		this.hasFirewood = false;
		this.hasWater = false;
		this.isLit = false;
		this.burnSpeed = 1f;
		this.temperature = 30f;
		this.timeLit = 0;
		this.maxTemperature = 1300f;
	}

	public float getTimeLit() {
		return timeLit;
	}

	public float getBurnSpeed() {
		return burnSpeed;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getMaxTemperature() {
		return maxTemperature;
	}

	public CauldronTileEntity setMaxTemperature(float maxTemperature) {
		this.maxTemperature = maxTemperature;
		return this;
	}

	public void setTemperature(float temperature) {
		this.temperature = Math.min(this.maxTemperature,Math.max(30f,temperature));
	}

	public boolean isLit() {
		return isLit;
	}

	public void setLit(boolean lit) {
		isLit = lit;
	}

	public boolean isHasFirewood() {
		return hasFirewood;
	}

	public void setHasFirewood(boolean hasFirewood) {
		this.hasFirewood = hasFirewood;
	}

	public boolean isHasWater() {
		return hasWater;
	}

	public void setHasWater(boolean hasWater) {
		this.hasWater = hasWater;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setHasFirewood(compound.getBoolean("has_firewood"));
		setHasWater(compound.getBoolean("has_water"));
		setLit(compound.getBoolean("is_lit"));
		timeLit = (compound.getFloat("lit_time"));
		setTemperature(compound.getFloat("temperature"));
		burnSpeed = compound.getFloat("burn_speed");

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("has_firewood", isHasFirewood());
		compound.setBoolean("has_water", isHasWater());
		compound.setBoolean("is_lit", isLit());
		compound.setFloat("lit_time", timeLit);
		compound.setFloat("temperature", temperature);
		compound.setFloat("burn_speed", burnSpeed);
		return compound;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTagCompound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void update() {
		IBlockState state = this.world.getBlockState(getPos());
		if (isLit()) {
			world.setLightFor(EnumSkyBlock.BLOCK, getPos(), Math.min(15, (int) this.burnSpeed));
			this.timeLit -= this.burnSpeed;
			this.temperature += this.burnSpeed;
			this.burnSpeed = Math.max(1f, this.burnSpeed-0.1f);
			if (this.timeLit < 0) {
				this.resetBurnSettings();
			}
			if(!world.isRemote) {
				Random rand = new Random();
				int qty = (int)(2f * this.burnSpeed);
				for(int i = 0; i < qty; i++) {
					float x = getPos().getX() + 0.5f + (2*rand.nextFloat()-1f)*0.3f;
					float z = getPos().getZ() + 0.5f + (2*rand.nextFloat()-1f)*0.3f;
					float y = getPos().getY();
					SpawnParticleMessage spm = new SpawnParticleMessage(EnumParticleTypes.FLAME, false, x,y,z,0,0.01f,0,0);
					SkillUtils.sendMessageWithinRange((WorldServer) world, getPos(), 16, spm);
				}
			}
		} else {
			world.setLightFor(EnumSkyBlock.BLOCK, getPos(), 0);
		}
		setTemperature(this.temperature*0.99f);
		checkLightAround();
	}

	public void wiggleFan(float strength, float maxFanStrength) {
		this.burnSpeed += Math.min(this.burnSpeed + strength, maxFanStrength);
	}

	public void setOnFire() {
		this.isLit = true;
		this.timeLit = 20000f;
		this.burnSpeed = 1;
	}

	private void resetBurnSettings() {
		this.isLit = false;
		this.burnSpeed = 1f;
		this.timeLit = 0;
	}

	public void checkLightAround() {
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().up());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().down());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().north());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().east());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().south());
		getWorld().checkLightFor(EnumSkyBlock.BLOCK, getPos().west());
		getWorld().markBlockRangeForRenderUpdate(getPos(), new BlockPos(16, 16, 16));
	}

	public void prepareToDie() {
		world.setLightFor(EnumSkyBlock.BLOCK, getPos(), 0);
		checkLightAround();
	}

}
