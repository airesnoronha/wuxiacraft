package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.combat.IPlayerStats;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerStatsProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IPlayerStats.class)
    public static final Capability<IPlayerStats> STATS_CAPABILITY = null;

    private IPlayerStats instance = STATS_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == STATS_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == STATS_CAPABILITY ? STATS_CAPABILITY.cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return STATS_CAPABILITY.getStorage().writeNBT(STATS_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        STATS_CAPABILITY.getStorage().readNBT(STATS_CAPABILITY, this.instance, null, nbt);
    }
}
