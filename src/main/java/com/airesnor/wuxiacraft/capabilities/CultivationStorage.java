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
		tag.setString("level", instance.getCurrentLevel().levelName);
		tag.setInteger("subLevel", instance.getCurrentSubLevel());
		tag.setDouble("progress", instance.getCurrentProgress());
		tag.setDouble("energy", instance.getEnergy());
		tag.setInteger("pelletCD", instance.getPillCooldown());
		tag.setBoolean("suppress", instance.getSuppress());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICultivation> capability, ICultivation instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setCurrentLevel(CultivationLevel.REGISTERED_LEVELS.get(tag.getString("level").toLowerCase())); //for people don't lose their previous cultivation i hope
		instance.setCurrentSubLevel(tag.getInteger("subLevel"));
		instance.setProgress(tag.getDouble("progress"));
		instance.addEnergy(tag.getFloat("energy"));
		instance.setPillCooldown(tag.getInteger("pelletCD"));
		instance.setSuppress(tag.getBoolean("suppress"));
	}
}
