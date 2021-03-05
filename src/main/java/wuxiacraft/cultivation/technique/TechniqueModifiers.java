package wuxiacraft.cultivation.technique;

public class TechniqueModifiers {

	public final double attackSpeed;
	public final double movementSpeed;
	public final double strength;
	public final double maxHealth;

	public TechniqueModifiers(double attackSpeed, double maxHealth, double movementSpeed, double strength) {
		this.attackSpeed = attackSpeed;
		this.movementSpeed = movementSpeed;
		this.strength = strength;
		this.maxHealth = maxHealth;
	}

	public TechniqueModifiers multiply(double amount) {
		return new TechniqueModifiers(amount * attackSpeed,
				amount * maxHealth,
				amount * movementSpeed,
				amount * strength);
	}

	public TechniqueModifiers add(TechniqueModifiers modifiers) {
		return new TechniqueModifiers(modifiers.attackSpeed + attackSpeed,
				modifiers.maxHealth + maxHealth,
				modifiers.movementSpeed + movementSpeed,
				modifiers.strength + strength);
	}

}
