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
        tag.setDouble("barrier-amount", instance.getBarrierAmount());
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
        if (tag.hasKey("barrier-amount")) {
            instance.setBarrierAmount(tag.getDouble("barrier-amount"));
        }
        if (tag.hasKey("barrier-regen-active")) {
            instance.setBarrierRegenActive(tag.getBoolean("barrier-regen-active"));
        }
        if (tag.hasKey("barrier-cooldown")) {
            instance.setBarrierCooldown(tag.getInteger("barrier-cooldown"));
        }
        if (tag.hasKey("barrier-max-cooldown")) {
            instance.setBarrierMaxCooldown(tag.getInteger("barrier-max-cooldown"));
        }
        if (tag.hasKey("barrier-active")) {
            instance.setBarrierActive(tag.getBoolean("barrier-active"));
        }
        if (tag.hasKey("barrier-broken")) {
            instance.setBarrierBroken(tag.getBoolean("barrier-broken"));
        }
        if (tag.hasKey("barrier-hits")) {
            instance.setBarrierHits(tag.getInteger("barrier-hits"));
        }
    }
}
