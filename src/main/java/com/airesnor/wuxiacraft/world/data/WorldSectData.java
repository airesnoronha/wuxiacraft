package com.airesnor.wuxiacraft.world.data;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.world.Sect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class WorldSectData extends WorldSavedData {

    private static final String DATA_NAME = WuxiaCraft.MOD_ID + "_SectData";
    public static List<Sect> SECTS;

    public WorldSectData() {
        super(DATA_NAME);
        SECTS = new ArrayList<>();
    }

    public WorldSectData(String name) {
        super(name);
        SECTS = new ArrayList<>();
    }

    public static WorldSectData get(World world) {
        MapStorage storage = world.getMapStorage();
        WorldSectData instance = (WorldSectData) storage.getOrLoadData(WorldSectData.class, DATA_NAME);
        if (instance == null) {
            instance = new WorldSectData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    public void saveChanges() {
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        SECTS.clear();
        NBTTagList tagList = (NBTTagList) nbt.getTag("sects");
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            Sect sect = new Sect();
            SECTS.add(sect.readFromNBT(tagCompound));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList tagList = new NBTTagList();
        for (Sect sect : SECTS) {
            tagList.appendTag(sect.writeToNBT());
        }
        compound.setTag("sects", tagList);
        return compound;
    }
}
