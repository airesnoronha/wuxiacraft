package wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public abstract class AspectElementalConsumer extends TechniqueAspect {

	public ResourceLocation element;
	public double cost;

	public AspectElementalConsumer(String name, ResourceLocation textureLocation, ResourceLocation element, double cost) {
		super(name, textureLocation);
		this.element = element;
		this.cost = cost;
	}

	@Override
	public void accept(HashMap<String, Object> metaData) {
		String elementBase = "element-base-" + element.getPath();
		if(metaData.containsKey(elementBase)) {
			double elementalCultivationBase = (double) metaData.get(elementBase);
			if(elementalCultivationBase >= cost) {
				elementalCultivationBase -= cost;
				metaData.put(elementBase, elementalCultivationBase);
				consumed(metaData);
			} else {
				notConsumed(metaData);
			}
		}
	}

	public abstract void consumed(HashMap<String, Object> metaData);
	public abstract void notConsumed(HashMap<String, Object> metaData);

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectElementalConsumer) return true;
		if(aspect instanceof AspectElementalConverter) return true;
		if(aspect instanceof AspectElementalGenerator) return true;
		return false;
	}
}
