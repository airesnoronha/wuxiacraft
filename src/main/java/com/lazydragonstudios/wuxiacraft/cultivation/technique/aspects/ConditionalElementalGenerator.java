package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import net.minecraft.resources.ResourceLocation;

public abstract class ConditionalElementalGenerator extends ElementalGenerator {
	public ConditionalElementalGenerator(double generated, ResourceLocation element) {
		super(generated, element);
	}

	public abstract void onCultivate(CultivatingEvent event);
}
