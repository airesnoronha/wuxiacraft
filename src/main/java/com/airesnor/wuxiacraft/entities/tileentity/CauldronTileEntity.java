package com.airesnor.wuxiacraft.entities.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class CauldronTileEntity extends TileEntity {

    private boolean hasFirewood;
    private boolean hasWater;
    private boolean isLit;

    public CauldronTileEntity () {
        this.hasFirewood = false;
        this.hasWater = false;
        this.isLit = false;
    }

    public boolean isLit() {
        return isLit;
    }

    public void setLit(boolean lit) {
        isLit = lit;
    }

    public boolean isHasFirewood() {
        return hasFirewood;
    }

    public void setHasFirewood(boolean hasFirewood) {
        this.hasFirewood = hasFirewood;
    }

    public boolean isHasWater() {
        return hasWater;
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setHasFirewood(compound.getBoolean("has_firewood"));
        setHasWater(compound.getBoolean("has_water"));
        setLit(compound.getBoolean("is_lit"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("has_firewood", isHasFirewood());
        compound.setBoolean("has_water", isHasWater());
        compound.setBoolean("is_lit", isLit());
        return compound;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTagCompound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }
}
