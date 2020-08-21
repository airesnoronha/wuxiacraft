package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.IBarrier;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BarrierStorage implements Capability.IStorage<IBarrier> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IBarrier> capability, IBarrier instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setFloat("barrier-amount", instance.getBarrierAmount());
        tag.setFloat("barrier-regen-rate", instance.getBarrierRegenRate());
        tag.setBoolean("barrier-regen-active", instance.isBarrierRegenActive());
        tag.setInteger("barrier-cooldown", instance.getBarrierCooldown());
        tag.setInteger("barrier-max-cooldown", instance.getBarrierMaxCooldown());
        tag.setBoolean("barrier-active", instance.isBarrierActive());
        tag.setBoolean("barrier-broken", instance.isBarrierBroken());
        tag.setInteger("barrier-hits", instance.getBarrierHits());
        return tag;
    }

    @Override
    public void readNBT(Capability<IBarrier> capability, IBarrier instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setBarrierAmount(tag.getFloat("barrier-amount"));
        instance.setBarrierRegenRate(tag.getFloat("barrier-regen-rate"));
        instance.setBarrierRegenActive(tag.getBoolean("barrier-regen-active"));
        instance.setBarrierCooldown(tag.getInteger("barrier-cooldown"));
        instance.setBarrierMaxCooldown(tag.getInteger("barrier-max-cooldown"));
        instance.setBarrierActive(tag.getBoolean("barrier-active"));
        instance.setBarrierBroken(tag.getBoolean("barrier-broken"));
        instance.setBarrierHits(tag.getInteger("barrier-hits"));
    }
}
