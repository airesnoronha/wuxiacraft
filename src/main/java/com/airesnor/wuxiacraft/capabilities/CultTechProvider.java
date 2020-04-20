package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CultTechProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICultTech.class)
	public static final Capability<ICultTech> CULT_TECH_CAPABILITY = null;

	@SuppressWarnings({"ConstantConditions", "FieldMayBeFinal"})
	private ICultTech instance = CULT_TECH_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		//noinspection ConstantConditions
		return capability == CULT_TECH_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		//noinspection ConstantConditions
		return capability == CULT_TECH_CAPABILITY ? CULT_TECH_CAPABILITY.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		//noinspection ConstantConditions
		return CULT_TECH_CAPABILITY.getStorage().writeNBT(CULT_TECH_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		//noinspection ConstantConditions
		CULT_TECH_CAPABILITY.getStorage().readNBT(CULT_TECH_CAPABILITY, this.instance, null, nbt);
	}
}