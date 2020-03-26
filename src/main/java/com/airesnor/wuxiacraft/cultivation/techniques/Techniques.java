package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Techniques {

	public static final List<Technique> TECHNIQUES = new ArrayList<>();

	public static void init() {
		TECHNIQUES.add(BODY_STRENGTH);
		TECHNIQUES.add(LIGHT_FEET);
		TECHNIQUES.add(ASSASSIN_MANUAL);
		TECHNIQUES.add(BASIC_MEDICINE);
		TECHNIQUES.add(SWORD_HEART);
		TECHNIQUES.add(AXE_RAGE);
		TECHNIQUES.add(FIRE_BENDING);
		TECHNIQUES.add(MOUNTAIN_RAISER);
		TECHNIQUES.add(METAL_MANIPULATION);
		TECHNIQUES.add(SURGING_WAVES);
		TECHNIQUES.add(BOTANICAL_GROWTH);
		TECHNIQUES.add(SWORD_FLIGHT_JOURNAL);
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

	//Mortal ones
	public static final Technique BODY_STRENGTH = new Technique(TechniqueTier.MORTAL, "body_strength", new TechniquesModifiers(0.1f, 0f, 0f, -0.1f, 0.1f), 1.07f)
			.addSmallSkill(Skills.MINOR_POWER_PUNCH)
			.addGreatSkill(Skills.MINOR_BODY_REINFORCEMENT)
			.addPerfectionEffect(STRENGTH_I);
	public static final Technique LIGHT_FEET = new Technique(TechniqueTier.MORTAL, "light_feet", new TechniquesModifiers(0f, 0f, 0f, 0.2f, -0.1f))
			.addSmallSkill(Skills.LIGHT_FEET_LEAP)
			.addGreatSkill(Skills.LIGHT_FEET_SKILL)
			.addPerfectionEffect(SPEED_I);
	public static final Technique ASSASSIN_MANUAL = new Technique(TechniqueTier.MORTAL, "assassin_manual", new TechniquesModifiers(0f, 0.01f, -0.1f, 0.1f, 0f))
			.addGreatSkill(Skills.WALL_CROSSING)
			.addPerfectionEffect(NIGHT_VISION);
	public static final Technique BASIC_MEDICINE = new Technique(TechniqueTier.MORTAL, "basic_medicine", new TechniquesModifiers(0f, -0.1f, 0.1f, 0f, -0.1f))
			.addGreatEffect(REGENERATION_I)
			.addPerfectionEffect(REGENERATION_I)
			.addSmallSkill(Skills.SELF_HEALING)
			.addGreatSkill(Skills.HEALING_HANDS);
	public static final TechniqueWeapon SWORD_HEART = (TechniqueWeapon) new TechniqueWeapon(TechniqueTier.MORTAL, "sword_heart",
			new TechniquesModifiers(0f, 0.2f, -0.2f, 0.1f, 0f), TechniqueWeapon.WeaponType.SWORD)
			.addSmallSkill(Skills.WEAK_SWORD_BEAM)
			.addPerfectSkill(Skills.SWORD_BEAM_BARRAGE);
	public static final TechniqueWeapon AXE_RAGE = new TechniqueWeapon(TechniqueTier.MORTAL, "axe_rage",
			new TechniquesModifiers(-0.2f, 0.1f, 0f, 0f, 0.2f), TechniqueWeapon.WeaponType.AXE);
	public static final Technique FIRE_BENDING = new Technique(TechniqueTier.MORTAL, "fire_bending", new TechniquesModifiers(0f, 0f, 0f, 0.1f, 0.1f), 1.1f).addElement(Element.FIRE)
			.addSmallSkill(Skills.FLAMES)
			.addGreatSkill(Skills.FIRE_BAll);
	public static final Technique MOUNTAIN_RAISER = new Technique(TechniqueTier.MORTAL, "mountain_raiser", new TechniquesModifiers(0.1f, 0f, 0.1f, 0f, 0f), 1.1f).addElement(Element.EARTH)
			.addSmallSkill(Skills.EARTH_SUCTION)
			.addGreatSkill(Skills.EARTHLY_WALL);
	public static final Technique METAL_MANIPULATION = new Technique(TechniqueTier.MORTAL, "metal_manipulation", new TechniquesModifiers(0.1f, 0f, 0f, 0f, 0.1f), 1.1f).addElement(Element.METAL)
			.addSmallSkill(Skills.METAL_DETECTION)
			.addSmallSkill(Skills.ORE_SUCTION);
	public static final Technique SURGING_WAVES = new Technique(TechniqueTier.MORTAL, "surging_waves", new TechniquesModifiers(0f, 0.1f, 0f, 0.1f, 0f), 1.1f).addElement(Element.WATER)
			.addSmallSkill(Skills.WATER_NEEDLE)
			.addGreatSkill(Skills.WATER_BLADE);
	public static final Technique BOTANICAL_GROWTH = new Technique(TechniqueTier.MORTAL, "botanical_growth", new TechniquesModifiers(0f, 0f, 0.1f, 0f, 0.1f), 1.1f).addElement(Element.WOOD)
			.addSmallSkill(Skills.GATHER_WOOD)
			.addGreatSkill(Skills.ACCELERATE_GROWTH);
	public static final Technique SWORD_FLIGHT_JOURNAL = new TechniqueWeapon(TechniqueTier.MORTAL, "sword_flight_journal",
			new TechniquesModifiers(0f, 0f, 0, 0.3f, 0f), TechniqueWeapon.WeaponType.SWORD)
			.addGreatSkill(Skills.WEAK_SWORD_FLIGHT);

}
