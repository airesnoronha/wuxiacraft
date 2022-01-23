package wuxiacraft.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.System;
import wuxiacraft.cultivation.stats.PlayerSystemStat;

import java.math.RoundingMode;

@SuppressWarnings("FieldCanBeLocal")
public class EnergiesOverlay implements IIngameOverlay {

	private static final ResourceLocation ENERGY_BAR = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_bar.png");
	private static final ResourceLocation ENERGY_BODY_BAR_FILL = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_body_fill.png");
	private static final ResourceLocation ENERGY_DIVINE_BAR_FILL = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_divine_fill.png");
	private static final ResourceLocation ENERGY_ESSENCE_BAR_FILL = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/energy_essence_fill.png");

	private static int barInScreenX = 6-256;
	private static int barInScreenY = 5-256;
	private static int[] barFillInScreenX = new int[]{8 - 256, 131 - 256, 71 - 256};
	private static int[] barFillInScreenY = new int[]{70 - 256, 161 - 256, 8 - 256};

	private static final int barX = 6;
	private static final int barY = 5;
	private static final int barWidth = 504;
	private static final int barHeight = 504;

	private static final int[] barFillsWidth = new int[]{264, 376, 417};
	private static final int[] barFillsHeight = new int[]{428, 345, 309};


	@Override
	public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		var cultivation = Cultivation.get(mc.player);

		barInScreenX = 6;
		barInScreenY = 5;
		barFillInScreenX = new int[]{8, 131, 71};
		barFillInScreenY = new int[]{70, 161, 8};

		mStack.pushPose();
		mStack.translate(5, height - 75, 0);
		mStack.scale(70f/512f, 70f/512f, 1);

		for (var system : System.values()) {
			var systemData = cultivation.getSystemData(system);
			int index = system.ordinal();
			int maxFillAmount = system == System.BODY ? barFillsHeight[index] : barFillsWidth[index];
			int fillAmount = (int) ((float) maxFillAmount * systemData.getStat(PlayerSystemStat.ENERGY).divide(systemData.getStat(PlayerSystemStat.MAX_ENERGY), RoundingMode.HALF_UP).floatValue());
			RenderSystem.setShaderTexture(0, ENERGY_BAR);
			int x, y, bWidth, bHeight;
			int texX, texY;
			x = barFillInScreenX[index];
			y = barFillInScreenY[index];
			texX = 0;
			texY = 0;
			if (system == System.ESSENCE) {
				bWidth = fillAmount;
				bHeight = barFillsHeight[index];
				RenderSystem.setShaderTexture(0, ENERGY_ESSENCE_BAR_FILL);
			} else if (system == System.DIVINE) {
				bWidth = fillAmount;
				bHeight = barFillsHeight[index];
				x += barFillsWidth[index] - fillAmount;
				texX += barFillsWidth[index] - fillAmount;
				RenderSystem.setShaderTexture(0, ENERGY_DIVINE_BAR_FILL);
			} else {
				bWidth = barFillsWidth[index];
				bHeight = fillAmount;
				y += barFillsHeight[index] - fillAmount;
				texY += barFillsHeight[index] - fillAmount;
				RenderSystem.setShaderTexture(0, ENERGY_BODY_BAR_FILL);
			}
			GuiComponent.blit(mStack,
							x, y, // bar in screen position
							bWidth, bHeight, // bar in screen fill
							texX, texY, // bar tex position
							bWidth, bHeight, // bar fill
							barFillsWidth[index], barFillsHeight[index]); // tex image size
		}

		RenderSystem.setShaderTexture(0, ENERGY_BAR);
		GuiComponent.blit(mStack,
						barInScreenX, barInScreenY, // bar in screen position
						barWidth, barHeight, // bar in screen fill
						barX, barY, // bar tex position
						barWidth, barHeight, // bar fill
						512, 512); // tex image size
						
		mStack.popPose();

	}
}
