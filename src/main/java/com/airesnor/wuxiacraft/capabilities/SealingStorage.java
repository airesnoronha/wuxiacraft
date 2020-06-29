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
        //Cultivation
        tag.setString("sealed-level", instance.getCurrentLevel().levelName);
        tag.setInteger("sealed-subLevel", instance.getCurrentSubLevel());
        tag.setDouble("sealed-progress", instance.getCurrentProgress());
        tag.setDouble("sealed-energy", instance.getEnergy());

        //Foundation
        tag.setLong("sealed-agility", instance.getAgility());
        tag.setLong("sealed-constitution", instance.getConstitution());
        tag.setLong("sealed-dexterity", instance.getDexterity());
        tag.setLong("sealed-resistance", instance.getResistance());
        tag.setLong("sealed-spirit", instance.getSpirit());
        tag.setLong("sealed-strength", instance.getStrength());
        tag.setDouble("sealed-agility-progress", instance.getAgilityProgress());
        tag.setDouble("sealed-constitution-progress", instance.getConstitutionProgress());
        tag.setDouble("sealed-dexterity-progress", instance.getDexterityProgress());
        tag.setDouble("sealed-resistance-progress", instance.getResistanceProgress());
        tag.setDouble("sealed-spirit-progress", instance.getSpiritProgress());
        tag.setDouble("sealed-strength-progress", instance.getStrengthProgress());

        //Sealing
        tag.setBoolean("sealed-cultivation", instance.isCultivationSealed());
        tag.setBoolean("sealed-foundation", instance.isFoundationSealed());
        return tag;
    }

    @Override
    public void readNBT(Capability<ISealing> capability, ISealing instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        //Cultivation
        instance.setCurrentLevel(CultivationLevel.LOADED_LEVELS.get(tag.getString("sealed-level").toLowerCase())); //for people don't lose their previous cultivation i hope
        instance.setCurrentSubLevel(tag.getInteger("sealed-subLevel"));
        instance.setProgress(tag.getDouble("sealed-progress"));
        instance.setEnergy(tag.getDouble("sealed-energy"));

        //Foundation
        if (tag.hasKey("sealed-agility"))
            instance.setAgility(tag.getLong("sealed-agility"));
        if (tag.hasKey("sealed-constitution"))
            instance.setConstitution(tag.getLong("sealed-constitution"));
        if (tag.hasKey("sealed-dexterity"))
            instance.setDexterity(tag.getLong("sealed-dexterity"));
        if (tag.hasKey("sealed-resistance"))
            instance.setResistance(tag.getLong("sealed-resistance"));
        if (tag.hasKey("sealed-spirit"))
            instance.setSpirit(tag.getLong("sealed-spirit"));
        if (tag.hasKey("sealed-strength"))
            instance.setStrength(tag.getLong("sealed-strength"));
        if (tag.hasKey("sealed-agility-progress"))
            instance.setAgilityProgress(tag.getDouble("sealed-agility-progress"));
        if (tag.hasKey("sealed-constitution-progress"))
            instance.setConstitutionProgress(tag.getDouble("sealed-constitution-progress"));
        if (tag.hasKey("sealed-dexterity-progress"))
            instance.setDexterityProgress(tag.getDouble("sealed-dexterity-progress"));
        if (tag.hasKey("sealed-resistance-progress"))
            instance.setResistanceProgress(tag.getDouble("sealed-resistance-progress"));
        if (tag.hasKey("sealed-spirit-progress"))
            instance.setSpiritProgress(tag.getDouble("sealed-spirit-progress"));
        if (tag.hasKey("sealed-strength-progress"))
            instance.setStrengthProgress(tag.getDouble("sealed-strength-progress"));

        //Sealing
        instance.setSealed("cultivation", tag.getBoolean("sealed-cultivation"));
        instance.setSealed("foundation", tag.getBoolean("sealed-foundation"));
    }
}
