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
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

public class EssenceQiGatheringMinigame implements Minigame {

	private static final ResourceLocation MINIGAME_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigames/mortal_essence_minigame.png");

	private final LinkedList<Strand> strands = new LinkedList<>();

	private Strand selectedStrand = null;

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
		return false;
	}

	@Override
	public boolean onMouseRelease(double x, double y, int button) {
		if (this.selectedStrand != null) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new MeditateMessage(System.ESSENCE, false));
			var player = Minecraft.getInstance().player;
			if (player == null) return false;
			var cultivation = Cultivation.get(player);
			var essenceData = cultivation.getSystemData(System.ESSENCE);
			essenceData.getStage().cultivationFailure(player);
			this.strands.remove(this.selectedStrand);
			this.selectedStrand = null;
		}
		return false;
	}

	@Override
	public void onMouseMove(double x, double y) {
		if (this.selectedStrand != null) {
			this.selectedStrand.x = x;
			this.selectedStrand.y = y;
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
						.divide(essenceData.getStat(PlayerSystemStat.MAX_CULTIVATION_BASE), RoundingMode.HALF_UP)).intValue();
		GuiComponent.blit(poseStack, imageX, imageY + 60 - barFill, 60, barFill, 0, 60 + 60 - barFill, 60, barFill, 256, 256); //the person cross-legged fill
		GuiComponent.blit(poseStack, dantian.x, dantian.y, 8, 8, dantian.width, dantian.height, 8, 8, 256, 256);
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
		var strandCount = essenceData.getStat(PlayerSystemStat.ENERGY).divide(new BigDecimal("2.5"), RoundingMode.HALF_UP).intValue();
		this.keepCorrectStrandCount(strandCount);
		var markedToRemove = new LinkedList<Strand>();
		for (var strand : this.strands) {
			strand.tick();
			strand.setGrabbed(strand == selectedStrand);
			if (MathUtil.inBounds(strand.x, strand.y, dantian.x, dantian.y, 8, 8)) {
				selectedStrand = null;
				markedToRemove.add(strand);
				WuxiaPacketHandler.INSTANCE.sendToServer(new MeditateMessage(System.ESSENCE, true));
				essenceData.getStage().cultivate(player);
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
		} else if (this.strands.size() > count) {
			for (int overflow = this.strands.size(); overflow > count; overflow--) {
				this.strands.remove(this.strands.size() - 1);
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

		Strand() {
			this.x = MIN_X + Math.random() * (MAX_X - MIN_X);
			this.y = MIN_Y + Math.random() * (MAX_Y - MIN_Y);
			this.movX = -0.5 + Math.random();
			this.movY = -0.5 + Math.random();
		}

		void tick() {
			if (this.grabbed) return;
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
