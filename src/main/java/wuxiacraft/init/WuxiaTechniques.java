package wuxiacraft.init;

import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.cultivation.technique.TechniqueModifiers;

import java.util.LinkedList;
import java.util.List;

public class WuxiaTechniques {

	public static final List<Technique> TECHNIQUES = new LinkedList<>();

	public static final Technique BASIC_BODY_FORGING_MANUAL = new Technique(CultivationLevel.System.ESSENCE, "BASIC_BODY_FORGING_MANUAL",
			new TechniqueModifiers(0, 0, 0, 0, 0), 1, 34000, 34, 0, 0, 0, 0);
	public static final Technique BASIC_QI_GATHERING_TECHNIQUE = new Technique(CultivationLevel.System.ESSENCE, "basic_qi_gathering_technique",
			new TechniqueModifiers(0, 0, 0, 0, 0), 1, 34000, 34, 0, 0, 0, 0);
	public static final Technique BASIC_MENTAL_ENERGY_ = new Technique(CultivationLevel.System.ESSENCE, "basic_qi_gathering_technique",
			new TechniqueModifiers(0, 0, 0, 0, 0), 1, 34000, 34, 0, 0, 0, 0);

	public static Technique getTechniqueByName(String name) {
		return null;
	}

}
