package wuxiacraft.client;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.widgets.WuxiaButton;
import wuxiacraft.client.overlays.EnergiesOverlay;
import wuxiacraft.client.overlays.HealthOverlay;
import wuxiacraft.init.WuxiaTechniqueAspects;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEventHandler {

	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
		event.addSprite(WuxiaButton.UI_CONTROLS);
		event.addSprite(EnergiesOverlay.ENERGY_BAR);
		event.addSprite(EnergiesOverlay.ENERGY_BODY_BAR_FILL);
		event.addSprite(EnergiesOverlay.ENERGY_DIVINE_BAR_FILL);
		event.addSprite(EnergiesOverlay.ENERGY_ESSENCE_BAR_FILL);
		event.addSprite(IntrospectionScreen.INTROSPECTION_GUI);
		event.addSprite(HealthOverlay.HEALTH_BAR);
		for(var aspectEntry : WuxiaTechniqueAspects.ASPECTS.getEntries()) {
			event.addSprite(aspectEntry.get().textureLocation);
		}
	}

}
