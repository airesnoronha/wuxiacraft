package wuxiacraft.cultivation.technique;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashSet;

public class TechniqueAspect extends ForgeRegistryEntry<TechniqueAspect> {

	//Class itself
	public String name;

	public HashSet<ResourceLocation> expectedAspects;

	public TechniqueModifier modifier;

	public TechniqueAspect(String name) {
		this.name = name;
		expectedAspects = new HashSet<>();
		modifier = new TechniqueModifier();
	}

	public TechniqueAspect addExpected(ResourceLocation aspect) {
		expectedAspects.add(aspect);
		return this;
	}

	public TechniqueAspect setModifiers(TechniqueModifier modifiers) {
		this.modifier = modifiers;
		return this;
	}

	/**
	 * Logic if aspect was expected
	 * @param tMod the current modifiers when accepting this
	 */
	public void accept(TechniqueModifier tMod) {
		tMod.add(this.modifier);
	}

	/**
	 * Logic if aspect is junk, or not expected
	 * @param tMod the current modifiers when rejecting this
	 */
	public void reject(TechniqueModifier tMod) {
		tMod.subtract(this.modifier);
		tMod.subtract(this.modifier);
	}

	/**
	 * Logic if aspect is disconnected from start node
	 * @param tMod the current modifiers when this is disconnected
	 */
	public void disconnect(TechniqueModifier tMod) {
		tMod.subtract(this.modifier);
	}

}
