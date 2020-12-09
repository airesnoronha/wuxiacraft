package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.combat.IPlayerStats;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

public class PlayerStatsStorage implements Capability.IStorage<IPlayerStats> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IPlayerStats> capability, IPlayerStats instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("stats.hp", instance.getMaxHP());
        tag.setDouble("stats.max_hp", instance.getMaxHP());
        tag.setDouble("stats.armor", instance.getMaxHP());
        tag.setDouble("stats.strength", instance.getMaxHP());
        tag.setDouble("stats.movement_speed", instance.getMaxHP());
        tag.setDouble("stats.attack_speed", instance.getMaxHP());
        tag.setInteger("stats.resistances-count", instance.getResistances().size());
        for (Pair<Element, Double> pair : instance.getResistances()) {
            tag.setInteger("stats.elements.element-" + instance.getResistances().indexOf(pair), Element.ELEMENTS.indexOf(pair.getKey()));
            tag.setDouble("stats.elements.resistance-" + instance.getResistances().indexOf(pair), pair.getValue());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerStats> capability, IPlayerStats instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = ((NBTTagCompound) nbt);
        if (tag.hasKey("stats.hp")) {
            instance.setHP(tag.getDouble("stats.hp"));
        }
        if (tag.hasKey("stats.max_hp")) {
            instance.setHP(tag.getDouble("stats.max_hp"));
        }
        if (tag.hasKey("stats.armor")) {
            instance.setHP(tag.getDouble("stats.armor"));
        }
        if (tag.hasKey("stats.movement_speed")) {
            instance.setHP(tag.getDouble("stats.movement_speed"));
        }
        if (tag.hasKey("stats.strength")) {
            instance.setHP(tag.getDouble("stats.strength"));
        }
        if (tag.hasKey("stats.movement_speed")) {
            instance.setHP(tag.getDouble("stats.movement_speed"));
        }
        if (tag.hasKey("stats.attack_speed")) {
            instance.setHP(tag.getDouble("stats.attack_speed"));
        }
        if (tag.hasKey("stats.resistances-count")) {
            int count = tag.getInteger("stats.resistances-count");
            for (int i = 0; i < count; i++) {
                Element element = Element.ELEMENTS.get(tag.getInteger("stats.elements.element-" + i));
                instance.addElementResistance(element, tag.getDouble("stats.elements.resistance-" + i));
            }
        }
    }
}
