package com.lazydragonstudios.wuxiacraft.client.gui.minigames;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.gui.MeditateScreen;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.networking.MeditateMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

public class EssenceQiCondensationMinigame implements Minigame {

	private static final ResourceLocation MINIGAME_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigames/mortal_essence_minigame.png");
	private static final float defaultOuterCircleRadius = 60f;
	private static final float innerCircleRadius = 15;

	private final LinkedList<Strand> strands = new LinkedList<>();

	private Strand selectedStrand = null;


	private float outerCircleRadius = defaultOuterCircleRadius;

	private float hoveringCircleRadius = defaultOuterCircleRadius;

	private boolean grabbedCircle = false;


	//the size is actually the tex coordinates
	private final Rectangle dantian = new Rectangle(96, 104, 60, 5);

	@Override
	public void init(MeditateScreen screen) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var essenceData = cultivation.getSystemData(System.ESSENCE);
		if (!essenceData.techniqueData.modifier.isValidTechnique()) return;
	}

	@Override
	public boolean onMouseClick(double x, double y, int button) {
		for (var strand : this.strands) {
			if (strand.inBounds(x, y)) {
				selectedStrand =
						strand;
				break;
			}
		}
		if (this.selectedStrand == null && isInCircleBorder(x, y, dantian.x + 4, dantian.y + 4, outerCircleRadius, 3f)) {
			this.grabbedCircle = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(double x, double y, int button) {
		if (this.selectedStrand != null) {
			this.selectedStrand = null;
		}
		if (this.grabbedCircle) {
			this.grabbedCircle = false;
			this.outerCircleRadius = this.hoveringCircleRadius;
		}
		return false;
	}

	@Override
	public void onMouseMove(double x, double y) {
		if (this.selectedStrand != null) {
			this.selectedStrand.x = x;
			this.selectedStrand.y = y;
		}
		else if (this.grabbedCircle) {
			var dx = x - dantian.x;
			var dy = y - dantian.y;
			hoveringCircleRadius = (float) Math.min(outerCircleRadius, Math.sqrt(dx * dx + dy * dy));
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var essenceData = cultivation.getSystemData(System.ESSENCE);
		if (!essenceData.techniqueData.modifier.isValidTechnique()) return;
		RenderSystem.setShaderTexture(0, MINIGAME_TEXTURE);
		int imageX = 70;
		int imageY = 66;
		GuiComponent.blit(poseStack, imageX, imageY, 60, 60, 0, 0, 60, 60, 256, 256); //the person cross-legged
		//fill = 60 * cult_base/max_cult_base
		int barFill = BigDecimal.valueOf(60).multiply(
						essenceData.getStat(PlayerSystemStat.CULTIVATION_BASE)
								.divide(essenceData.getStat(PlayerSystemStat.MAX_CULTIVATION_BASE), RoundingMode.HALF_UP))
				.min(BigDecimal.valueOf(60)).intValue();
		GuiComponent.blit(poseStack, imageX, imageY + 60 - barFill, 60, barFill, 0, 60 + 60 - barFill, 60, barFill, 256, 256); //the person cross-legged fill
		GuiComponent.blit(poseStack, dantian.x, dantian.y, 8, 8, dantian.width, dantian.height, 8, 8, 256, 256);
		drawCircle(poseStack, dantian.x + 4, dantian.y + 4, outerCircleRadius, 0xFFEEEE00);
		drawCircle(poseStack, dantian.x + 4, dantian.y + 4, innerCircleRadius, 0xFFAA1010);
		if (grabbedCircle) {
			drawCircle(poseStack, dantian.x + 4, dantian.y + 4, hoveringCircleRadius, 0xFF10FA10);
		}
		for (var strand : this.strands) {
			strand.render(poseStack);
		}
	}

	@Override
	public void renderTooltips(PoseStack stack, int mouseX, int mouseY) {

	}

	@Override
	public void tick() {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var essenceData = cultivation.getSystemData(System.ESSENCE);
		int energy = essenceData.getStat(PlayerSystemStat.ENERGY).intValue();
		var strandCount = energy > 4 ? 5 : 0;
		this.keepCorrectStrandCount(strandCount);
		var markedToRemove = new LinkedList<Strand>();
		for (var strand : this.strands) {
			strand.setGrabbed(strand == selectedStrand);
			strand.tick(this.outerCircleRadius);
			if (this.grabbedCircle) {
				if (isOutsideCircle(strand.x, strand.y, dantian.x + 4, dantian.y + 4, hoveringCircleRadius)) {
					WuxiaPacketHandler.INSTANCE.sendToServer(new MeditateMessage(System.ESSENCE, false));
					essenceData.getStage().cultivationFailure(player);
					markedToRemove.addAll(this.strands);
					this.outerCircleRadius = defaultOuterCircleRadius;
					this.hoveringCircleRadius = defaultOuterCircleRadius;
					this.grabbedCircle = false;
					break;
				}
			}
		}
		if (this.outerCircleRadius < innerCircleRadius) {
			this.outerCircleRadius = defaultOuterCircleRadius;
			WuxiaPacketHandler.INSTANCE.sendToServer(new MeditateMessage(System.ESSENCE, true));
			essenceData.getStage().cultivate(player);
			markedToRemove.addAll(this.strands);
		}
		for (var toRemove : markedToRemove) {
			this.strands.remove(toRemove);
		}
	}

	public void keepCorrectStrandCount(int count) {
		if (this.strands.size() < count) {
			for (int remaining = this.strands.size(); remaining < count; remaining++) {
				this.strands.add(new Strand());
			}
		} else if (this.strands.size() > count) {
			for (int overflow = this.strands.size(); overflow > count; overflow--) {
				this.strands.remove(this.strands.size() - 1);
			}
		}
	}

	private static void drawCircle(PoseStack poseStack, float x, float y, float radius, int color) {
		var prevShader = RenderSystem.getShader();
		var tesselator = Tesselator.getInstance();
		var buffer = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
		RenderSystem.disableCull();
		RenderSystem.disableTexture();
		int guiScale = (int) (Minecraft.getInstance().getWindow().getGuiScale());
		RenderSystem.lineWidth(guiScale * 3f);
		buffer.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_COLOR_NORMAL);
		int circleSections = 64;
		for (int i = 0; i <= circleSections; i++) {
			var angle = 2 * Math.PI * i / circleSections;
			buffer.vertex(poseStack.last().pose(),
							x + radius * (float) Math.cos(angle),
							y + radius * (float) Math.sin(angle),
							0f)
					.color(color)
					.normal((float) Math.cos(angle-Math.PI/2), (float) Math.sin(angle-Math.PI/2), 0f)
					.endVertex();
		}
		tesselator.end();
		RenderSystem.enableCull();
		RenderSystem.enableTexture();
		RenderSystem.setShader(() -> prevShader);
	}

	private static boolean isInCircleBorder(double x, double y, double circleX, double circleY, double circleRadius, double circleBorder) {
		var dx = x - circleX;
		var dy = y - circleY;
		var dRadius = Math.sqrt(dx * dx + dy * dy);
		return dRadius >= circleRadius - circleBorder / 2 && dRadius <= circleRadius + circleBorder / 2;
	}

	private static boolean isOutsideCircle(double x, double y, double circleX, double circleY, double circleRadius) {
		var dx = x - circleX;
		var dy = y - circleY;
		var dRadius = Math.sqrt(dx * dx + dy * dy);
		return dRadius > circleRadius;
	}

	private static class Strand {
		private static final int CENTER_X = 100;
		private static final int CENTER_Y = 108;
		private static final int MAX_RADIUS = 50;
		private double x;
		private double y;
		private double movX;
		private double movY;
		private boolean grabbed;

		Strand() {
			var v2 = new Vec2((float) Math.random() * 2f - 1f, (float) Math.random() * 2f - 1f);
			v2 = v2.normalized();
			v2 = v2.scale(MAX_RADIUS);
			this.x = CENTER_X + v2.x;
			this.y = CENTER_Y + v2.y;
			this.movX = (-0.5 + Math.random())*0.1;
			this.movY = (-0.5 + Math.random())*0.1;
		}

		void tick(double outerCircleRadius) {
			if (this.grabbed) return;
			this.x += movX;
			this.y += movY;
			if (EssenceQiCondensationMinigame.isOutsideCircle(this.x, this.y, CENTER_X, CENTER_Y, outerCircleRadius)) {
				this.movX *= -1;
				this.movY *= -1;
			}
		}

		boolean inBounds(double mouseX, double mouseY) {
			return MathUtil.inBounds(mouseX, mouseY, this.x - 3, this.y - 3, 5, 5);
		}

		void render(PoseStack stack) {
			stack.pushPose();
			stack.translate(this.x, this.y, 0);
			GuiComponent.blit(stack, -3, -3, 5, 5, 60, 0, 5, 5, 256, 256);
			stack.popPose();
		}

		public void setGrabbed(boolean grabbed) {
			this.grabbed = grabbed;
		}
	}

}
