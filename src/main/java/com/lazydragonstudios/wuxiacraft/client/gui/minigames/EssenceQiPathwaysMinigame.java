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

public class EssenceQiPathwaysMinigame implements Minigame {

	private static final ResourceLocation MINIGAME_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/minigames/qi_pathways.png");

	private static final Point[][] pathways = new Point[][]{
			new Point[]{
					new Point(3, 22),
					new Point(8, 23),
					new Point(22, 25),
					new Point(34, 26),
					new Point(46, 25),
					new Point(57, 25),
					new Point(62, 27),
					new Point(66, 30),
					new Point(67, 40),
					new Point(67, 48),
					new Point(70, 55),
					new Point(73, 61)
			},
			new Point[]{
					new Point(144, 22),
					new Point(139, 23),
					new Point(125, 25),
					new Point(113, 26),
					new Point(101, 25),
					new Point(90, 25),
					new Point(85, 27),
					new Point(81, 30),
					new Point(80, 40),
					new Point(80, 48),
					new Point(77, 55),
					new Point(74, 61)
			},
			new Point[]{
					new Point(33, 130),
					new Point(37, 127),
					new Point(41, 122),
					new Point(47, 110),
					new Point(52, 100),
					new Point(57, 90),
					new Point(60, 80),
					new Point(63, 75),
					new Point(66, 71),
					new Point(68, 68),
					new Point(70, 65),
					new Point(73, 61)
			},
			new Point[]{
					new Point(114, 130),
					new Point(110, 127),
					new Point(106, 122),
					new Point(100, 110),
					new Point(95, 100),
					new Point(90, 90),
					new Point(87, 80),
					new Point(84, 75),
					new Point(81, 71),
					new Point(79, 68),
					new Point(77, 65),
					new Point(74, 61)
			}
	};
	private static final int imageX = 19;
	private static final int imageY = 26;

	private final LinkedList<Strand> strands = new LinkedList<>();

	private Strand selectedStrand = null;

	//the size is actually the tex coordinates
	private final Rectangle dantian = new Rectangle(73, 61, 148, 5);

	int chosenPathway = -1;
	int currentPoint = 0;
	boolean isGrabbed = false;

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
		GuiComponent.blit(poseStack, imageX, imageY, 148, 135, 0, 0, 148, 135, 512, 512); //the white body
		//fill = 60 * cult_base/max_cult_base
		int barFill = BigDecimal.valueOf(135).multiply(
				essenceData.getStat(PlayerSystemStat.CULTIVATION_BASE)
						.divide(essenceData.getStat(PlayerSystemStat.MAX_CULTIVATION_BASE), RoundingMode.HALF_UP)).intValue();
		GuiComponent.blit(poseStack, imageX, imageY + 135 - barFill, 148, barFill, 148, 135 - barFill, 148, barFill, 512, 512); //the green body fill
		GuiComponent.blit(poseStack, imageX + 2, imageY + 21, 144, 111, 0, 135, 144, 111, 512, 512); //the pathways
		GuiComponent.blit(poseStack, imageX + dantian.x - 4, imageY + dantian.y - 4, 8, 8, dantian.width, dantian.height, 8, 8, 512, 512);
		if (this.selectedStrand != null) {
			if (this.chosenPathway == -1) {
				for (int i = 0; i < 4; i++) {
					Point point = pathways[i][0];
					GuiComponent.blit(poseStack, imageX + point.x - 3, imageY + point.y - 3, 5, 148, 13, 5, 5, 512, 512);
				}
			} else if (this.chosenPathway < 4) {
				if (this.currentPoint < pathways[this.chosenPathway].length) {
					Point point = pathways[this.chosenPathway][this.currentPoint];
					GuiComponent.blit(poseStack, imageX + point.x - 3, imageY + point.y - 3, 5, 148, 13, 5, 5, 512, 512);
				}
			}
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
		var strandCount = essenceData.getStat(PlayerSystemStat.ENERGY).divide(new BigDecimal("4"), RoundingMode.HALF_UP).intValue();
		this.keepCorrectStrandCount(strandCount);
		var markedToRemove = new LinkedList<Strand>();
		for (var strand : this.strands) {
			strand.tick();
			strand.setGrabbed(strand == selectedStrand);
			if (selectedStrand == strand) {
				if (chosenPathway == -1) {
					for (int i = 0; i < 4; i++) {
						Point currentPoint = pathways[i][0];
						if (MathUtil.inBounds(strand.x, strand.y, imageX + currentPoint.x - 3, imageY + currentPoint.y - 3, 5, 5)) {
							chosenPathway = i;
							this.currentPoint = 1;
						}
					}
				} else if (this.chosenPathway < 4) {
					if (this.currentPoint >= pathways[this.chosenPathway].length) return;
					Point currentPoint = pathways[this.chosenPathway][this.currentPoint];
					if (MathUtil.inBounds(strand.x, strand.y, imageX + currentPoint.x - 3, imageY + currentPoint.y - 3, 5, 5)) {
						this.currentPoint++;
					}
					if (this.currentPoint >= pathways[this.chosenPathway].length) {
						selectedStrand = null;
						markedToRemove.add(strand);
						WuxiaPacketHandler.INSTANCE.sendToServer(new MeditateMessage(System.ESSENCE, true));
						essenceData.getStage().cultivate(player);
						this.chosenPathway = -1;
						this.currentPoint = 0;
					}
				}
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
			GuiComponent.blit(stack, -3, -3, 5, 5, 148, 0, 5, 5, 512, 512);
			stack.popPose();
		}

		public void setGrabbed(boolean grabbed) {
			this.grabbed = grabbed;
		}
	}

}
