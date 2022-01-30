package com.lazydragonstudios.wuxiacraft.client.overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class DebugOverlay implements IIngameOverlay {
	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		/*Minecraft mc = Minecraft.getInstance();
		if(mc.player == null) return;
		ICultivation cultivation = Cultivation.get(mc.player);
		int yPos = 30;
		for(var system : System.values()) {
			var systemData = cultivation.getSystemData(system);
			String text = String.format("%s Energy: %.1f/ %.1f (%.0f%%)", system.toString(), systemData.getStat(PlayerSystemStat.ENERGY), systemData.getStat(PlayerSystemStat.MAX_ENERGY),
							(systemData.getStat(PlayerSystemStat.ENERGY).multiply(new BigDecimal("100.0")).divide(systemData.getStat(PlayerSystemStat.MAX_ENERGY), RoundingMode.HALF_UP)));
			gui.getFont().draw(mStack, text, 10, yPos, 0xFFAA00);
			yPos+= 10;
		}*/
	}
}
