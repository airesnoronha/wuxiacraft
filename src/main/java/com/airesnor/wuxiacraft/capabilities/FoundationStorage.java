package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.IFoundation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class FoundationStorage implements Capability.IStorage<IFoundation> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IFoundation> capability, IFoundation instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("agility", instance.getAgility());
		tag.setLong("constitution", instance.getConstitution());
		tag.setLong("dexterity", instance.getDexterity());
		tag.setLong("resistance", instance.getResistance());
		tag.setLong("spirit", instance.getSpirit());
		tag.setLong("strength", instance.getStrength());
		tag.setInteger("selected", instance.getSelectedAttribute());
		tag.setDouble("agility-progress", instance.getAgilityProgress());
		tag.setDouble("constitution-progress", instance.getConstitutionProgress());
		tag.setDouble("dexterity-progress", instance.getDexterityProgress());
		tag.setDouble("resistance-progress", instance.getResistanceProgress());
		tag.setDouble("spirit-progress", instance.getSpiritProgress());
		tag.setDouble("strength-progress", instance.getStrengthProgress());
		return tag;
	}

	@Override
	public void readNBT(Capability<IFoundation> capability, IFoundation instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		if (tag.hasKey("agility"))
			instance.setAgility(tag.getLong("agility"));
		if (tag.hasKey("constitution"))
			instance.setConstitution(tag.getLong("constitution"));
		if (tag.hasKey("dexterity"))
			instance.setDexterity(tag.getLong("dexterity"));
		if (tag.hasKey("resistance"))
			instance.setResistance(tag.getLong("resistance"));
		if (tag.hasKey("spirit"))
			instance.setSpirit(tag.getLong("spirit"));
		if (tag.hasKey("strength"))
			instance.setStrength(tag.getLong("strength"));
		if (tag.hasKey("selected"))
			instance.selectAttribute(tag.getInteger("selected"));
		if (tag.hasKey("agility-progress"))
			instance.setAgilityProgress(tag.getDouble("agility-progress"));
		if (tag.hasKey("constitution-progress"))
			instance.setConstitutionProgress(tag.getDouble("constitution-progress"));
		if (tag.hasKey("dexterity-progress"))
			instance.setDexterityProgress(tag.getDouble("dexterity-progress"));
		if (tag.hasKey("resistance-progress"))
			instance.setResistanceProgress(tag.getDouble("resistance-progress"));
		if (tag.hasKey("spirit-progress"))
			instance.setSpiritProgress(tag.getDouble("spirit-progress"));
		if (tag.hasKey("strength-progress"))
			instance.setStrengthProgress(tag.getDouble("strength-progress"));
	}
}
