package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;

public abstract class TechniqueAspect extends ForgeRegistryEntry<TechniqueAspect> {

	//Class itself
	public final String name;

	public final ResourceLocation textureLocation;

	public TechniqueAspect(String name, ResourceLocation textureLocation) {
		this.name = name;
		this.textureLocation = textureLocation;
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
