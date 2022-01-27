package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wuxiacraft.cultivation.ICultivation;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
public abstract class TechniqueAspect extends ForgeRegistryEntry<TechniqueAspect> {

	//Class itself
	public final String name;

	public final ResourceLocation textureLocation;

	public final LinkedList<Checkpoint> checkpoints;

	public TechniqueAspect(String name, ResourceLocation textureLocation) {
		this.name = name;
		this.textureLocation = textureLocation;
		this.checkpoints = new LinkedList<>();
	}

	/**
	 * adds a checkpoint to this aspect
	 * @param checkpoint the checkpoint to be added
	 * @return this
	 */
	public TechniqueAspect addCheckpoint(Checkpoint checkpoint) {
		this.checkpoints.add(checkpoint);
		return this;
	}

	/**
	 * gets the current checkpoint for the specified proficiency
	 *
	 * @param proficiency the proficiency of this aspect
	 * @return the current checkpoint
	 */
	public Checkpoint getCurrentCheckpoint(BigDecimal proficiency) {
		Checkpoint currentSelected = this.checkpoints.getFirst();
		for (var checkpoint : this.checkpoints) {
			if (checkpoint.proficiencyRequired.compareTo(proficiency) < 0) {
				currentSelected = checkpoint;
			}
		}
		return currentSelected;
	}

	/**
	 * Logic if aspect was expected
	 *
	 * @param metaData the current modifiers when accepting this
	 */
	public void accept(HashMap<String, Object> metaData) {
	}

	/**
	 * Logic if aspect is junk, or not expected
	 *
	 * @param metaData the current modifiers when rejecting this
	 */
	public void reject(HashMap<String, Object> metaData) {
	}

	/**
	 * Logic if aspect is disconnected from start node
	 *
	 * @param metaData the current modifiers when this is disconnected
	 */
	public void disconnect(HashMap<String, Object> metaData) {
	}

	/**
	 * this is meant to be overridden
	 * Whether the can connect to neighbour aspect
	 *
	 * @param aspect aspect to be connected to
	 * @return true if it can be connected
	 */
	public boolean canConnect(TechniqueAspect aspect) {
		return true;
	}

	public record Checkpoint(String name, BigDecimal proficiencyRequired, Consumer<ICultivation> onReached) {
	}

}
