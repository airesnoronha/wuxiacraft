package wuxiacraft.init;

import net.minecraft.potion.Effects;
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

	// TODO -- Add a "pagoda" above players head while cultivating
	//From Against the gods
	//Rank immortal3
	public static final Technique GREAT_WAY_OF_THE_BUDDHA = new Technique(CultivationLevel.System.BODY, "great_way_of_the_buddha",
			new TechniqueModifiers(0, 0, 0, 0), 4, 34000, 34, 0, 0, 0, 0)
			.addCheckpoint(4500.0 * 8, 0.08f, "First Stage")
			.addCheckpoint(20300.0 * 8, 0.16f, "Second Stage")
			.addCheckpoint(91470.0 * 8, 0.24f, "Third Stage")
			.addCheckpoint(412130.0 * 8, 0.32f, "Fourth Stage")
			.addCheckpoint(1856900.0 * 8, 0.40f, "Fifth Stage")
			.addCheckpoint(8366600.0 * 8, 0.48f, "Sixth Stage")
			.addCheckpoint(37697000.0 * 8, 0.56f, "Seventh Stage")
			.addCheckpoint(169849800.0 * 8, 0.64f, "Eighth Stage")
			.addCheckpoint(765284900.0 * 8, 0.72f, "Ninth Stage")
			.addCheckpoint(3448111300.0 * 8, 0.8f, "Tenth Stage")
			.addCheckpoint(15536006750.0 * 8, 0.88f, "Eleventh Stage")
			.addCheckpoint(70000000000.0 * 8, 1f, "Twelfth Stage")
			.addEffect(4500 * 16, Effects.REGENERATION);

	//From Rebirth of the Urban Immortal Cultivator
	//rank immortal2
	public static final Technique AZURE_THEARCH_LONGEVITY_BODY = new Technique(CultivationLevel.System.BODY, "azure_thearch_longevity_body",
			new TechniqueModifiers(0, 0, 0, 0), 4, 1500000000.0 * 8, 34, 0, 0, 0, 0)
			.addCheckpoint(5400 * 8, 0.15f, "Initial Success")
			.addCheckpoint(30200 * 8, 0.30f, "Connate Body")
			.addCheckpoint(165400 * 8, 0.45f, "Great Success")
			.addCheckpoint(907300 * 8, 0.60f, "Phenomenon Success")
			.addCheckpoint(4979800 * 8, 0.75f, "Divine Body")
			.addCheckpoint(27330630 * 8, 0.9f, "Deity body")
			.addCheckpoint(1500000000.0 * 8, 1f, "Thearch body")
			.addElement(WuxiaElements.WOOD);

	//From Wuxia4You
	public static final Technique YIN_BODY_ART = new Technique(CultivationLevel.System.BODY, "yin_body_art",
			new TechniqueModifiers(0, 0, 0, 0), 4, 1500000000.0 * 8, 34, 0, 0, 0, 0)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(WuxiaElements.ICE);

	//From Wuxia4You
	public static final Technique LIGHTNING_THUNDER_BODY_ART = new Technique(CultivationLevel.System.BODY, "lightning_thunder_body_art",
			new TechniqueModifiers(0, 0, 0, 0), 4, 190000000.0 * 8, 34, 0, 0, 0, 0)
			.addCheckpoint(4570 * 8, 0.125f, "Lightning Cell Nourishment")
			.addCheckpoint(20890 * 8, 0.25f, "Lightning Bolt Muscles")
			.addCheckpoint(95400 * 8, 0.375f, "Thunder Blood Marrow")
			.addCheckpoint(435900 * 8, 0.5f, "Thunder Bones")
			.addCheckpoint(1991700 * 8, 0.625f, "Semi Lightning Thunder Body")
			.addCheckpoint(9100500 * 8, 0.75f, "Lightning Tendon Enhancement ")
			.addCheckpoint(41582360 * 8, 0.875f, "Thunder Meridians Nourishment")
			.addCheckpoint(190000000.0 * 8, 1f, "Complete Lightning Thunder Body")
			.addElement(WuxiaElements.LIGHTNING);

	// TODO -- Add a golden buddha statue
	//From HuoYuhao -- Keep reminding myself he's a fire cultivator o.O
	public static final Technique MERCIFUL_PALM_OF_BUDDHA = new Technique(CultivationLevel.System.BODY, "merciful_palm_of_buddha",
			new TechniqueModifiers(0.3, 0.3, 1.8, 0.8), 3.8, 23000000.0 * 8, 800, 0, 0, 0, 0)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected");


	// TODO -- model transformations
	// TODO -- Painful to change a body transformation art
	//From Asura
	//rank immortal 2
	public static final Technique PHOENIX_BODY_ART = new Technique(CultivationLevel.System.BODY, "phoenix_body_art",
			new TechniqueModifiers(0, 0, 0, 0), 7.6, 10370000000.0 * 8, 176722.67, 0, 0, 0, 0)
			.addCheckpoint(6020.0 * 8, 0.11f, "Blood Cleaning")
			.addCheckpoint(36300.0 * 8, 0.22f, "Crimson Blood")
			.addCheckpoint(218100.0 * 8, 0.33f, "Black Blood")
			.addCheckpoint(1312600.0 * 8, 0.44f, "Bone Refining Stage 1")
			.addCheckpoint(79000500.0 * 8, 0.55f, "Bone Refining Stage 2")
			.addCheckpoint(47553900.0 * 8, 0.66f, "Bone Refining Stage 3")
			.addCheckpoint(286231100.0 * 8, 0.77f, "Full Phoenix Body")
			.addCheckpoint(1722851250.0 * 8, 0.88f, "True Phoenix Body")
			.addCheckpoint(10370000000.0 * 8, 1f, "Reborn By Fire") //TODO -- set player on fire
			.addElement(WuxiaElements.FIRE);


	//Essence ones
	//From Wuxia4You suggestion
	// from the amount of levels, suffer a downgrade to mortal 2, really
	public static final Technique WIDE_WATER_FLOW = new Technique(CultivationLevel.System.ESSENCE, "wide_water_flow",
			new TechniqueModifiers(0.9, 0.3, 1.2, 0.5), 4.2, 320000.0 * 8, 120, 0, 0, 0, 0)
			.addCheckpoint(1000.0 * 8, 0.1f, "Elemental Perception")
			.addCheckpoint(10000.0 * 8, 0.3f, "Element Release")
			.addCheckpoint(50000.0 * 8, 0.5f, "Meridian Widening")
			.addCheckpoint(150000.0 * 8, 0.7f, "Water Flow")
			.addCheckpoint(320000.0 * 8, 1f, "Wide Water Flow")
			.addElement(WuxiaElements.WATER);

	//From Dremtas White Tiger
	public static final Technique BASIC_LIGHTNING_GUIDE = new Technique(CultivationLevel.System.ESSENCE, "basic_lightning_guide",
			new TechniqueModifiers(0.2, 1.2, 0.9, 1.8), 3.4, 23000000.0 * 8, 800, 0, 0, 0, 0)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(WuxiaElements.LIGHTNING);

	//From Febian
	public static final Technique FOREST_HEART_MANUAL = new Technique(CultivationLevel.System.ESSENCE, "forest_heart_manual",
			new TechniqueModifiers(0.9, 0.6, 1.8, 0.6), 1.7, 23000000.0 * 8, 800, 0, 0, 0, 0)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(WuxiaElements.WOOD)
			.addCompatible(WuxiaTechniques.BLOOMING_FOREST_ART);

	//From Anime4You -- former Wuxia4You (liked this one better)
	//rank immortal 2
	public static final Technique BLOOMING_FOREST_ART = new Technique( CultivationLevel.System.ESSENCE, "blooming_forest_art",
			new TechniqueModifiers(1.8, 0.6, 2.2, 0.8), 8.1, 561000000.0 * 8, 80000.0, 0, 0, 0, 0)
			.addCheckpoint(4276.0 * 8, 0.15f, "Grass Land")
			.addCheckpoint(30485.0 * 8, 0.30f, "Flower Land")
			.addCheckpoint(217305.0 * 8, 0.45f, "Small Tree")
			.addCheckpoint(1548972.0 * 8, 0.60f, "Medium Tree")
			.addCheckpoint(11041210.0 * 8, 0.75f, "Big Tree")
			.addCheckpoint(78702725.0 * 8, 0.9f, "Forest")
			.addCheckpoint(561000000.0 * 8, 1f, "Blooming Forest")
			.addElement(WuxiaElements.WOOD);

	//From Dremtas White Tiger
	public static final Technique COLOSSAL_MOUNTAIN_CONTROL = new Technique( CultivationLevel.System.ESSENCE,"colossal_mountain_control",
			new TechniqueModifiers(1.2, 0.2, 0.9, 0.3), 6.4, 23000000.0 * 8, 800,0 ,0, 0,0)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(WuxiaElements.EARTH);

	// TODO -- multiple jumps --  i guess this is hard
	//From Febian
	public static final Technique LIGHT_CLOUD_STEPS = new Technique(CultivationLevel.System.ESSENCE, "light_cloud_steps",
			new TechniqueModifiers(0.1, 1.2, 0.6, 1.9), 7.2, 23000000.0 * 8, 800, 0, 0, 0, 0)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(WuxiaElements.AIR);
	//soul ones

	//from discord chat
	//middle earth tier
	public static final Technique SOUL_SLAUGHTERING_TECHNIQUE = new Technique(CultivationLevel.System.DIVINE, "soul_slaughtering_technique",
			new TechniqueModifiers(1.7f, 1.2f, 0.8f, 2.4f), 9f, 1673455223020.0 * 10, 34, 0, 0, 0, 0)
			.addCheckpoint(14221.71 * 10, 0.125f, "Killing Intent")
			.addCheckpoint(202256.98 * 10, 0.250f, "")
			.addCheckpoint(2876439.78 * 10, 0.375f, "")
			.addCheckpoint(40907887.05 * 10, 0.5f, "")
			.addCheckpoint(581780030.68 * 10, 0.625f, "")
			.addCheckpoint(8273905803.74 * 10, 0.750f, "")
			.addCheckpoint(117669073600.65 * 10, 0.875f, "Peak Slaughterer")
			.addCheckpoint(1673455223020.00 * 10, 1f, "Satan Slaughter God");

	//get
	public static Technique getTechniqueByName(String name) {
		for (Technique tech : TECHNIQUES) {
			if (tech.getName().equalsIgnoreCase(name)) return tech;
		}
		return null;
	}

}
