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
			new TechniquesModifiers(3, 2, 5, 1.5, 3, 2), 2.9, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addEffect(600000, STRENGTH_I)
			.addSkill(50000, Skills.MINOR_BODY_REINFORCEMENT)
			.addSkill(300000, Skills.MINOR_POWER_PUNCH);

	public static final Technique LIGHT_FEET = new Technique("light_feet", Cultivation.System.BODY,
			new TechniquesModifiers(2, 4, 3, 5, 3, 1), 2.9, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addEffect(600000, SPEED_I)
			.addSkill(50000, Skills.LIGHT_FEET_SKILL)
			.addSkill(300000, Skills.LIGHT_FEET_LEAP);

	// TODO -- Add a "pagoda" above players head while cultivating
	//From Against the gods
	//Rank immortal3
	public static final Technique GREAT_WAY_OF_THE_BUDDHA = new Technique("great_way_of_the_buddha", Cultivation.System.BODY,
			new TechniquesModifiers(11, 6, 22, 12, 19, 12), 5.1, 70000000000.0, 420000)
			.addCheckpoint(4500, "First Stage")
			.addCheckpoint(20300, "Second Stage")
			.addCheckpoint(91470, "Third Stage")
			.addCheckpoint(412130, "Fourth Stage")
			.addCheckpoint(1856900, "Fifth Stage")
			.addCheckpoint(8366600, "Sixth Stage")
			.addCheckpoint(37697000, "Seventh Stage")
			.addCheckpoint(169849800, "Eighth Stage")
			.addCheckpoint(765284900, "Ninth Stage")
			.addCheckpoint(3448111300.0, "Tenth Stage")
			.addCheckpoint(15536006750.0, "Eleventh Stage")
			.addCheckpoint(70000000000.0, "Twelfth Stage")
			.addEffect(4500, REGENERATION_I);

	//From Rebirth of the Urban Immortal Cultivator
	//rank immortal2
	public static final Technique AZURE_THEARCH_LONGEVITY_BODY = new Technique("azure_thearch_longevity_body", Cultivation.System.BODY,
			new TechniquesModifiers(17, 12, 15, 8, 8, 18), 11.2, 1500000000.0, 50000)
			.addCheckpoint(5400, "Initial Success")
			.addCheckpoint(30200, "Connate Body")
			.addCheckpoint(165400, "Great Success")
			.addCheckpoint(907300, "Phenomenon Success")
			.addCheckpoint(4979800, "Divine Body")
			.addCheckpoint(27330630, "Deity body")
			.addCheckpoint(1500000000.0, "Thearch body")
			.addElement(Element.WOOD)
			.addSkill(3200, Skills.GATHER_WOOD)
			.addSkill(12000, Skills.ACCELERATE_GROWTH)
			.addSkill(15000, Skills.WOODEN_PRISON)
			.addEffect(4500, REGENERATION_I);

	//From Wuxia4You
	public static final Technique YIN_BODY_ART = new Technique("yin_body_art", Cultivation.System.BODY,
			new TechniquesModifiers(3, 4, 2, 5, 3, 1), 3.2, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.ICE);

	//From Dremtas White Tiger
	public static final Technique BEDROCK_SKIN_NOTES = new Technique("bedrock_skin_notes", Cultivation.System.BODY,
			new TechniquesModifiers(4, 2, 5, 2, 3, 1.5), 2.9, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.EARTH)
			.addSkill(2000, Skills.EARTH_SUCTION)
			.addSkill(1000, Skills.EARTHLY_WALL);

	//From Dremtas White Tiger
	public static final Technique WHITE_TIGER_TEMPERING = new Technique("white_tiger_tempering", Cultivation.System.BODY,
			new TechniquesModifiers(2, 4, 3, 5, 4, 2), 3.5, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected");

	//From Asura
	// Call random tribulation while cultivating
	// rank immortal 1
	public static final Technique LIGHTNING_THUNDER_BODY_ART = new Technique("lightning_thunder_body_art", Cultivation.System.BODY,
			new TechniquesModifiers(4, 2, 5, 2, 3, 3), 8.2, 190000000.0, 15000)
			.addCheckpoint(4570, "Lightning Cell Nourishment")
			.addCheckpoint(20890, "Lightning Bolt Muscles")
			.addCheckpoint(95400, "Thunder Blood Marrow")
			.addCheckpoint(435900, "Thunder Bones")
			.addCheckpoint(1991700, "Semi Lightning Thunder Body")
			.addCheckpoint(9100500, "Lightning Tendon Enhancement ")
			.addCheckpoint(41582360, "Thunder Meridians Nourishment")
			.addCheckpoint(190000000.0, "Complete Lightning Thunder Body")
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
			new TechniquesModifiers(4, 2, 5, 2, 3, 2), 4.7, 190000000.0, 15000)
			.addCheckpoint(7600, "Infant Monkey Stage")
			.addCheckpoint(574500, "Young Monkey")
			.addCheckpoint(435900, "Monkey King")
			.addCheckpoint(3305000, "Monkey Emperor")
			.addCheckpoint(25058900, "Immortal Monkey")
			.addCheckpoint(190000000.0, "Ape Rebirth");


	// TODO -- Add a golden buddha statue
	//From HuoYuhao -- Keep reminding myself he's a fire cultivator o.O
	public static final Technique MERCIFUL_PALM_OF_BUDDHA = new Technique("merciful_palm_of_buddha", Cultivation.System.BODY,
			new TechniquesModifiers(2, 3, 6, 1, 4, 4), 3.8, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected");

	// TODO -- model transformations
	// TODO -- Painful to change a body transformation art
	//From Asura
	//rank immortal 2
	public static final Technique PHOENIX_BODY_ART = new Technique("phoenix_body_art", Cultivation.System.BODY,
			new TechniquesModifiers(16, 16, 22, 9, 24, 19), 4.2, 10370000000.0, 176722.67)
			.addCheckpoint(6020, "Blood Cleaning")
			.addCheckpoint(36300, "Crimson Blood")
			.addCheckpoint(218100, "Black Blood")
			.addCheckpoint(1312600, "Bone Refining Stage 1")
			.addCheckpoint(79000500, "Bone Refining Stage 2")
			.addCheckpoint(47553900, "Bone Refining Stage 3")
			.addCheckpoint(286231100, "Full Phoenix Body")
			.addCheckpoint(1722851250, "True Phoenix Body")
			.addCheckpoint(10370000000.0, "Reborn By Fire") //TODO -- set player on fire
			.addElement(Element.FIRE);

	//From Anime4You -- former Wuxia4You (liked this one better)
	public static final Technique DRAGON_BODY_TEMPERING = new Technique("dragon_body_tempering", Cultivation.System.BODY,
			new TechniquesModifiers(4, 2, 5, 2, 3, 4), 3.1, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.FIRE);

	//From that cat dude -- Ziso
	public static final Technique CAT_BODY_TRANSFORMATION_ARTS = new Technique("cat_body_transformation_Arts", Cultivation.System.BODY,
			new TechniquesModifiers(4, 2, 5, 2, 3, 4), 6.2, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addEffect(20000, NIGHT_VISION);


	//Essence ones
	//From Wuxia4You suggestion
	// from the amount of levels, suffer a downgrade to mortal 2, really
	public static final Technique WIDE_WATER_FLOW = new Technique("wide_water_flow", Cultivation.System.ESSENCE,
			new TechniquesModifiers(5, 3, 2, 4, 3, 6), 4.2, 320000, 120)
			.addCheckpoint(1000, "Elemental Perception")
			.addCheckpoint(10000, "Element Release")
			.addCheckpoint(50000, "Meridian Widening")
			.addCheckpoint(150000, "Water Flow")
			.addCheckpoint(320000, "Wide Water Flow")
			.addElement(Element.WATER)
			.addSkill(2000, Skills.WATER_NEEDLE)
			.addSkill(10000, Skills.WATER_BLADE);

	//From Dremtas White Tiger
	public static final Technique BASIC_LIGHTNING_GUIDE = new Technique("basic_lightning_guide", Cultivation.System.ESSENCE,
			new TechniquesModifiers(2, 5, 3, 4, 4, 5), 3.4, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.LIGHTNING)
			.addSkill(3000, Skills.WEAK_LIGHTNING_BOLT);

	//From Febian
	public static final Technique FOREST_HEART_MANUAL = new Technique("forest_heart_manual", Cultivation.System.ESSENCE,
			new TechniquesModifiers(4, 2, 6, 3, 4, 7), 1.7, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.WOOD)
			.addSkill(3200, Skills.GATHER_WOOD)
			.addSkill(12000, Skills.ACCELERATE_GROWTH)
			.addSkill(15000, Skills.WOODEN_PRISON);

	// TODO -- multiple jumps --  i guess this is hard
	//From Febian
	public static final Technique LIGHT_CLOUD_STEPS = new Technique("light_cloud_steps", Cultivation.System.ESSENCE,
			new TechniquesModifiers(1, 6, 2, 7, 4, 5), 7.2, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.WIND)
			.addSkill(3000, Skills.WIND_BLADE);

	//From Anime4You -- former Wuxia4You (liked this one better)
	//rank immortal 2
	public static final Technique BLOOMING_FOREST_ART = new Technique("blooming_forest_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(4, 2, 6, 3, 4, 8), 8.1, 561000000.0, 80000.0)
			.addCheckpoint(4276, "Grass Land")
			.addCheckpoint(30485, "Flower Land")
			.addCheckpoint(217305, "Small Tree")
			.addCheckpoint(1548972, "Medium Tree")
			.addCheckpoint(11041210, "Big Tree")
			.addCheckpoint(78702725, "Forest")
			.addCheckpoint(561000000.0, "Blooming Forest")
			.addElement(Element.WOOD)
			.addSkill(3200, Skills.GATHER_WOOD)
			.addSkill(12000, Skills.ACCELERATE_GROWTH)
			.addSkill(15000, Skills.WOODEN_PRISON);

	//From Dremtas White Tiger
	public static final Technique COLOSSAL_MOUNTAIN_CONTROL = new Technique("colossal_mountain_control", Cultivation.System.ESSENCE,
			new TechniquesModifiers(5, 2, 4, 3, 4, 4), 6.4, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.EARTH)
			.addSkill(1000, Skills.EARTH_SUCTION)
			.addSkill(3000, Skills.EARTHLY_WALL);

	//From Asura
	public static final Technique WIND_STORM_ART = new Technique("wind_storm_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(2, 6, 3, 5, 3, 3), 10.2, 21066000.0, 6752.12)
			.addCheckpoint(4150, "Breeze Stage")
			.addCheckpoint(17200, "Gust Stage")
			.addCheckpoint(71300, "Violent Gust Stage")
			.addCheckpoint(295550, "Gale Stage")
			.addCheckpoint(1225400, "Storm Stage")
			.addCheckpoint(5080750, "Hurricane Stage")
			.addCheckpoint(21066000, "Tornadoes Stage")
			.addElement(Element.WIND)
			.addElement(Element.LIGHTNING)
			.addSkill(3000, Skills.WIND_BLADE)
			.addSkill(3000, Skills.WEAK_LIGHTNING_BOLT);

	//todo - turn it into a weapon technique (spear)
	//From Asura -- Immortal 3 Level
	public static final Technique DRAGON_SPEAR_ART = new Technique("dragon_spear_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(4, 19, 5, 12, 25, 19), 12.1, 70000000, 320529.05)
			.addCheckpoint(3454, "Worm Stage")
			.addCheckpoint(11931, "Snake Stage")
			.addCheckpoint(41213, "Python Stage")
			.addCheckpoint(142360, "Flood Dragon Stage")
			.addCheckpoint(491742, "Dragon Stage")
			.addCheckpoint(1698575, "Elder Dragon Stage")
			.addCheckpoint(5867219, "Ancient Dragon Stage")
			.addCheckpoint(20266550, "Divine Dragon Stage")
			.addCheckpoint(70000000, "Eternal Dragon Stage");

	//From Anime4You -- former Wuxia4You (sad i liked it better)
	//immortal 2
	public static final Technique YANG_BURNING_ART = new Technique("yang_burning_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(4, 3, 2, 6, 5, 7), 8.9, 324561600.00, 47325.73)
			.addCheckpoint(4650, "Elemental Perception")
			.addCheckpoint(43290, "Elemental Release")
			.addCheckpoint(402840, "Yang absorbing")
			.addCheckpoint(3748476, "Elemental Fusion")
			.addCheckpoint(34879957, "Burning Way")
			.addCheckpoint(324561600, "True Yang Burning")
			.addElement(Element.FIRE)
			.addSkill(2000, Skills.FLAMES)
			.addSkill(10000, Skills.FIRE_BAll);

	//todo - turn it into a weapon technique (sword)
	//From Anime4You -- former Wuxia4You (sad i liked it better)
	//immortal 3
	public static final Technique COLD_SWORD_ART = new Technique("cold_sword_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(3, 4, 2, 4, 5, 3), 8.2, 31000000000.0, 940164.60)
			.addCheckpoint(6800, "Sword Feeling")
			.addCheckpoint(46200, "Sword Understanding")
			.addCheckpoint(314150, "Unfeeling Sword")
			.addCheckpoint(2135500, "Deadly Sword")
			.addCheckpoint(14516690, "Killing Sword")
			.addCheckpoint(98682724, "Sword Body Fusion")
			.addCheckpoint(670833358, "Heartless Sword")
			.addCheckpoint(4560244962.0, "Death Sword")
			.addCheckpoint(31000000000.0, "Death God Sword")
			.addElement(Element.ICE)
			.addSkill(3000, Skills.WEAK_SWORD_BEAM)
			.addSkill(6000, Skills.WATER_BLADE)
			.addSkill(13000, Skills.SWORD_BEAM_BARRAGE)
			.addSkill(46200, Skills.ADEPT_SWORD_FLIGHT);

	//todo - turn it into a weapon technique (sword)
	//From Anime4You -- former Wuxia4You (sad i liked it better)
	public static final Technique EARTH_SPLITTING_SWORD_ART = new Technique("earth_splitting_sword_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(3, 2, 2, 4, 5, 3), 9.2, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.EARTH)
			.addSkill(2000, Skills.EARTH_SUCTION)
			.addSkill(1000, Skills.EARTHLY_WALL)
			.addSkill(3000, Skills.WEAK_SWORD_BEAM)
			.addSkill(13000, Skills.SWORD_BEAM_BARRAGE);

	//From Anime4You -- former Wuxia4You (sad i liked it better)
	//immortal 2
	public static final Technique EARTH_ASSIMILATING_QI = new Technique("earth_assimilating_qi", Cultivation.System.ESSENCE,
			new TechniquesModifiers(3, 4, 2, 4, 5, 3), 5.9, 324561600.00, 47325.73)
			.addCheckpoint(4650, "Elemental Perception")
			.addCheckpoint(43290, "Element Release")
			.addCheckpoint(402840, "Earth Conception")
			.addCheckpoint(3748476, "Earth Fusion")
			.addCheckpoint(34879957, "True Earth Qi")
			.addCheckpoint(324561600, "Earths Tremor")
			.addElement(Element.EARTH)
			.addSkill(2000, Skills.EARTH_SUCTION)
			.addSkill(1000, Skills.EARTHLY_WALL);

	//From Zacj -- yellow duck
	public static final Technique THUNDER_FIRE_ART = new Technique("thunder_fire_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(3, 4, 2, 4, 5, 4), 8.8, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.FIRE)
			.addElement(Element.LIGHTNING)
			.addSkill(2000, Skills.FLAMES)
			.addSkill(10000, Skills.FIRE_BAll)
			.addSkill(3000, Skills.WEAK_LIGHTNING_BOLT);

	public static final Technique DIVINE_FART_CULTIVATION_ART = new Technique("divine_fart_cultivation_art", Cultivation.System.ESSENCE,
			new TechniquesModifiers(2, 3, 1, 5, 3, 4), 4.5, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
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
			new TechniquesModifiers(5, 2, 4, 3, 4, 2), 4.2, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.FIRE)
			.addSkill(2000, Skills.FLAMES)
			.addSkill(10000, Skills.FIRE_BAll);

	//From Anime4You -- former Wuxia4You (liked this one better)
	//rank immortal 3
	public static final Technique BURNING_FLAME_SOUL = new Technique("burning_flame_soul", Cultivation.System.DIVINE,
			new TechniquesModifiers(5, 2, 4, 3, 4, 3), 11.3, 66900000000.0, 1200000.0)
			.addCheckpoint(4000, "Soul Burning")
			.addCheckpoint(32000, "Flame Absorbing")
			.addCheckpoint(255734, "Flame Assimilating")
			.addCheckpoint(2045164, "Flame Reinforcement")
			.addCheckpoint(16355651, "Flame Releasing")
			.addCheckpoint(130799900, "Flame Control")
			.addCheckpoint(1046036843, "Flame Forming")
			.addCheckpoint(8365396871.0, "Burning Flame")
			.addCheckpoint(66900000000.0, "Earth Flame")
			.addElement(Element.FIRE)
			.addSkill(2000, Skills.FLAMES)
			.addSkill(10000, Skills.FIRE_BAll)
			.setCultivationEffect(actor -> {
				if(!actor.world.isRemote && actor instanceof EntityPlayer) {
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
			new TechniquesModifiers(6, 3, 2, 4, 2, 3), 9.2, 1300000000.0, 101241.12)
			.addCheckpoint(3400, "Soul Thunder")
			.addCheckpoint(29013, "Wall Forming")
			.addCheckpoint(247096, "Thunder Reinforcement")
			.addCheckpoint(2104438, "Thunder Wall Release")
			.addCheckpoint(17922774, "Wall Destruction")
			.addCheckpoint(152642088, "Reforming Thunder")
			.addCheckpoint(1300000000.0, "True Thunder Wall")
			.addElement(Element.LIGHTNING)
			.addSkill(3000, Skills.WEAK_LIGHTNING_BOLT)
			.setCultivationEffect(actor -> {
				if(!actor.world.isRemote && actor instanceof EntityPlayer) {
					ICultTech cultTech = CultivationUtils.getCultTechFromEntity(actor);
					if (cultTech.getDivineTechnique().getProficiency() < 3400) {
						actor.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 1f);
					}
				}
				return true;
			});

	//From Dremtas White Tiger
	public static final Technique AQUATIC_SOUL_MANIPULATION = new Technique("aquatic_soul_manipulation", Cultivation.System.DIVINE,
			new TechniquesModifiers(2, 1, 3, 1, 3, 2), 4.5, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.WATER)
			.addSkill(2000, Skills.WATER_NEEDLE)
			.addSkill(10000, Skills.WATER_BLADE);

	//From Asura
	public static final Technique SUN_SOUL_ART = new Technique("sun_soul_art", Cultivation.System.DIVINE,
			new TechniquesModifiers(2, 1, 3, 1, 3, 2), 2.8, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.LIGHT)
			.addElement(Element.FIRE)
			.addSkill(2000, Skills.FLAMES)
			.addSkill(10000, Skills.FIRE_BAll);

	//From Febian
	public static final Technique AMORPHOUS_METALLIC_SOUL = new Technique("amorphous_metallic_soul", Cultivation.System.DIVINE,
			new TechniquesModifiers(2, 1, 3, 1, 3, 7), 6.1, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.METAL)
			.addSkill(2000, Skills.METAL_DETECTION)
			.addSkill(10000, Skills.ORE_SUCTION);

	//From Febian
	//Does extra damage against living
	public static final Technique NINE_SPRINGS_SOUL = new Technique("nine_springs_soul", Cultivation.System.DIVINE,
			new TechniquesModifiers(2, 1, 3, 1, 3, 4), 6.2, 2300000, 800)
			.addCheckpoint(1000, "Minor Success")
			.addCheckpoint(10000, "Small Success")
			.addCheckpoint(90000, "Middle Success")
			.addCheckpoint(600000, "Natural Success")
			.addCheckpoint(1500000, "Great Success")
			.addCheckpoint(23000000, "Perfected")
			.addElement(Element.DARK);

	//From HuoYuhao
	//If player kills, he loses something
	//rank divine 1 -- yeah this one deserves
	public static final Technique BUDDHA_S_HEAVENLY_WAY = new Technique("buddha_s_heavenly_way", Cultivation.System.DIVINE,
			new TechniquesModifiers(2, 1, 3, 1, 3, 16), 18.2, 1800000000000.0, 6991714.27)
			.addCheckpoint(1350, "Tiny Buddha")
			.addCheckpoint(9121, "Connate Buddha")
			.addCheckpoint(61601, "Peaceful Mind")
			.addCheckpoint(416016, "Constant Zen")
			.addCheckpoint(2809512, "Small Buddhahood")
			.addCheckpoint(18973665, "Holy Buddha Soul")
			.addCheckpoint(128136087, "Medium Buddhahood")
			.addCheckpoint(865349742, "Saint Buddha Soul")
			.addCheckpoint(5844022478.0, "Greater Buddhahood")
			.addCheckpoint(39466815629.0, "Reincarnation Free")
			.addCheckpoint(266533802983.0, "Total Abnegation")
			.addCheckpoint(1800000000000.0, "Perfected Buddha");


}
