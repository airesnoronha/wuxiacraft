package com.lazydragonstudios.wuxiacraft.cultivation.technique;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.ConditionalElementalGenerator;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TechniqueEventHandler {

	@SubscribeEvent
	public static void onCultivateCustomAspect(CultivatingEvent event) {
		var techniqueData = Cultivation.get(event.getPlayer()).getSystemData(event.getSystem()).techniqueData;
		var grid = techniqueData.grid;
		for (var aspectLocation : grid.getGrid().values()) {
			var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(aspectLocation);
			if (aspect == null) continue;
			if (event.isCanceled()) break;
			if (aspect instanceof ConditionalElementalGenerator generator) {
				generator.onCultivate(event);
			}
		}
	}
}
