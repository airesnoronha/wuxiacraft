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
		this.strength += tMod.strength;
		this.agility += tMod.agility;
		this.health += tMod.health;
		this.energy += tMod.energy;
		this.energyRegen += tMod.energyRegen;
		this.cultivation_speed += tMod.cultivation_speed;
		for(var key : tMod.elements.keySet()) {
			if(this.elements.containsKey(key)) {
				this.elements.put(key, this.elements.get(key) + tMod.elements.get(key));
			}
			else {
				this.elements.put(key, tMod.elements.get(key));
			}
		}
	}

	public void subtract(TechniqueModifier tMod) {
		this.strength -= tMod.strength;
		this.agility -= tMod.agility;
		this.health -= tMod.health;
		this.energy -= tMod.energy;
		this.energyRegen -= tMod.energyRegen;
		this.cultivation_speed -= tMod.cultivation_speed;
		for(var key : tMod.elements.keySet()) {
			if(this.elements.containsKey(key)) {
				this.elements.put(key, this.elements.get(key) - tMod.elements.get(key));
			}
			else {
				this.elements.put(key, -tMod.elements.get(key));
			}
		}
	}

	//TODO serialize this
	public CompoundTag serialize() {
		return new CompoundTag();
	}

	// TODO deserialize this
	public void deserialize() {

	}

}
