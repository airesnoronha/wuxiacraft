package com.airesnor.wuxiacraft.cultivation.techniques;

import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Techniques {

	public static final List<Technique> TECHNIQUES = new ArrayList<>();

	public static void init() {
		TECHNIQUES.add(BODY_STRENGTH);
		TECHNIQUES.add(LIGHT_FEET);
		TECHNIQUES.add(ASSASSIN_MANUAL);
	}

	public static String getTechniqueName(Technique technique) {
		return I18n.format("wuxiacraft.techniques." + technique.getUName());
	}

	public static Technique BODY_STRENGTH = new Technique(TechniqueTier.MORTAL, "body_strength", new TechniquesModifiers(0.1f,0f,0f,-0.1f,0.1f));
	public static Technique LIGHT_FEET = new Technique(TechniqueTier.MORTAL, "light_feet", new TechniquesModifiers(0f,0f,0f,0.2f,-0.1f));
	public static Technique ASSASSIN_MANUAL = new Technique(TechniqueTier.MORTAL, "assassin_manual", new TechniquesModifiers(0f,0f,0f,0.1f,0f))
			.addSmallEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 420, 0))
			.addGreatEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 420, 0))
			.addPerfectionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16)), 420, 0));
	public static Technique BASIC_MEDICINE = new Technique(TechniqueTier.MORTAL, "basic_medicine", new TechniquesModifiers(0f, 0f,0.1f,0f,0f))
			.addGreatEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(10)), 100, 0))
			.addPerfectionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(10)), 100, 0));
	//public static Technique


}
