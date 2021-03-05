package wuxiacraft.cultivation.technique;

import wuxiacraft.cultivation.CultivationLevel;

public class BodyTechnique extends Technique{

	private final double conversionRate;

	public BodyTechnique(String name, TechniqueModifiers baseModifiers, double cultivationSpeed, double maxProficiency, double efficientTillModifier, double energyRegen, double maxEnergy, double healingCostModifier, double healingAmountModifier, double conversionRate) {
		super(CultivationLevel.System.BODY, name, baseModifiers, cultivationSpeed, maxProficiency, efficientTillModifier, energyRegen, maxEnergy, healingCostModifier, healingAmountModifier);
		this.conversionRate = conversionRate;
	}

	/**
	 *
	 * @return the ratio that blood energy will become essence energy
	 */
	public double getConversionRate() {
		return conversionRate;
	}
}
