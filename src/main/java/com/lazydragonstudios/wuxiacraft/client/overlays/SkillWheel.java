package com.lazydragonstudios.wuxiacraft.client.overlays;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.InputHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SkillWheel implements IIngameOverlay {

	private static ResourceLocation SKILL_WHEEL = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/skill_wheel.png");

	private int selectedSkill = 0;

	@Override
	public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width, int height) {
		if (!InputHandler.mappings.get(InputHandler.SKILL_WHEEL).isDown()) return;
		var window = Minecraft.getInstance().getWindow();
		var scaledX = window.getGuiScaledWidth();
		var scaledY = window.getGuiScaledHeight();
		var middleX = scaledX / 2;
		var middleY = scaledY / 2;
		RenderSystem.setShaderTexture(0, SKILL_WHEEL);
		RenderSystem.enableBlend();
		poseStack.pushPose();
		poseStack.translate(middleX, middleY, 0);
		for (int i = 0; i < 10; i++) {
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(36));
			GuiComponent.blit(poseStack, -31, -102, 62, 72, 0, 0, 62, 72, 256, 256);
		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}
}
