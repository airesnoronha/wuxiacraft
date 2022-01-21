package wuxiacraft.cultivation;

import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;

public class Technique extends ForgeRegistryEntry<Technique> {

	public final String name;

	public final Cultivation.System system;

	public final double healthModifier;

	public final double strengthModifier;

	public final double agilityModifier;

	public final double energyModifier;

	public final double energyRegenModifier;

	public final double cultivationSpeed;

	public final HashMap<String, Checkpoint> checkpoints = new HashMap<>();
	private String firstCheckpoint = null;

	public Technique(String name, Cultivation.System system, double healthModifier, double strengthModifier, double agilityModifier, double energyModifier, double energyRegenModifier, double cultivationSpeed) {
		this.name = name;
		this.system = system;
		this.healthModifier = healthModifier;
		this.strengthModifier = strengthModifier;
		this.agilityModifier = agilityModifier;
		this.energyModifier = energyModifier;
		this.energyRegenModifier = energyRegenModifier;
		this.cultivationSpeed = cultivationSpeed;
	}

	public Technique addCheckpoint(String name, double proficiency, double modifier, String nextCheckpoint) {
		this.checkpoints.put(name, new Checkpoint(name, proficiency, modifier, nextCheckpoint));
		if(firstCheckpoint == null) {
			firstCheckpoint = name;
		}
		return this;
	}

	public Checkpoint getCheckpoint(double proficiency) {
		Checkpoint checkpointToReturn = null;
		for (Checkpoint c = checkpoints.get(firstCheckpoint); c.nextCheckpoint != null; c = checkpoints.get(c.nextCheckpoint)) {
			if (proficiency > c.proficiency) {
				checkpointToReturn = c;
			}
		}
		return checkpointToReturn;
	}

	public record Checkpoint(String name, double proficiency, double modifier,@Nullable String nextCheckpoint) {
	}

}
