package wuxiacraft.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.KnownTechnique;
import wuxiacraft.cultivation.SystemStats;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.init.WuxiaTechniques;

public class CultivationStorage implements Capability.IStorage<ICultivation> {

	@Nullable
	@Override
	public INBT writeNBT(Capability<ICultivation> capability, ICultivation instance, Direction side) {
		CompoundNBT tag = new CompoundNBT();
		//body stats
		SystemStats stats = instance.getStatsBySystem(CultivationLevel.System.BODY);
		tag.putString("bodyLevel", stats.getLevel().levelName);
		tag.putInt("bodySubLevel", stats.getSubLevel());
		tag.putDouble("bodyBase", stats.getBase());
		tag.putDouble("bodyFoundation", stats.getFoundation());
		//essence stats
		stats = instance.getStatsBySystem(CultivationLevel.System.DIVINE);
		tag.putString("divineLevel", stats.getLevel().levelName);
		tag.putInt("divineSubLevel", stats.getSubLevel());
		tag.putDouble("divineBase", stats.getBase());
		tag.putDouble("divineFoundation", stats.getFoundation());
		//divine stats
		stats = instance.getStatsBySystem(CultivationLevel.System.ESSENCE);
		tag.putString("essenceLevel", stats.getLevel().levelName);
		tag.putInt("essenceSubLevel", stats.getSubLevel());
		tag.putDouble("essenceBase", stats.getBase());
		tag.putDouble("essenceFoundation", stats.getFoundation());

		//body known technique
		KnownTechnique knownTechnique = instance.getTechniqueBySystem(CultivationLevel.System.BODY);
		if(knownTechnique != null) {
			tag.putString("bodyTechName", knownTechnique.getTechnique().getName());
			tag.putDouble("bodyProficiency", knownTechnique.getProficiency());
		}
		//divine known technique
		knownTechnique = instance.getTechniqueBySystem(CultivationLevel.System.DIVINE);
		if(knownTechnique != null) {
			tag.putString("divineTechName", knownTechnique.getTechnique().getName());
			tag.putDouble("divineProficiency", knownTechnique.getProficiency());
		}
		//essence known technique
		knownTechnique = instance.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
		if(knownTechnique != null) {
			tag.putString("essenceTechName", knownTechnique.getTechnique().getName());
			tag.putDouble("essenceProficiency", knownTechnique.getProficiency());
		}

		//energy
		tag.putDouble("energy", instance.getEnergy());
		tag.putDouble("health", instance.getHP());

		return tag;
	}

	@Override
	public void readNBT(Capability<ICultivation> capability, ICultivation instance, Direction side, INBT nbt) {
		CompoundNBT tag = (CompoundNBT) nbt;
		//body stats
		instance.getStatsBySystem(CultivationLevel.System.BODY).setLevel(CultivationLevel.getLevelInListByName(CultivationLevel.BODY_LEVELS, tag.getString("bodyLevel")));
		instance.getStatsBySystem(CultivationLevel.System.BODY).setSubLevel(tag.getInt("bodySubLevel"));
		instance.getStatsBySystem(CultivationLevel.System.BODY).setBase(tag.getDouble("bodyBase"));
		instance.getStatsBySystem(CultivationLevel.System.BODY).setFoundation(tag.getDouble("bodyFoundation"));
		//divine stats
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setLevel(CultivationLevel.getLevelInListByName(CultivationLevel.BODY_LEVELS, tag.getString("divineLevel")));
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setSubLevel(tag.getInt("divineSubLevel"));
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setBase(tag.getDouble("divineBase"));
		instance.getStatsBySystem(CultivationLevel.System.DIVINE).setFoundation(tag.getDouble("divineFoundation"));
		//essence stats
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setLevel(CultivationLevel.getLevelInListByName(CultivationLevel.BODY_LEVELS, tag.getString("essenceLevel")));
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setSubLevel(tag.getInt("essenceSubLevel"));
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setBase(tag.getDouble("essenceBase"));
		instance.getStatsBySystem(CultivationLevel.System.ESSENCE).setFoundation(tag.getDouble("essenceFoundation"));

		//body known technique
		if(tag.contains("bodyTechName")) {
			Technique technique = WuxiaTechniques.getTechniqueByName(tag.getString("bodyTechName"));
			instance.setKnownTechnique(technique, tag.getDouble("bodyProficiency"));
		}
		//divine known technique
		if(tag.contains("divineTechName")) {
			Technique technique = WuxiaTechniques.getTechniqueByName(tag.getString("divineTechName"));
			instance.setKnownTechnique(technique, tag.getDouble("divineProficiency"));
		}
		//essence known technique
		if(tag.contains("essenceTechName")) {
			Technique technique = WuxiaTechniques.getTechniqueByName(tag.getString("essenceTechName"));
			instance.setKnownTechnique(technique, tag.getDouble("essenceProficiency"));
		}

		//energy
		instance.setEnergy(tag.getDouble("energy"));
		instance.setHP(tag.getDouble("health"));

	}
}
