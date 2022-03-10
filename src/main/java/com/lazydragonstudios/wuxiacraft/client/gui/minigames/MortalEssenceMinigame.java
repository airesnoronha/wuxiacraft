package com.lazydragonstudios.wuxiacraft.client.gui.minigames;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.gui.MeditateScreen;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

public class MortalEssenceMinigame implements Minigame {

	private static final ResourceLocation MINIGAME_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigames/mortal_essence_minigame.png");

	private final LinkedList<Strand> strands = new LinkedList<>();

	private Strand selectedStrand = null;

	@Override
	public void init(MeditateScreen screen) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var essenceData = cultivation.getSystemData(System.ESSENCE);
		if (!essenceData.techniqueData.modifier.isValidTechnique()) return;
		//gonna create a ball for each energy unit
		var energy = essenceData.getStat(PlayerSystemStat.ENERGY).intValue();
		this.keepCorrectStrandCount(energy);
	}

	@Override
	public boolean onMouseClick(double x, double y, int button) {
		for (var strand : this.strands) {
			if (strand.inBounds(x, y)) {
				selectedStrand = strand;
				break;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseRelease(double x, double y, int button) {
		selectedStrand = null;
		return false;
	}

	@Override
	public void onMouseMove(double x, double y) {
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
						.divide(essenceData.getStat(PlayerSystemStat.MAX_CULTIVATION_BASE), RoundingMode.HALF_UP)).intValue();
		GuiComponent.blit(poseStack, imageX, imageY + 60 - barFill, 60, barFill, 0, 60 + 60 - barFill, 60, barFill, 256, 256); //the person cross-legged fill

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
		var energy = essenceData.getStat(PlayerSystemStat.ENERGY).intValue();
		this.keepCorrectStrandCount(energy);
		var markedToRemove = new LinkedList<Strand>();
		for (var strand : this.strands) {
			strand.tick();
			strand.setGrabbed(strand == selectedStrand);
			if (strand.isComplete()) {
				selectedStrand = null;
				markedToRemove.add(strand);
			}
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
		}
	}

	private static class Strand {
		private static final int MIN_X = 7;
		private static final int MAX_X = 192;
		private static final int MIN_Y = 24;
		private static final int MAX_Y = 162;
		private static final int MAX_GRABBED_TICKS = 25;
		private double x;
		private double y;
		private double movX;
		private double movY;
		private boolean grabbed;
		private int grabbedTicks;

		Strand() {
			this.x = MIN_X + Math.random() * (MAX_X - MIN_X);
			this.y = MIN_Y + Math.random() * (MAX_Y - MIN_Y);
			this.movX = -0.5 + Math.random();
			this.movY = -0.5 + Math.random();
		}

		void tick() {
			if (this.grabbed) {
				this.grabbedTicks = Math.min(MAX_GRABBED_TICKS, this.grabbedTicks + 1);
				return;
			}
			this.grabbedTicks = 0;
			this.x += movX;
			this.y += movY;
			if (this.x <= MIN_X || this.x >= MAX_X) {
				movX *= -1;
			}
			if (this.y <= MIN_Y || this.y >= MAX_Y) {
				movY *= -1;
			}
		}

		boolean inBounds(double mouseX, double mouseY) {
			return MathUtil.inBounds(mouseX, mouseY, this.x, this.y, 8, 8);
		}

		void render(PoseStack stack) {
			stack.pushPose();
			if (this.grabbedTicks > 0) {
				var scale = (float) (MAX_GRABBED_TICKS - this.grabbedTicks) / (float) MAX_GRABBED_TICKS;
				stack.scale(scale, scale, 0);
			}
			GuiComponent.blit(stack, (int) this.x - 4, (int) this.y - 4, 4, 4, 60, 0, 8, 8, 256, 256);
			stack.popPose();
		}

		public void setGrabbed(boolean grabbed) {
			this.grabbed = grabbed;
		}

		public boolean isComplete() {
			return this.grabbedTicks >= MAX_GRABBED_TICKS;
		}
	}

}
