package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.handlers.RegistryHandler;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Techniques {

	public static final List<Technique> TECHNIQUES = new ArrayList<>();

	public static void init() {
		TECHNIQUES.add(BODY_STRENGTH);
		TECHNIQUES.add(LIGHT_FEET);
		TECHNIQUES.add(GREAT_WAY_OF_THE_BUDDHA);
		TECHNIQUES.add(AZURE_THEARCH_LONGEVITY_BODY);
		TECHNIQUES.add(YIN_BODY_ART);
		TECHNIQUES.add(BEDROCK_SKIN_NOTES);
		TECHNIQUES.add(WHITE_TIGER_TEMPERING);
		TECHNIQUES.add(LIGHTNING_THUNDER_BODY_ART);
		TECHNIQUES.add(MONKEY_KING_VITALITY_ART);
		TECHNIQUES.add(MERCIFUL_PALM_OF_BUDDHA);
		TECHNIQUES.add(PHOENIX_BODY_ART);
		TECHNIQUES.add(DRAGON_BODY_TEMPERING);
		TECHNIQUES.add(CAT_BODY_TRANSFORMATION_ARTS);
		TECHNIQUES.add(WIDE_WATER_FLOW);
		TECHNIQUES.add(BASIC_LIGHTNING_GUIDE);
		TECHNIQUES.add(FOREST_HEART_MANUAL);
		TECHNIQUES.add(LIGHT_CLOUD_STEPS);
		TECHNIQUES.add(BLOOMING_FOREST_ART);
		TECHNIQUES.add(COLOSSAL_MOUNTAIN_CONTROL);
		TECHNIQUES.add(WIND_STORM_ART);
		TECHNIQUES.add(DRAGON_SPEAR_ART);
		TECHNIQUES.add(YANG_BURNING_ART);
		TECHNIQUES.add(COLD_SWORD_ART);
		TECHNIQUES.add(EARTH_SPLITTING_SWORD_ART);
		TECHNIQUES.add(EARTH_ASSIMILATING_QI);
		TECHNIQUES.add(THUNDER_FIRE_ART);
		TECHNIQUES.add(DIVINE_FART_CULTIVATION_ART);
		TECHNIQUES.add(IGNITED_SOUL);
		TECHNIQUES.add(BURNING_FLAME_SOUL);
		TECHNIQUES.add(THUNDER_WALL_SOUL_ARTS);
		TECHNIQUES.add(AQUATIC_SOUL_MANIPULATION);
		TECHNIQUES.add(SUN_SOUL_ART);
		TECHNIQUES.add(AMORPHOUS_METALLIC_SOUL);
		TECHNIQUES.add(NINE_SPRINGS_SOUL);
		TECHNIQUES.add(BUDDHA_S_HEAVENLY_WAY);
		TECHNIQUES.add(RAGEFUL_ABNEGATION_SAINT_ARTS);
	}

	public static Technique getTechniqueByUName(String uName) {
		for (Technique t : TECHNIQUES) {
			if (t.getUName().equals(uName)) {
				return t;
			}
		}
		return null;
	}

	//User potion effects
	private static final PotionEffect REGENERATION_I = new PotionEffect(MobEffects.REGENERATION, 100, 0, true, false);
	private static final PotionEffect NIGHT_VISION = new PotionEffect(MobEffects.NIGHT_VISION, 410, 0, false, false);
	private static final PotionEffect SPEED_I = new PotionEffect(MobEffects.SPEED, 200, 0, false, false);
	private static final PotionEffect STRENGTH_I = new PotionEffect(MobEffects.STRENGTH, 200, 0, false, false);

	//Body ones
	public static final Technique BODY_STRENGTH = new Technique("body_strength", Cultivation.System.BODY,
			new TechniqueModifiers(1.5, 0.6, 1.8, 0.7, 1.4, 0.6), 2.9, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addEffect(600000 * 8, STRENGTH_I)
			.addSkill(50000 * 8, Skills.MINOR_BODY_REINFORCEMENT)
			.addSkill(300000 * 8, Skills.MINOR_POWER_PUNCH);

	public static final Technique LIGHT_FEET = new Technique("light_feet", Cultivation.System.BODY,
			new TechniqueModifiers(0.4, 1.8, 0.9, 2.1, 0.7, 0.2), 2.9, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addEffect(600000, SPEED_I)
			.addSkill(50000, Skills.LIGHT_FEET_SKILL)
			.addSkill(300000, Skills.LIGHT_FEET_LEAP);

	// TODO -- Add a "pagoda" above players head while cultivating
	//From Against the gods
	//Rank immortal3
	public static final Technique GREAT_WAY_OF_THE_BUDDHA = new Technique("great_way_of_the_buddha", Cultivation.System.BODY,
			new TechniqueModifiers(3.6, 1.9, 4.5, 1.8, 3.9, 2.2), 5.1, 70000000000.0 * 8, 420000)
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
			.addEffect(4500, REGENERATION_I);

	//From Rebirth of the Urban Immortal Cultivator
	//rank immortal2
	public static final Technique AZURE_THEARCH_LONGEVITY_BODY = new Technique("azure_thearch_longevity_body", Cultivation.System.BODY,
			new TechniqueModifiers(2.2, 1.2, 3.2, 0.8, 1.6, 2.8), 11.2, 1500000000.0 * 8, 50000)
			.addCheckpoint(5400 * 8, 0.15f, "Initial Success")
			.addCheckpoint(30200 * 8, 0.30f, "Connate Body")
			.addCheckpoint(165400 * 8, 0.45f, "Great Success")
			.addCheckpoint(907300 * 8, 0.60f, "Phenomenon Success")
			.addCheckpoint(4979800 * 8, 0.75f, "Divine Body")
			.addCheckpoint(27330630 * 8, 0.9f, "Deity body")
			.addCheckpoint(1500000000.0 * 8, 1f, "Thearch body")
			.addElement(Element.WOOD)
			.addSkill(3200 * 8, Skills.GATHER_WOOD)
			.addSkill(12000 * 8, Skills.ACCELERATE_GROWTH)
			.addSkill(15000 * 8, Skills.WOODEN_PRISON)
			.addEffect(5400 * 8, REGENERATION_I);

	//From Wuxia4You
	public static final Technique YIN_BODY_ART = new Technique("yin_body_art", Cultivation.System.BODY,
			new TechniqueModifiers(0.4, 1.4, 0.8, 1.5, 0.3, 0.1), 3.2, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.ICE);

	//From Dremtas White Tiger
	public static final Technique BEDROCK_SKIN_NOTES = new Technique("bedrock_skin_notes", Cultivation.System.BODY,
			new TechniqueModifiers(1.9, 0.1, 1.7, 0.1, 1.2, 0.5), 2.9, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.EARTH)
			.addSkill(2000 * 8, Skills.EARTH_SUCTION)
			.addSkill(1000 * 8, Skills.EARTHLY_WALL);

	//From Dremtas White Tiger
	public static final Technique WHITE_TIGER_TEMPERING = new Technique("white_tiger_tempering", Cultivation.System.BODY,
			new TechniqueModifiers(0.6, 1.4, 1.2, 1.9, 1.6, 1.2), 3.5, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected");

	//From Asura
	// Call random tribulation while cultivating
	// rank immortal 1
	public static final Technique LIGHTNING_THUNDER_BODY_ART = new Technique("lightning_thunder_body_art", Cultivation.System.BODY,
			new TechniqueModifiers(1.2, 2.1, 0.9, 2.2, 1.8, 1.7), 8.2, 190000000.0 * 8, 15000)
			.addCheckpoint(4570 * 8, 0.125f, "Lightning Cell Nourishment")
			.addCheckpoint(20890 * 8, 0.25f, "Lightning Bolt Muscles")
			.addCheckpoint(95400 * 8, 0.375f, "Thunder Blood Marrow")
			.addCheckpoint(435900 * 8, 0.5f, "Thunder Bones")
			.addCheckpoint(1991700 * 8, 0.625f, "Semi Lightning Thunder Body")
			.addCheckpoint(9100500 * 8, 0.75f, "Lightning Tendon Enhancement ")
			.addCheckpoint(41582360 * 8, 0.875f, "Thunder Meridians Nourishment")
			.addCheckpoint(190000000.0 * 8, 1f, "Complete Lightning Thunder Body")
			.addElement(Element.LIGHTNING)
			.addSkill(3000, Skills.WEAK_LIGHTNING_BOLT)
			.setCultivationEffect(actor -> {
						if (!actor.world.isRemote) {
							if (actor.getRNG().nextFloat() < 0.01) {
								ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
								CultivationUtils.callCustomThunder(actor, cultivation.getBodyModifier() * 5, 0.15 * cultivation.getBodyModifier());
							}
						}
						return true;
					}
			);

	//From Fruit
	//rank immortal 1
	public static final Technique MONKEY_KING_VITALITY_ART = new Technique("monkey_king_vitality_art", Cultivation.System.BODY,
			new TechniqueModifiers(0.8, 1.9, 1.2, 2.0, 1.9, 1.7), 4.7, 190000000.0 * 8, 15000)
			.addCheckpoint(7600.0 * 8, 0.16f, "Infant Monkey Stage")
			.addCheckpoint(574500.0 * 8, 0.32f, "Young Monkey")
			.addCheckpoint(435900.0 * 8, 0.48f, "Monkey King")
			.addCheckpoint(3305000.0 * 8, 0.64f, "Monkey Emperor")
			.addCheckpoint(25058900.0 * 8, 0.80f, "Immortal Monkey")
			.addCheckpoint(190000000.0 * 8, 1f, "Ape Rebirth");


	// TODO -- Add a golden buddha statue
	//From HuoYuhao -- Keep reminding myself he's a fire cultivator o.O
	public static final Technique MERCIFUL_PALM_OF_BUDDHA = new Technique("merciful_palm_of_buddha", Cultivation.System.BODY,
			new TechniqueModifiers(0.3, 0.3, 1.8, 0.8, 2.8, 0.7), 3.8, 23000000.0 * 8, 800)
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
	public static final Technique PHOENIX_BODY_ART = new Technique("phoenix_body_art", Cultivation.System.BODY,
			new TechniqueModifiers(1.9, 1.4, 2.2, 2.5, 2.9, 1.8), 7.6, 10370000000.0 * 8, 176722.67)
			.addCheckpoint(6020.0 * 8, 0.11f, "Blood Cleaning")
			.addCheckpoint(36300.0 * 8, 0.22f, "Crimson Blood")
			.addCheckpoint(218100.0 * 8, 0.33f, "Black Blood")
			.addCheckpoint(1312600.0 * 8, 0.44f, "Bone Refining Stage 1")
			.addCheckpoint(79000500.0 * 8, 0.55f, "Bone Refining Stage 2")
			.addCheckpoint(47553900.0 * 8, 0.66f, "Bone Refining Stage 3")
			.addCheckpoint(286231100.0 * 8, 0.77f, "Full Phoenix Body")
			.addCheckpoint(1722851250.0 * 8, 0.88f, "True Phoenix Body")
			.addCheckpoint(10370000000.0 * 8, 1f, "Reborn By Fire") //TODO -- set player on fire
			.addElement(Element.FIRE)
			.addSkill(2000 * 8, Skills.FLAMES)
			.addSkill(10000 * 8, Skills.FIRE_BAll);

	//From Anime4You -- former Wuxia4You (liked this one better)
	// divine rank 1
	public static final Technique DRAGON_BODY_TEMPERING = new Technique("dragon_body_tempering", Cultivation.System.BODY,
			new TechniqueModifiers(3.1, 1.2, 4.0, 2.8, 3.7, 3.0), 4.2, 2461400000000.0 * 8, 10177811.92)
			.addCheckpoint(6061.0 * 8, 0.08f, "Dragon Blood")
			.addCheckpoint(36744.9 * 8, 0.16f, "Dragon Bone")
			.addCheckpoint(222738.63 * 8, 0.25f, "Dragon Marrow")
			.addCheckpoint(1350187.45 * 8, 0.33f, "Dragon Viscera")
			.addCheckpoint(8184508.08 * 8, 0.41f, "Dragon Aura")
			.addCheckpoint(49612498.43 * 8, 0.50f, "Dragon Quasi Body")
			.addCheckpoint(3000738905.26 * 8, 0.58f, "Dragon Body")
			.addCheckpoint(1823006137.70 * 8, 0.66f, "Dragon Scales") //todo start rendering model here
			.addCheckpoint(11050620056.09 * 8, 0.75f, "Dragon Body Parts")
			.addCheckpoint(66986172508.48 * 8, 0.83f, "True Dragon Body")
			.addCheckpoint(406053894221.41 * 8, 0.91f, "Ancient Dragon Body")
			.addCheckpoint(2461400000000.0 * 8, 1.0f, "Divine Dragon Body");

	//From that cat dude -- Ziso
	public static final Technique CAT_BODY_TRANSFORMATION_ARTS = new Technique("cat_body_transformation_Arts", Cultivation.System.BODY,
			new TechniqueModifiers(0.2, 1.8, 0.6, 2.2, 0.8, 0.4), 6.2, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addEffect(20000 * 8, NIGHT_VISION);


	//Essence ones
	//From Wuxia4You suggestion
	// from the amount of levels, suffer a downgrade to mortal 2, really
	public static final Technique WIDE_WATER_FLOW = new Technique("wide_water_flow", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.9, 0.3, 1.2, 0.5, 0.8, 1.8), 4.2, 320000.0 * 8, 120)
			.addCheckpoint(1000.0 * 8, 0.1f, "Elemental Perception")
			.addCheckpoint(10000.0 * 8, 0.3f, "Element Release")
			.addCheckpoint(50000.0 * 8, 0.5f, "Meridian Widening")
			.addCheckpoint(150000.0 * 8, 0.7f, "Water Flow")
			.addCheckpoint(320000.0 * 8, 1f, "Wide Water Flow")
			.addElement(Element.WATER)
			.addSkill(2000 * 8, Skills.WATER_NEEDLE)
			.addSkill(10000 * 8, Skills.WATER_BLADE);

	//From Dremtas White Tiger
	public static final Technique BASIC_LIGHTNING_GUIDE = new Technique("basic_lightning_guide", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.2, 1.2, 0.9, 1.8, 1.9, 1.2), 3.4, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.LIGHTNING)
			.addSkill(3000 * 8, Skills.WEAK_LIGHTNING_BOLT)
			.addCompatible(Techniques.WIND_STORM_ART);

	//From Febian
	public static final Technique FOREST_HEART_MANUAL = new Technique("forest_heart_manual", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.9, 0.6, 1.8, 0.6, 1.2, 2.7), 1.7, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.WOOD)
			.addSkill(3200 * 8, Skills.GATHER_WOOD)
			.addSkill(12000 * 8, Skills.ACCELERATE_GROWTH)
			.addSkill(15000 * 8, Skills.WOODEN_PRISON)
			.addCompatible(Techniques.BLOOMING_FOREST_ART);

	// TODO -- multiple jumps --  i guess this is hard
	//From Febian
	public static final Technique LIGHT_CLOUD_STEPS = new Technique("light_cloud_steps", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.1, 1.2, 0.6, 1.9, 1.2, 2.1), 7.2, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.WIND)
			.addSkill(3000 * 8, Skills.WIND_BLADE);

	//From Anime4You -- former Wuxia4You (liked this one better)
	//rank immortal 2
	public static final Technique BLOOMING_FOREST_ART = new Technique("blooming_forest_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.8, 0.6, 2.2, 0.8, 1.2, 3.7), 8.1, 561000000.0 * 8, 80000.0)
			.addCheckpoint(4276.0 * 8, 0.15f, "Grass Land")
			.addCheckpoint(30485.0 * 8, 0.30f, "Flower Land")
			.addCheckpoint(217305.0 * 8, 0.45f, "Small Tree")
			.addCheckpoint(1548972.0 * 8, 0.60f, "Medium Tree")
			.addCheckpoint(11041210.0 * 8, 0.75f, "Big Tree")
			.addCheckpoint(78702725.0 * 8, 0.9f, "Forest")
			.addCheckpoint(561000000.0 * 8, 1f, "Blooming Forest")
			.addElement(Element.WOOD)
			.addSkill(3200 * 8, Skills.GATHER_WOOD)
			.addSkill(12000 * 8, Skills.ACCELERATE_GROWTH)
			.addSkill(15000 * 8, Skills.WOODEN_PRISON);

	//From Dremtas White Tiger
	public static final Technique COLOSSAL_MOUNTAIN_CONTROL = new Technique("colossal_mountain_control", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.2, 0.2, 0.9, 0.3, 1.2, 1.9), 6.4, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.EARTH)
			.addSkill(1000 * 8, Skills.EARTH_SUCTION)
			.addSkill(3000 * 8, Skills.EARTHLY_WALL)
			.addCompatible(Techniques.EARTH_ASSIMILATING_QI);

	//From Asura
	//rank immortal 2
	public static final Technique WIND_STORM_ART = new Technique("wind_storm_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.8, 2.7, 1.2, 2.1, 1.1, 2.9), 10.2, 21066000.0 * 8, 6752.12)
			.addCheckpoint(4150.0 * 8, 0.14f, "Breeze Stage")
			.addCheckpoint(17200.0 * 8, 0.28f, "Gust Stage")
			.addCheckpoint(71300.0 * 8, 0.42f, "Violent Gust Stage")
			.addCheckpoint(295550.0 * 8, 0.56f, "Gale Stage")
			.addCheckpoint(1225400.0 * 8, 0.70f, "Storm Stage")
			.addCheckpoint(5080750.0 * 8, 0.84f, "Hurricane Stage")
			.addCheckpoint(21066000.0 * 8, 1f, "Tornadoes Stage")
			.addElement(Element.WIND)
			.addElement(Element.LIGHTNING)
			.addSkill(3000 * 8, Skills.WIND_BLADE)
			.addSkill(3000 * 8, Skills.WEAK_LIGHTNING_BOLT);

	//todo - turn it into a weapon technique (spear)
	//From Asura -- Immortal 3 Level
	public static final Technique DRAGON_SPEAR_ART = new Technique("dragon_spear_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.1, 2.4, 1.2, 1.9, 2.4, 3.3), 12.1, 70000000.0 * 8, 320529.05)
			.addCheckpoint(3454.0 * 8, 0.11f, "Worm Stage")
			.addCheckpoint(11931.0 * 8, 0.22f, "Snake Stage")
			.addCheckpoint(41213.0 * 8, 0.33f, "Python Stage")
			.addCheckpoint(142360.0 * 8, 0.44f, "Flood Dragon Stage")
			.addCheckpoint(491742.0 * 8, 0.55f, "Dragon Stage")
			.addCheckpoint(1698575.0 * 8, 0.66f, "Elder Dragon Stage")
			.addCheckpoint(5867219.0 * 8, 0.77f, "Ancient Dragon Stage")
			.addCheckpoint(20266550.0 * 8, 0.88f, "Divine Dragon Stage")
			.addCheckpoint(70000000.0 * 8, 1f, "Eternal Dragon Stage");

	//From Anime4You -- former Wuxia4You (sad i liked it better)
	//immortal 2
	public static final Technique YANG_BURNING_ART = new Technique("yang_burning_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.6, 2.0, 0.8, 1.2, 2.2, 2.3), 8.9, 324561600.00, 47325.73)
			.addCheckpoint(4650, 0.16f, "Elemental Perception")
			.addCheckpoint(43290, 0.32f, "Elemental Release")
			.addCheckpoint(402840, 0.48f, "Yang absorbing")
			.addCheckpoint(3748476, 0.64f, "Elemental Fusion")
			.addCheckpoint(34879957, 0.80f, "Burning Way")
			.addCheckpoint(324561600, 1f, "True Yang Burning")
			.addElement(Element.FIRE)
			.addSkill(2000 * 8, Skills.FLAMES)
			.addSkill(10000 * 8, Skills.FIRE_BAll);

	//todo - turn it into a weapon technique (sword)
	//From Anime4You -- former Wuxia4You (sad i liked it better)
	//immortal 3
	public static final Technique COLD_SWORD_ART = new Technique("cold_sword_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.6, 2.7, 1.8, 2.2, 3.0, 4.0), 8.2, 31000000000.0, 940164.60)
			.addCheckpoint(6800, 0.11f, "Sword Feeling")
			.addCheckpoint(46200, 0.22f, "Sword Understanding")
			.addCheckpoint(314150, 0.33f, "Unfeeling Sword")
			.addCheckpoint(2135500, 0.44f, "Deadly Sword")
			.addCheckpoint(14516690, 0.55f, "Killing Sword")
			.addCheckpoint(98682724, 0.66f, "Sword Body Fusion")
			.addCheckpoint(670833358, 0.77f, "Heartless Sword")
			.addCheckpoint(4560244962.0, 0.88f, "Death Sword")
			.addCheckpoint(31000000000.0, 1f, "Death God Sword")
			.addElement(Element.ICE)
			.addSkill(3000 * 8, Skills.WEAK_SWORD_BEAM)
			.addSkill(6000 * 8, Skills.WATER_BLADE)
			.addSkill(13000 * 8, Skills.SWORD_BEAM_BARRAGE)
			.addSkill(46200 * 8, Skills.ADEPT_SWORD_FLIGHT);

	//todo - turn it into a weapon technique (sword)
	//From Anime4You -- former Wuxia4You (sad i liked it better)
	public static final Technique EARTH_SPLITTING_SWORD_ART = new Technique("earth_splitting_sword_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.7, 1.2, 0.3, 1.7, 1.2, 1.8), 9.2, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.EARTH)
			.addSkill(2000 * 8, Skills.EARTH_SUCTION)
			.addSkill(1000 * 8, Skills.EARTHLY_WALL)
			.addSkill(3000 * 8, Skills.WEAK_SWORD_BEAM)
			.addSkill(13000 * 8, Skills.SWORD_BEAM_BARRAGE);

	//From Anime4You -- former Wuxia4You (sad i liked it better)
	//immortal 2
	public static final Technique EARTH_ASSIMILATING_QI = new Technique("earth_assimilating_qi", Cultivation.System.ESSENCE,
			new TechniqueModifiers(1.8, 0.4, 1.2, 0.7, 1.5, 2.4), 5.9, 324561600.0 * 8, 47325.73)
			.addCheckpoint(4650.0 * 8, 0.16f, "Elemental Perception")
			.addCheckpoint(43290.0 * 8, 0.32f, "Element Release")
			.addCheckpoint(402840.0 * 8, 0.48f, "Earth Conception")
			.addCheckpoint(3748476.0 * 8, 0.64f, "Earth Fusion")
			.addCheckpoint(34879957.0 * 8, 0.80f, "True Earth Qi")
			.addCheckpoint(324561600.0 * 8, 1f, "Earths Tremor")
			.addElement(Element.EARTH)
			.addSkill(2000 * 8, Skills.EARTH_SUCTION)
			.addSkill(1000 * 8, Skills.EARTHLY_WALL);

	//From Zacj -- yellow duck
	public static final Technique THUNDER_FIRE_ART = new Technique("thunder_fire_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.3, 1.7, 0.4, 1.3, 1.8, 1.7), 8.8, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.FIRE)
			.addElement(Element.LIGHTNING)
			.addSkill(2000 * 8, Skills.FLAMES)
			.addSkill(10000 * 8, Skills.FIRE_BAll)
			.addSkill(3000 * 8, Skills.WEAK_LIGHTNING_BOLT);


	// From Anophobia
	public static final Technique DIVINE_FART_CULTIVATION_ART = new Technique("divine_fart_cultivation_art", Cultivation.System.ESSENCE,
			new TechniqueModifiers(0.7, 1.4, 1.8, 1.3, 0.7, 1.9), 4.5, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.setCultivationEffect(actor -> {
				if (!actor.world.isRemote) {
					AxisAlignedBB range = new AxisAlignedBB(new BlockPos(actor.posX, actor.posY, actor.posZ));
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
					range.grow(Math.min(72, cultivation.getEssenceModifier()));
					List<EntityLivingBase> targets = actor.world.getEntitiesWithinAABB(EntityLivingBase.class, range);
					for (EntityLivingBase target : targets) {
						target.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120, 0, false, true));
					}
					actor.world.playSound(null, actor.posX, actor.posY + 0.4, actor.posZ, RegistryHandler.fartSound, SoundCategory.HOSTILE, 1, 1);
				}
				return true;
			});

	//Soul ones

	//From Asura
	public static final Technique IGNITED_SOUL = new Technique("ignited_soul", Cultivation.System.DIVINE,
			new TechniqueModifiers(0.4, 0.9, 0.7, 1.1, 0.8, 0.3), 4.2, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.FIRE)
			.addSkill(2000 * 8, Skills.FLAMES)
			.addSkill(10000 * 8, Skills.FIRE_BAll)
			.addCompatible(Techniques.BURNING_FLAME_SOUL);

	//From Anime4You -- former Wuxia4You (liked this one better)
	//rank immortal 3
	public static final Technique BURNING_FLAME_SOUL = new Technique("burning_flame_soul", Cultivation.System.DIVINE,
			new TechniqueModifiers(0.8, 1.8, 1.4, 2.1, 1.7, 0.7), 11.3, 66900000000.0 * 8, 1200000.0)
			.addCheckpoint(4000.0 * 8, 0.11f, "Soul Burning")
			.addCheckpoint(32000.0 * 8, 0.22f, "Flame Absorbing")
			.addCheckpoint(255734.0 * 8, 0.33f, "Flame Assimilating")
			.addCheckpoint(2045164.0 * 8, 0.44f, "Flame Reinforcement")
			.addCheckpoint(16355651.0 * 8, 0.55f, "Flame Releasing")
			.addCheckpoint(130799900.0 * 8, 0.66f, "Flame Control")
			.addCheckpoint(1046036843.0 * 8, 0.77f, "Flame Forming")
			.addCheckpoint(8365396871.0 * 8, 0.88f, "Burning Flame")
			.addCheckpoint(66900000000.0 * 8, 0.99f, "Earth Flame")
			.addElement(Element.FIRE)
			.addSkill(2000 * 8, Skills.FLAMES)
			.addSkill(10000 * 8, Skills.FIRE_BAll)
			.setCultivationEffect(actor -> {
				if (!actor.world.isRemote && actor instanceof EntityPlayer) {
					ICultTech cultTech = CultivationUtils.getCultTechFromEntity(actor);
					if (cultTech.getDivineTechnique().getProficiency() < 4000) {
						actor.attackEntityFrom(DamageSource.ON_FIRE, 1f);
					}
				}
				return true;
			});

	//From Anime4You -- former Wuxia4You (liked this one better)
	//Today ...  was gonna be the day ...
	//rank immortal 2
	public static final Technique THUNDER_WALL_SOUL_ARTS = new Technique("thunder_wall_soul_arts", Cultivation.System.DIVINE,
			new TechniqueModifiers(1.2, 0.5, 0.9, 1.0, 1.2, 0.7), 9.2, 1300000000.0 * 8, 101241.12)
			.addCheckpoint(3400.0 * 8, 0.14f, "Soul Thunder")
			.addCheckpoint(29013.0 * 8, 0.28f, "Wall Forming")
			.addCheckpoint(247096.0 * 8, 0.42f, "Thunder Reinforcement")
			.addCheckpoint(2104438.0 * 8, 0.56f, "Thunder Wall Release")
			.addCheckpoint(17922774.0 * 8, 0.70f, "Wall Destruction")
			.addCheckpoint(152642088.0 * 8, 0.84f, "Reforming Thunder")
			.addCheckpoint(1300000000.0 * 8, 1f, "True Thunder Wall")
			.addElement(Element.LIGHTNING)
			.addSkill(3000 * 8, Skills.WEAK_LIGHTNING_BOLT)
			.setCultivationEffect(actor -> {
				if (!actor.world.isRemote && actor instanceof EntityPlayer) {
					ICultTech cultTech = CultivationUtils.getCultTechFromEntity(actor);
					if (cultTech.getDivineTechnique().getProficiency() < 3400) {
						actor.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1f);
					}
				}
				return true;
			});

	//From Dremtas White Tiger
	public static final Technique AQUATIC_SOUL_MANIPULATION = new Technique("aquatic_soul_manipulation", Cultivation.System.DIVINE,
			new TechniqueModifiers(0.2, 0.1, 0.3, 0.1, .03, 0.2), 4.5, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.WATER)
			.addSkill(2000 * 8, Skills.WATER_NEEDLE)
			.addSkill(10000 * 8, Skills.WATER_BLADE);

	//From Asura
	public static final Technique SUN_SOUL_ART = new Technique("sun_soul_art", Cultivation.System.DIVINE,
			new TechniqueModifiers(0.3, 0.1, 0.3, 0.1, 0.7, 0.8), 2.8, 23000000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.LIGHT)
			.addElement(Element.FIRE)
			.addSkill(2000 * 8, Skills.FLAMES)
			.addSkill(10000 * 8, Skills.FIRE_BAll);

	//From Febian
	public static final Technique AMORPHOUS_METALLIC_SOUL = new Technique("amorphous_metallic_soul", Cultivation.System.DIVINE,
			new TechniqueModifiers(0.2, 0.1, 0.3, 0.1, 0.3, 0.7), 6.1, 23000000.0 * 8, 800)
			.addCheckpoint(1000.8 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.METAL)
			.addSkill(2000 * 8, Skills.METAL_DETECTION)
			.addSkill(10000 * 8, Skills.ORE_SUCTION);

	//From Febian
	//Does extra damage against living
	public static final Technique NINE_SPRINGS_SOUL = new Technique("nine_springs_soul", Cultivation.System.DIVINE,
			new TechniqueModifiers(0.2, 0.1, 0.3, 0.1, 0.3, 0.4), 6.2, 2300000.0 * 8, 800)
			.addCheckpoint(1000.0 * 8, 0.10f, "Minor Success")
			.addCheckpoint(10000.0 * 8, 0.30f, "Small Success")
			.addCheckpoint(90000.0 * 8, 0.45f, "Middle Success")
			.addCheckpoint(600000.0 * 8, 0.60f, "Natural Success")
			.addCheckpoint(1500000.0 * 8, 0.8f, "Great Success")
			.addCheckpoint(23000000.0 * 8, 1.0f, "Perfected")
			.addElement(Element.DARK);

	//From HuoYuhao
	//If player kills, he loses something
	//rank divine 1 -- yeah this one deserves
	public static final Technique BUDDHA_S_HEAVENLY_WAY = new Technique("buddha_s_heavenly_way", Cultivation.System.DIVINE,
			new TechniqueModifiers(1.8, 1.1, 1.7, 1.1, 1.4, 2.6), 18.2, 1800000000000.0 * 8, 6991714.27)
			.addCheckpoint(1350.0 * 8, 0.08f, "Tiny Buddha")
			.addCheckpoint(9121.0 * 8, 0.16f, "Connate Buddha")
			.addCheckpoint(61601.0 * 8, 0.24f, "Peaceful Mind")
			.addCheckpoint(416016.0 * 8, 0.32f, "Constant Zen")
			.addCheckpoint(2809512.0 * 8, 0.40f, "Small Buddhahood")
			.addCheckpoint(18973665.0 * 8, 0.48f, "Holy Buddha Soul")
			.addCheckpoint(128136087.0 * 8, 0.56f, "Medium Buddhahood")
			.addCheckpoint(865349742.0 * 8, 0.64f, "Saint Buddha Soul")
			.addCheckpoint(5844022478.0 * 8, 0.72f, "Greater Buddhahood")
			.addCheckpoint(39466815629.0 * 8, 0.8f, "Reincarnation Free")
			.addCheckpoint(266533802983.0 * 8, 0.88f, "Total Abnegation")
			.addCheckpoint(1800000000000.0 * 8, 1.0f, "Perfected Buddha");

	//From Anophobia's buggy suggestion
	//rank divine 1 -- yeah another one
	public static final Technique RAGEFUL_ABNEGATION_SAINT_ARTS = new Technique("rageful_abnegation_saint_arts", Cultivation.System.DIVINE,
			new TechniqueModifiers(1.1, 1.8, 1.3, 1.7, 1.5, 1.9), 18.2, 1800000000000.0 * 8, 6991714.27)
			.addCheckpoint(1350.0 * 8, 0.08f, "Selfless State")
			.addCheckpoint(9121.0 * 8, 0.16f, "Connate Rage")
			.addCheckpoint(61601.0 * 8, 0.24f, "Aggressive Donation")
			.addCheckpoint(416016.0 * 8, 0.32f, "Disgraceful Graceful")
			.addCheckpoint(2809512.0 * 8, 0.40f, "Berserk State")
			.addCheckpoint(18973665.0 * 8, 0.48f, "Destructive Gratification")
			.addCheckpoint(128136087.0 * 8, 0.56f, "Selfless Rageful Freak")
			.addCheckpoint(865349742.0 * 8, 0.64f, "Constant Pain Killer")
			.addCheckpoint(5844022478.0 * 8, 0.72f, "Harmful Saintliness")
			.addCheckpoint(39466815629.0 * 8, 0.8f, "Total Rage")
			.addCheckpoint(266533802983.0 * 8, 0.88f, "Total Abnegation")
			.addCheckpoint(1800000000000.0 * 8, 1.0f, "Total Saint")
			.addElement(Element.LIGHT)
			.addEffect(32000 * 8, STRENGTH_I);

}
