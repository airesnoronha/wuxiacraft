package com.lazydragonstudios.wuxiacraft.client.gui;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.gui.minigames.Minigame;
import com.lazydragonstudios.wuxiacraft.client.gui.minigames.MortalEssenceMinigame;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRealms;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.HashMap;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MeditateScreen extends Screen {

	public static final ResourceLocation MEDITATE_SCREEN = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/cultivation_minigame_screen.png");

	public MeditateScreen() {
		super(new TextComponent(""));
	}

	private Minigame minigame = null;

	private System system = System.ESSENCE;

	private static HashMap<ResourceLocation, Supplier<Minigame>> stageMiniGames = new HashMap<>();

	static {
		stageMiniGames.put(WuxiaRealms.ESSENCE_MORTAL_STAGE.getId(), MortalEssenceMinigame::new);
	}

	private int guiTop = 0;
	private int guiLeft = 0;

	@Override
	protected void init() {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		for (var system : System.values()) {
			var systemData = cultivation.getSystemData(system);
			if (!systemData.techniqueData.modifier.isValidTechnique()) continue;
			this.system = system;
			var supplier = stageMiniGames.get(systemData.currentStage);
			if (supplier == null) continue;
			this.minigame = supplier.get();
			break;
		}
		var scaledResX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		var scaledResY = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		this.guiTop = (scaledResY - 170) / 2;
		this.guiLeft = (scaledResX - 200) / 2;
		if (this.minigame == null) return;
		this.minigame.init(this);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.render(poseStack, mouseX, mouseY, partialTicks); // renderable widgets, keep that here
		renderBackground(poseStack);
		poseStack.pushPose();
		poseStack.translate(this.guiLeft, this.guiTop, 0);
		RenderSystem.setShaderTexture(0, MEDITATE_SCREEN);
		blit(poseStack, 0, 0, 0, 0, 200, 170);
		for (var system : System.values()) {
			int texX = 0;
			if (system == this.system) texX = 63;
			blit(poseStack, 5 + 63 * system.ordinal(), 6, texX, 170, 63, 14);
		}
		this.renderLabels(poseStack, mouseX, mouseY, partialTicks);
		if (this.minigame == null) return;
		minigame.render(poseStack, mouseX - this.guiLeft, mouseY - this.guiTop, partialTicks);
		poseStack.popPose();
	}

	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		for (var system : System.values()) {
			var message = new TranslatableComponent("wuxiacraft.system." + system.name().toLowerCase());
			int messageWidth = this.font.width(message);
			this.font.drawShadow(poseStack, message, 5 + 63 * system.ordinal() + (63 - messageWidth) / 2f, 9, 0xFFAA00);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		var superResult = super.mouseClicked(mouseX, mouseY, button);
		for (var system : System.values()) {
			if (MathUtil.inBounds(mouseX - this.guiLeft, mouseY - this.guiTop, 5 + 63 * system.ordinal(), 6, 63, 18)) {
				var player = Minecraft.getInstance().player;
				if (player == null) break;
				var cultivation = Cultivation.get(player);
				var systemData = cultivation.getSystemData(system);
				if (!systemData.techniqueData.modifier.isValidTechnique()) break;
				var minigame = stageMiniGames.get(systemData.currentStage);
				if (minigame == null) break;
				this.system = system;
				this.minigame = minigame.get();
				break;
			}
		}
		if (this.minigame == null) return superResult;
		return superResult || minigame.onMouseClick(mouseX-this.guiLeft, mouseY-this.guiTop, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		var superResult = super.mouseReleased(mouseX, mouseY, button);
		if (this.minigame == null) return superResult;
		return superResult || minigame.onMouseRelease(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		super.mouseMoved(mouseX, mouseY);
		if (this.minigame == null) return;
		minigame.onMouseMove(mouseX, mouseY);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.minigame == null) return;
		this.minigame.tick();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
