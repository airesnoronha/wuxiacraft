package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;

public class DivineTechnique extends Technique {

	public final double scanFactor;
	public final double resistFactor;

	public DivineTechnique(String uName, TechniqueModifiers baseModifiers, double cultSpeed, double maxProficiency, double efficientTillModifier, double scanFactor, double resistFactor) {
		super(uName, Cultivation.System.DIVINE, baseModifiers, cultSpeed, maxProficiency, efficientTillModifier);
		this.scanFactor = scanFactor;
		this.resistFactor = resistFactor;
	}
}
