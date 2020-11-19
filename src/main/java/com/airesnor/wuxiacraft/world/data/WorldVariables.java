package com.airesnor.wuxiacraft.world.data;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.world.dimensions.WuxiaDimensions;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;

public class WorldVariables extends WorldSavedData {

	private static final String DATA_NAME = WuxiaCraft.MOD_ID + "_variables";

	private int tribulationMultiplier;

	public WorldVariables(int dimId) {
		super(DATA_NAME);
		tribulationMultiplier = 5;
		if(dimId == 0) {
			tribulationMultiplier = 3;
		}
		else if(dimId == WuxiaDimensions.EARTH.getId() ||
			dimId == WuxiaDimensions.FIRE.getId() ||
			dimId == WuxiaDimensions.METAL.getId() ||
			dimId == WuxiaDimensions.WATER.getId() ||
			dimId == WuxiaDimensions.WOOD.getId()) {
			tribulationMultiplier = 2;
		}
	}

	public static WorldVariables get(World world) {

		MapStorage storage = world.getPerWorldStorage();
		WorldVariables instance = (WorldVariables) storage.getOrLoadData(WorldVariables.class, DATA_NAME);
		if(instance == null) {
			instance = new WorldVariables(world.provider.getDimension());
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.tribulationMultiplier = nbt.getInteger("tribulationMultiplier");
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("tribulationMultiplier", this.tribulationMultiplier);
		return compound;
	}

	public int getTribulationMultiplier() {
		return tribulationMultiplier;
	}

	public void setTribulationMultiplier(int tribulationMultiplier) {
		this.tribulationMultiplier = tribulationMultiplier;
		this.markDirty();
	}
}
