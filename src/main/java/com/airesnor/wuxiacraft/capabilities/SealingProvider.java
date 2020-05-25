package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.ISealing;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SealingProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(ISealing.class)
    public static final Capability<ISealing> SEALING_CAPABILITY = null;

    private ISealing instance = SEALING_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SEALING_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == SEALING_CAPABILITY ? SEALING_CAPABILITY.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return SEALING_CAPABILITY.getStorage().writeNBT(SEALING_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        SEALING_CAPABILITY.getStorage().readNBT(SEALING_CAPABILITY, this.instance, null, nbt);
    }
}
