package com.airesnor.wuxiacraft.cultivation.techniques;

public class TechniquesModifiers {

	public final double armor;
	public final double attackSpeed;
	public final double movementSpeed;
	public final double strength;
	public final double maxHealth;
	public final double maxEnergy;

	public TechniquesModifiers(double armor, double attackSpeed, double maxHealth, double movementSpeed, double strength, double maxEnergy) {
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.movementSpeed = movementSpeed;
		this.strength = strength;
		this.maxHealth = maxHealth;
		this.maxEnergy = maxEnergy;
	}

	public TechniquesModifiers multiply(double amount) {
		return new TechniquesModifiers(amount * armor,
				amount * attackSpeed,
				amount * maxHealth,
				amount * movementSpeed,
				amount * strength,
				amount * maxEnergy);
	}

	public TechniquesModifiers add(TechniquesModifiers modifiers) {
		return new TechniquesModifiers(modifiers.armor + armor,
				modifiers.armor + attackSpeed,
				modifiers.armor + maxHealth,
				modifiers.armor + movementSpeed,
				modifiers.armor + strength,
				modifiers.armor + maxEnergy);
	}
}
