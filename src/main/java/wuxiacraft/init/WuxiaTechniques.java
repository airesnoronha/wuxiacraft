package wuxiacraft.init;

import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.technique.BodyTechnique;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.cultivation.technique.TechniqueModifiers;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class WuxiaTechniques {

	public static final List<Technique> TECHNIQUES = new LinkedList<>();

	//basic ones
	public static final Technique BASIC_BODY_FORGING_MANUAL = new BodyTechnique("basic_body_forging_manual",
			new TechniqueModifiers(0, 0, 0, 0), 4, 34000, 34, 0, 0, 0, 0, 0)
			.addCheckpoint(1000, 0.1f, "Initial Success")
			.addCheckpoint(5000, 0.3f, "Small Success")
			.addCheckpoint(12000, 0.5f, "Intermediate Success")
			.addCheckpoint(21000, 0.7f, "Great Success")
			.addCheckpoint(34000, 1.0f, "Perfection");

	public static final Technique BASIC_QI_GATHERING_TECHNIQUE = new Technique(CultivationLevel.System.ESSENCE, "basic_qi_gathering_technique",
			new TechniqueModifiers(0, 0, 0, 0), 4, 34000, 34, 0, 0, 0, 0)
			.addCheckpoint(1000, 0.1f, "Initial Success")
			.addCheckpoint(5000, 0.3f, "Small Success")
			.addCheckpoint(12000, 0.5f, "Intermediate Success")
			.addCheckpoint(21000, 0.7f, "Great Success")
			.addCheckpoint(34000, 1.0f, "Perfection");

	public static final Technique BASIC_MENTAL_ENERGY_MANIPULATION = new Technique(CultivationLevel.System.DIVINE, "basic_mental_energy_manipulation",
			new TechniqueModifiers(0, 0, 0, 0), 4, 34000, 34, 0, 0, 0, 0)
			.addCheckpoint(1000, 0.1f, "Initial Success")
			.addCheckpoint(5000, 0.3f, "Small Success")
			.addCheckpoint(12000, 0.5f, "Intermediate Success")
			.addCheckpoint(21000, 0.7f, "Great Success")
			.addCheckpoint(34000, 1.0f, "Perfection");

	//Body ones

	public static Technique getTechniqueByName(String name) {
		for (Technique tech : TECHNIQUES) {
			if (tech.getName().equalsIgnoreCase(name)) return tech;
		}
		return null;
	}

}
