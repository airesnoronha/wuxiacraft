package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.aura.IAuraCap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AuraCapProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IAuraCap.class)
	public static final Capability<IAuraCap> AURA_CAPABILITY = null;

	@SuppressWarnings({"ConstantConditions", "FieldMayBeFinal"})
	private IAuraCap instance = AURA_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		//noinspection ConstantConditions
		return capability == AURA_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		//noinspection ConstantConditions
		return capability == AURA_CAPABILITY ? AURA_CAPABILITY.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		//noinspection ConstantConditions
		return AURA_CAPABILITY.getStorage().writeNBT(AURA_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		//noinspection ConstantConditions
		AURA_CAPABILITY.getStorage().readNBT(AURA_CAPABILITY, this.instance, null, nbt);
	}
}
