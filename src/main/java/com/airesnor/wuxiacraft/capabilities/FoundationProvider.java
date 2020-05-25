package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.IFoundation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FoundationProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IFoundation.class)
	public static final Capability<IFoundation> FOUNDATION_CAPABILITY = null;

	@SuppressWarnings({"FieldMayBeFinal", "ConstantConditions"})
	private IFoundation instance = FOUNDATION_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		//noinspection ConstantConditions
		return capability == FOUNDATION_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		//noinspection ConstantConditions
		return capability == FOUNDATION_CAPABILITY ? FOUNDATION_CAPABILITY.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		//noinspection ConstantConditions
		return FOUNDATION_CAPABILITY.getStorage().writeNBT(FOUNDATION_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		//noinspection ConstantConditions
		FOUNDATION_CAPABILITY.getStorage().readNBT(FOUNDATION_CAPABILITY, instance, null, nbt);
	}
}
