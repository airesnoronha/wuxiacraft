package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.aura.IAuraCap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class AuraCapStorage implements Capability.IStorage<IAuraCap> {

	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IAuraCap> capability, IAuraCap instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IAuraCap> capability, IAuraCap instance, EnumFacing side, NBTBase nbt) {

	}
}
