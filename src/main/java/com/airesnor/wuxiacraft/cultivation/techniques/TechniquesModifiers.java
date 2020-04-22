package com.airesnor.wuxiacraft.cultivation.techniques;

public class TechniquesModifiers {

	public final double armor;
	public final double attackSpeed;
	public final double movementSpeed;
	public final double strength;
	public final double maxHealth;

	public TechniquesModifiers(double armor, double attackSpeed, double maxHealth, double movementSpeed, double strength) {
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.movementSpeed = movementSpeed;
		this.strength = strength;
		this.maxHealth = maxHealth;
	}
}
