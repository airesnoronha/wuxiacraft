package com.airesnor.wuxiacraft.cultivation.techniques;

public class TechniqueModifiers {

	public final double armor;
	public final double attackSpeed;
	public final double movementSpeed;
	public final double strength;
	public final double maxHealth;
	public final double maxEnergy;

	public TechniqueModifiers(double armor, double attackSpeed, double maxHealth, double movementSpeed, double strength, double maxEnergy) {
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.movementSpeed = movementSpeed;
		this.strength = strength;
		this.maxHealth = maxHealth;
		this.maxEnergy = maxEnergy;
	}

	public TechniqueModifiers multiply(double amount) {
		return new TechniqueModifiers(amount * armor,
				amount * attackSpeed,
				amount * maxHealth,
				amount * movementSpeed,
				amount * strength,
				amount * maxEnergy);
	}

	public TechniqueModifiers add(TechniqueModifiers modifiers) {
		return new TechniqueModifiers(modifiers.armor + armor,
				modifiers.armor + attackSpeed,
				modifiers.armor + maxHealth,
				modifiers.armor + movementSpeed,
				modifiers.armor + strength,
				modifiers.armor + maxEnergy);
	}
}
