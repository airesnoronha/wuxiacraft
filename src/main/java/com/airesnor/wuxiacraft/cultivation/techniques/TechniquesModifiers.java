package com.airesnor.wuxiacraft.cultivation.techniques;

public class TechniquesModifiers {

	public float armor;
	public float attackSpeed;
	public float movementSpeed;
	public float strength;
	public float maxHealth;

	public TechniquesModifiers(float armor, float attackSpeed, float maxHealth, float movementSpeed, float strength) {
		this.armor = armor;
		this.attackSpeed = attackSpeed;
		this.movementSpeed = movementSpeed;
		this.strength = strength;
		this.maxHealth = maxHealth;
	}
}
