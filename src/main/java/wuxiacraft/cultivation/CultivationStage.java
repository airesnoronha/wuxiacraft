package wuxiacraft.cultivation;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class CultivationStage extends ForgeRegistryEntry<CultivationStage> {

	/**
	 * The name of this stage
	 */
	public final String name;

	/**
	 * The cultivation system this belongs to
	 */
	public final Cultivation.System system;

	/**
	 * The amount opf cultivation base required for this stage to advance
	 */
	public final double cultivationBase;

	/**
	 * The base max Energy of this stage.
	 * Can be modified by cultivation techniques.
	 */
	public final double maxEnergy;

	/**
	 * The base maxHealth of this stage.
	 * Can be modified by cultivation techniques.
	 */
	public final double maxHealth;

	/**
	 * The base passive energy generation.
	 * Can be modified by cultivation techniques.
	 */
	public final double energyRegenRate;

	/**
	 * The base punch strength.
	 * Can be modified by cultivation techniques.
	 */
	public final double strength;

	/**
	 * The base running speed.
	 * This value is a multiplier of vanilla's walking speed.
	 * Can be modified by cultivation techniques.
	 */
	public final double agility;

	/**
	 * Constructor for this cultivation stage
	 * @param name The stage name
	 * @param system the Stage Cultivation System
	 * @param cultivationBase the amount of cultivation base required to advance
	 * @param maxEnergy the max amount of energy of this stage
	 * @param maxHealth the max amount of health of this stage
	 * @param energyRegenRate the passive energy regen at this stage
	 * @param strength the expected punch strength for this level
	 * @param agility the expected running speed of this level
	 */
	public CultivationStage(String name, Cultivation.System system, double cultivationBase, double maxEnergy, double maxHealth, double energyRegenRate, double strength, double agility) {
		this.name = name;
		this.system = system;
		this.cultivationBase = cultivationBase;
		this.maxEnergy = maxEnergy;
		this.maxHealth = maxHealth;
		this.energyRegenRate = energyRegenRate;
		this.strength = strength;
		this.agility = agility;
	}
}