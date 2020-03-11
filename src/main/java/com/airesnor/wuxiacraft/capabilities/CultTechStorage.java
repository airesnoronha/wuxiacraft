package com.airesnor.wuxiacraft.capabilities;

import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CultTechStorage implements Capability.IStorage<ICultTech> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<ICultTech> capability, ICultTech instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("length", instance.getKnownTechniques().size());
		int i = 0;
		for (KnownTechnique t : instance.getKnownTechniques()) {
			tag.setString("tech-" + i, t.getTechnique().getUName());
			tag.setFloat("prog-" + i, t.getProgress());
			i++;
		}
		return tag;
	}

	@Override
	public void readNBT(Capability<ICultTech> capability, ICultTech instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		int i = tag.getInteger("length");
		for (int j = 0; j < i; j++) {
			Technique t = Techniques.getTechniqueByUName(tag.getString("tech-" + j));
			if (t != null) {
				float progress = tag.getFloat("prog-" + j);
				instance.addTechnique(t, progress);
			}
		}
	}
}