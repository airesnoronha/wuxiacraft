package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CultivationProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ICultivation.class)
	public static final Capability<ICultivation> CULTIVATION_CAP = null;

	private ICultivation instance = CULTIVATION_CAP.getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CULTIVATION_CAP;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CULTIVATION_CAP ? CULTIVATION_CAP.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return CULTIVATION_CAP.getStorage().writeNBT(CULTIVATION_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CULTIVATION_CAP.getStorage().readNBT(CULTIVATION_CAP, this.instance, null, nbt);
	}
}
