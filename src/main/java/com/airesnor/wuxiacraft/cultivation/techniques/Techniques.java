package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.init.MobEffects;

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
	}

	public static Technique getTechniqueByUName(String uName) {
		for (Technique t : TECHNIQUES) {
			if(t.getUName().equals(uName)) {
				return t;
			}
		}
		return null;
	}

	//User potion effects
	private static final PotionEffect REGENERATION_I = new PotionEffect(MobEffects.REGENERATION, 100, 0, true, false);
	private static final PotionEffect NIGHT_VISION = new PotionEffect(MobEffects.NIGHT_VISION, 410, 0, false, false);

	//Mortal ones
	public static final Technique BODY_STRENGTH = new Technique(TechniqueTier.MORTAL, "body_strength", new TechniquesModifiers(0.1f,0f,0f,-0.1f,0.1f));
	public static final Technique LIGHT_FEET = new Technique(TechniqueTier.MORTAL, "light_feet", new TechniquesModifiers(0f,0f,0f,0.2f,-0.1f));
	public static final Technique ASSASSIN_MANUAL = new Technique(TechniqueTier.MORTAL, "assassin_manual", new TechniquesModifiers(0f,0.01f,-0.1f,0.1f,0f))
			.addPerfectionEffect(NIGHT_VISION);
	public static final Technique BASIC_MEDICINE = new Technique(TechniqueTier.MORTAL, "basic_medicine", new TechniquesModifiers(0f, -0.1f,0.1f,0f,-0.1f))
			.addGreatEffect(REGENERATION_I)
			.addPerfectionEffect(REGENERATION_I);
	public static final TechniqueWeapon SWORD_HEART = new TechniqueWeapon(TechniqueTier.MORTAL, "sword_heart",
			new TechniquesModifiers(0f,0.2f,-0.2f,0.1f,0f), TechniqueWeapon.WeaponType.SWORD);
	public static final TechniqueWeapon AXE_RAGE = new TechniqueWeapon(TechniqueTier.MORTAL, "axe_rage",
			new TechniquesModifiers(-0.2f,0.1f,0f,0f,0.2f), TechniqueWeapon.WeaponType.AXE);
	public static final Technique FIRE_BENDING = new Technique(TechniqueTier.MORTAL,"fire_bending",new TechniquesModifiers(0f,0f,0f,0.1f,0.1f)).addElement(Element.FIRE);
	public static final Technique MOUNTAIN_RAISER = new Technique(TechniqueTier.MORTAL,"mountain_raiser",new TechniquesModifiers(0.1f,0f,0.1f,0f,0f)).addElement(Element.EARTH);
	public static final Technique METAL_MANIPULATION = new Technique(TechniqueTier.MORTAL,"metal_manipulation",new TechniquesModifiers(0.1f,0f,0f,0f,0.1f)).addElement(Element.METAL);
	public static final Technique SURGING_WAVES = new Technique(TechniqueTier.MORTAL,"surging_waves",new TechniquesModifiers(0f,0.1f,0f,0.1f,0f)).addElement(Element.WATER);
	public static final Technique BOTANICAL_GROWTH = new Technique(TechniqueTier.MORTAL,"botanical_growth",new TechniquesModifiers(0f,0f,0.1f,0f,0.1f)).addElement(Element.WOOD).addSmallSkill(Skills.GATHER_WOOD);

}
