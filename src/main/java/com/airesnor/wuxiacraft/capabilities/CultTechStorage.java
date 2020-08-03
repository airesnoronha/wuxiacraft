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
		if(instance.getBodyTechnique() != null) {
			tag.setString("tech-body", instance.getBodyTechnique().getTechnique().getUName());
			tag.setDouble("prof-body", instance.getBodyTechnique().getProficiency());
		}
		if(instance.getDivineTechnique() != null) {
			tag.setString("tech-divine", instance.getDivineTechnique().getTechnique().getUName());
			tag.setDouble("prof-divine", instance.getDivineTechnique().getProficiency());
		}
		if(instance.getEssenceTechnique() != null) {
			tag.setString("tech-essence", instance.getEssenceTechnique().getTechnique().getUName());
			tag.setDouble("prof-essence", instance.getEssenceTechnique().getProficiency());
		}
		return tag;
	}

	@Override
	public void readNBT(Capability<ICultTech> capability, ICultTech instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		if(tag.hasKey("tech-body")) {
			Technique t = Techniques.getTechniqueByUName(tag.getString("tech-body"));
			double p = tag.getDouble("prof-body");
			instance.setBodyTechnique(new KnownTechnique(t, p));
		}
		if(tag.hasKey("tech-divine")) {
			Technique t = Techniques.getTechniqueByUName(tag.getString("tech-divine"));
			double p = tag.getDouble("prof-divine");
			instance.setDivineTechnique(new KnownTechnique(t, p));
		}
		if(tag.hasKey("tech-essence")) {
			Technique t = Techniques.getTechniqueByUName(tag.getString("tech-essence"));
			double p = tag.getDouble("prof-essence");
			instance.setEssenceTechnique(new KnownTechnique(t, p));
		}
	}
}