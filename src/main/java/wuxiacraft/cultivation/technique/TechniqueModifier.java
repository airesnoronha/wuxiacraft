package wuxiacraft.cultivation.technique;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class TechniqueModifier {

	public double strength = 0;
	public double agility = 0;
	public double health = 0;
	public double energy = 0;
	public double energyRegen = 0;
	public double cultivation_speed = 0;
	public HashMap<ResourceLocation, Double> elements = new HashMap<>();

	public TechniqueModifier() {
	}

	public TechniqueModifier(double strength, double agility, double health, double energy, double energyRegen, double cultivation_speed) {
		this.strength = strength;
		this.agility = agility;
		this.health = health;
		this.energy = energy;
		this.energyRegen = energyRegen;
		this.cultivation_speed = cultivation_speed;
	}

	public void add(TechniqueModifier tMod) {

	}

	public void subtract(TechniqueModifier tMod) {
	}

	//TODO serialize this
	public CompoundTag serialize() {
		return new CompoundTag();
	}

	// TODO deserialize this
	public void deserialize() {

	}

}
