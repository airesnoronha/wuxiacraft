package com.airesnor.wuxiacraft.capabilities;

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

public class WuxiaItemInventoryProvider implements ICapabilityProvider, ICapabilitySerializable {

    private final ItemStackHandler inventory;
    private int itemInventorySize;

    public WuxiaItemInventoryProvider(int itemInventorySize) {
        this.itemInventorySize = itemInventorySize;
        inventory = new ItemStackHandler(this.itemInventorySize);
    }

    @Override
    public NBTBase serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("ItemInventoryItems", inventory.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        inventory.deserializeNBT(tag.getCompoundTag("ItemInventoryItems"));
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : null;
    }
}

