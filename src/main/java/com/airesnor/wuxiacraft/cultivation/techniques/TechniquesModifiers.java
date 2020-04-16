package com.airesnor.wuxiacraft.cultivation.techniques;

public class TechniquesModifiers {

	public double armor;
	public double attackSpeed;
	public double movementSpeed;
	public double strength;
	public double maxHealth;

	public TechniquesModifiers(double armor, double attackSpeed, double maxHealth, double movementSpeed, double strength) {
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.movementSpeed = movementSpeed;
		this.strength = strength;
		this.maxHealth = maxHealth;
	}
}
