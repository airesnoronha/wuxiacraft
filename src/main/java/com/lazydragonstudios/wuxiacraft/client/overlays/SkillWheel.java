package com.lazydragonstudios.wuxiacraft.client.overlays;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.InputHandler;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.networking.CultivationStateChangeMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class SkillWheel implements IIngameOverlay {

	private static ResourceLocation SKILL_WHEEL = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/overlay/skill_wheel.png");

	private int selectedSkill = 0;

	private boolean isShowingWheel = false;

	private Vec2 grabbedPosition = new Vec2(0, 0);

	@Override
	public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width, int height) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		if (!InputHandler.mappings.get(InputHandler.SKILL_WHEEL).isDown() || !cultivation.isCombat()) {
			if (isShowingWheel) {
				sendSelectedAnother();
			}
			isShowingWheel = false;
			return;
		}
		MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
		if (!isShowingWheel) {
			grabbedPosition = new Vec2((float) mouseHandler.xpos(), (float) mouseHandler.ypos());
		}
		isShowingWheel = true;
		var currentMousePos = new Vec2((float) mouseHandler.xpos(), (float) mouseHandler.ypos());
		var dx = currentMousePos.x - grabbedPosition.x;
		var dy = currentMousePos.y - grabbedPosition.y;
		var dist = Math.sqrt(dx * dx + dy * dy);
		double angle = 0;
		if (dist > 10) {
			angle = Math.atan2(dx, -dy) + Math.PI / 10;
			if (angle < 0) {
				angle = Math.PI * 2 + angle;
			}
			this.selectedSkill = (int) ((angle) * 10d / (2d * Math.PI));
		}

		var window = Minecraft.getInstance().getWindow();
		var scaledX = window.getGuiScaledWidth();
		var scaledY = window.getGuiScaledHeight();
		var middleX = scaledX / 2;
		var middleY = scaledY / 2;
		double length = 50;
		poseStack.pushPose();
		poseStack.translate(middleX, middleY, 0);
		var font = Minecraft.getInstance().font;
		for (int i = 0; i < 10; i++) {
			RenderSystem.setShaderTexture(0, SKILL_WHEEL);
			RenderSystem.enableBlend();
			int texX = 0;
			if (this.selectedSkill == i) {
				texX = 62;
			}
			GuiComponent.blit(poseStack, -31, -105, 62, 72, texX, 0, 62, 72, 256, 256);
			var skill = cultivation.getSkills().getSkillAt(i);
			int aspectCount = skill.getSkillChain().size();
			var divisions = aspectCount > 0 ? length / aspectCount : 0;
			for (int j = 0; j < aspectCount; j++) {
				poseStack.pushPose();
				double topOffset = 90 - j * divisions;
				poseStack.translate(0, -topOffset, 0);
				poseStack.mulPose(Vector3f.ZP.rotationDegrees(-(i) * 36));
				var aspect = skill.getSkillChain().get(j);
				var registryName = aspect.getType().getRegistryName();
				if (registryName == null) continue;
				RenderSystem.setShaderTexture(0, new ResourceLocation(registryName.getNamespace(), "textures/skills/" + registryName.getPath() + ".png"));
				GuiComponent.blit(poseStack, -16, -16, 32, 32, 0, 0, 32, 32, 32, 32);
				poseStack.popPose();
			}
			poseStack.pushPose();
			poseStack.translate(0, -46, 0);
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(-(i) * 36));
			var numberString = String.format("%d", i + 1);
			var stringWidth = font.width(numberString);
			font.drawShadow(poseStack, numberString, -stringWidth / 2f, -font.lineHeight / 2f, 0xA0FFAA00, true);
			poseStack.popPose();
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(36));
		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	private void sendSelectedAnother() {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		WuxiaPacketHandler.INSTANCE.sendToServer(new CultivationStateChangeMessage(this.selectedSkill, cultivation.getSkills().casting, cultivation.isDivineSense()));
		cultivation.getSkills().selectedSkill = this.selectedSkill;
	}
}
