package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class AspectElementalGenerator extends TechniqueAspect {

	public double generated;

	public ResourceLocation element;

	public AspectElementalGenerator(String name, double generated, ResourceLocation element) {
		super(name);
		this.generated = generated;
		this.element = element;
	}

	@Override
	public void accept(HashMap<String, Object> metaData) {
		String elementBase = "element-base-" + element.getPath();
		metaData.put(elementBase, (double)metaData.getOrDefault(elementBase, 0d) + this.generated);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectElementalConsumer) return true;
		if(aspect instanceof AspectElementalConverter) return true;
		return false;
	}
}
