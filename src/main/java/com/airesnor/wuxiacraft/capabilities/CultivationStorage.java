package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CultivationStorage implements Capability.IStorage<ICultivation> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<ICultivation> capability, ICultivation instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("level", instance.getCurrentLevel().name());
		tag.setInteger("subLevel", instance.getCurrentSubLevel());
		tag.setInteger("progress", (int)instance.getCurrentProgress());
		tag.setInteger("energy", (int)instance.getEnergy());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICultivation> capability, ICultivation instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setCurrentLevel(CultivationLevel.valueOf(tag.getString("level")));
		instance.setCurrentSubLevel(tag.getInteger("subLevel"));
		instance.addProgress(tag.getInteger("progress"));
		instance.addEnergy(tag.getInteger("energy"));
	}
}
