package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public interface ICultTech {

	boolean addTechnique(Technique technique, float progress);

	void remTechnique(Technique technique);

	void updateTechniques(EntityPlayer player, ICultivation cultivation);

	void progress(float amount);

	List<KnownTechnique> getKnownTechniques();

	TechniquesModifiers getOverallModifiers();

	List<Skill> getTechniqueSkills();

	float getOverallCultivationSpeed();

}
