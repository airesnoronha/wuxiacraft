package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wuxiacraft.cultivation.technique.TechniqueModifier;

import java.util.HashMap;
import java.util.HashSet;

public abstract class TechniqueAspect extends ForgeRegistryEntry<TechniqueAspect> {

	//Class itself
	public String name;

	public HashSet<ResourceLocation> expectedAspects;

	public TechniqueAspect(String name) {
		this.name = name;
		expectedAspects = new HashSet<>();
	}

	public TechniqueAspect addExpected(ResourceLocation aspect) {
		expectedAspects.add(aspect);
		return this;
	}
	/**
	 * Logic if aspect was expected
	 * @param metaData the current modifiers when accepting this
	 */
	public void accept(HashMap<String, Object> metaData) {
	}

	/**
	 * Logic if aspect is junk, or not expected
	 * @param metaData the current modifiers when rejecting this
	 */
	public void reject(HashMap<String, Object> metaData) {
	}

	/**
	 * Logic if aspect is disconnected from start node
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

}
