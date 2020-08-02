package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ISealing;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SealingStorage implements Capability.IStorage<ISealing> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISealing> capability, ISealing instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        //noinspection ConstantConditions
        tag.setTag("cultivation", CultivationProvider.CULTIVATION_CAP.getStorage().writeNBT(CultivationProvider.CULTIVATION_CAP, instance.getSealedCultivation(), null));
        tag.setBoolean("bodySeal", instance.isBodySealed());
        tag.setBoolean("divineSeal", instance.isDivineSealed());
        tag.setBoolean("essenceSeal", instance.isEssenceSealed());
        return tag;
    }

    @Override
    public void readNBT(Capability<ISealing> capability, ISealing instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        //noinspection ConstantConditions
        CultivationProvider.CULTIVATION_CAP.getStorage().readNBT(CultivationProvider.CULTIVATION_CAP, instance.getSealedCultivation(), null, tag.getTag("cultivation"));
        instance.setBodySealed(tag.getBoolean("bodySeal"));
        instance.setDivineSealed(tag.getBoolean("divineSeal"));
        instance.setEssenceSealed(tag.getBoolean("essenceSeal"));
    }
}
