package com.airesnor.wuxiacraft.items;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpaceRingProvider implements ICapabilityProvider, ICapabilitySerializable {

    private final ItemStackHandler inventory;

    public SpaceRingProvider() {
        inventory = new ItemStackHandler(9);
    }

    @Override
    public NBTBase serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("SpaceItems", inventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        inventory.deserializeNBT(tag.getCompoundTag("SpaceItems"));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        }
        return null;
    }
}

