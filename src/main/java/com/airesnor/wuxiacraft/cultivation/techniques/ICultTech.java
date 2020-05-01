package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public interface ICultTech {

	boolean addTechnique(Technique technique, double progress);

	void remTechnique(Technique technique);

	void progress(double amount);

	List<KnownTechnique> getKnownTechniques();

	TechniquesModifiers getOverallModifiers();

	List<Skill> getTechniqueSkills();

	float getOverallCultivationSpeed();

	void copyFrom(ICultTech cultTech);

	boolean hasElement(Element element);

}
