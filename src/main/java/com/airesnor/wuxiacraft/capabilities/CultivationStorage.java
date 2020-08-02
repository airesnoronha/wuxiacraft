package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
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
		tag.setString("levelBody", instance.getBodyLevel().levelName);
		tag.setString("levelDivine", instance.getDivineLevel().levelName);
		tag.setString("levelEssence", instance.getEssenceLevel().levelName);
		tag.setInteger("subLevelBody", instance.getBodySubLevel());
		tag.setInteger("subLevelDivine", instance.getDivineSubLevel());
		tag.setInteger("subLevelEssence", instance.getEssenceSubLevel());
		tag.setDouble("progressBody", instance.getBodyProgress());
		tag.setDouble("progressDivine", instance.getDivineProgress());
		tag.setDouble("progressEssence", instance.getEssenceProgress());
		tag.setDouble("energy", instance.getEnergy());
		tag.setInteger("pelletCD", instance.getPillCooldown());
		tag.setBoolean("suppress", instance.getSuppress());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICultivation> capability, ICultivation instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setBodyLevel(BaseSystemLevel.getLevelInListByName(BaseSystemLevel.BODY_LEVELS, tag.getString("levelBody")));
		instance.setDivineLevel(BaseSystemLevel.getLevelInListByName(BaseSystemLevel.DIVINE_LEVELS, tag.getString("levelDivine")));
		instance.setEssenceLevel(BaseSystemLevel.getLevelInListByName(BaseSystemLevel.ESSENCE_LEVELS, tag.getString("levelEssence")));
		instance.setBodySubLevel(tag.getInteger("subLevelBody"));
		instance.setDivineSubLevel(tag.getInteger("subLevelDivine"));
		instance.setEssenceSubLevel(tag.getInteger("subLevelEssence"));
		instance.setBodyProgress(tag.getDouble("progressBody"));
		instance.setDivineProgress(tag.getDouble("progressDivine"));
		instance.setEssenceProgress(tag.getDouble("progressEssence"));
		instance.addEnergy(tag.getFloat("energy"));
		instance.setPillCooldown(tag.getInteger("pelletCD"));
		instance.setSuppress(tag.getBoolean("suppress"));
	}
}
