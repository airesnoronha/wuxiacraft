package wuxiacraft.cultivation;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;
import java.util.LinkedList;

public class Technique extends ForgeRegistryEntry<Technique> {

	public final String name;

	public final double healthModifier;

	public final double strengthModifier;

	public final double agilityModifier;

	public final double energyModifier;

	public final double energyRegenModifier;

	public final HashMap<String, Double> checkpoints = new HashMap<>();

	public final LinkedList<String> checkpointsOrder = new LinkedList<>();

	public Technique(String name, double healthModifier, double strengthModifier, double agilityModifier, double energyModifier, double energyRegenModifier) {
		this.name = name;
		this.healthModifier = healthModifier;
		this.strengthModifier = strengthModifier;
		this.agilityModifier = agilityModifier;
		this.energyModifier = energyModifier;
		this.energyRegenModifier = energyRegenModifier;
	}

	public Technique addCheckpoint(String name, double proficiency) {
		this.checkpoints.put(name, proficiency);
		this.checkpointsOrder.add(name);
		return this;
	}

	public String getCheckpoint(double proficiency) {
		String checkpointToReturn = null;
		for(String checkpoint : checkpointsOrder) {
			if(proficiency > checkpoints.get(checkpoint)) {
				checkpointToReturn = checkpoint;
			}
		}
		return checkpointToReturn;
	}

}
