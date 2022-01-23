package wuxiacraft.cultivation.technique.aspects;

import java.util.HashMap;

public class AspectStart extends TechniqueAspect {
	public AspectStart(String name) {
		super(name);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectElementalGenerator) return true;
		return false;
	}
}
