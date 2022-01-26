package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

public class AspectStart extends TechniqueAspect {
	public AspectStart(String name, ResourceLocation textureLocation) {
		super(name, textureLocation);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectElementalGenerator) return true;
		return false;
	}
}
