package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.IBarrier;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarrierProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IBarrier.class)
    public static final Capability<IBarrier> BARRIER_CAPABILITY = null;

    private IBarrier instance = BARRIER_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == BARRIER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == BARRIER_CAPABILITY ? BARRIER_CAPABILITY.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return BARRIER_CAPABILITY.getStorage().writeNBT(BARRIER_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        BARRIER_CAPABILITY.getStorage().readNBT(BARRIER_CAPABILITY, this.instance, null, nbt);
    }
}
