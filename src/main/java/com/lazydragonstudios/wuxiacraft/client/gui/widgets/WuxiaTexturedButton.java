package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class WuxiaTexturedButton extends WuxiaButton {

	private ResourceLocation[] textureLocation;

	/**
	 * x, y, -> uv in texture
	 * width, height -> max u, max v
	 */
	private Rectangle[] tex;

	public WuxiaTexturedButton(int x, int y, int width, int height, Component title, Runnable onClicked, ResourceLocation[] textureLocation, Rectangle[] tex) {
		super(x, y, width, height, title, onClicked);
		this.textureLocation = textureLocation;
		this.tex = tex;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (textureLocation.length != tex.length) return;
		for (int i = 0; i < textureLocation.length; i++) {
			var texLocation = textureLocation[i];
			var tex = this.tex[i];
			RenderSystem.setShaderTexture(0, texLocation);
			GuiComponent.blit(poseStack, this.x, this.y, this.width, this.height, tex.x, tex.y, this.width, this.height, tex.width, tex.height);
		}
	}
}
