package wuxiacraft.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;


public class RenderHudHandler {

	@SubscribeEvent
	public void onPreRenderHud(RenderGameOverlayEvent.Pre event) {
		if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) event.setCanceled(true);
	}

	@SubscribeEvent
	public void onRenderHud(RenderGameOverlayEvent.Post event) {
		if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE)
		drawHudElements(event.getMatrixStack(), event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
	}

	private static void drawHudElements(MatrixStack stack, int width, int height) {
		assert Minecraft.getInstance().player != null;
		ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);

		String text = String.format("HP: %d/%d", (int)cultivation.getHP(), (int)cultivation.getFinalModifiers().maxHealth);
		Minecraft.getInstance().ingameGUI.getFontRenderer().drawString(stack, text, 30, 30, 0xFFAA00);
	}

}
