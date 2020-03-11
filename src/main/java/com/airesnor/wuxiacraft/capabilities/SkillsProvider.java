package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SkillsProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ISkillCap.class)
	public static final Capability<ISkillCap> SKILL_CAP_CAPABILITY = null;

	private ISkillCap instance = SKILL_CAP_CAPABILITY.getDefaultInstance();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == SKILL_CAP_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == SKILL_CAP_CAPABILITY ? SKILL_CAP_CAPABILITY.cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return SKILL_CAP_CAPABILITY.getStorage().writeNBT(SKILL_CAP_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		SKILL_CAP_CAPABILITY.getStorage().readNBT(SKILL_CAP_CAPABILITY, this.instance, null, nbt);
	}
}
