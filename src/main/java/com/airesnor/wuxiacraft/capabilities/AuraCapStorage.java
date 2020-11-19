package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.aura.IAuraCap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AuraCapStorage implements Capability.IStorage<IAuraCap> {

	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IAuraCap> capability, IAuraCap instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("size", instance.getAuraLocations().size());
		for (ResourceLocation location : instance.getAuraLocations()) {
			tag.setString("aura-" + instance.getAuraLocations().indexOf(location), location.toString());
		}
		return tag;
	}

	@Override
	public void readNBT(Capability<IAuraCap> capability, IAuraCap instance, EnumFacing side, NBTBase nbt) {

		NBTTagCompound tag = (NBTTagCompound) nbt;
		int size = tag.getInteger("size");
		for (int i = 0; i < size; i++) {
			ResourceLocation location = new ResourceLocation(tag.getString("aura-" + i));
			instance.addAuraLocation(location);
		}
	}
}
