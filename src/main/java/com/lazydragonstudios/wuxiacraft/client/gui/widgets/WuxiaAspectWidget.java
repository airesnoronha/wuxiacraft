package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class WuxiaAspectWidget extends AbstractWidget {

	public ResourceLocation aspect;

	private BiConsumer<Double, Double> onClicked;

	private BiConsumer<Double, Double> onDragged;

	public WuxiaAspectWidget(int x, int y, ResourceLocation aspect) {
		super(x, y, 32, 32, Component.nullToEmpty(aspect.getPath()));
		this.aspect = aspect;
		onClicked = (mx, my) -> {};
		onDragged = (mx, my) -> {};
	}

	@Override
	public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderTexture(0, getAspect().textureLocation);
		RenderSystem.enableBlend();
		GuiComponent.blit(poseStack,
				this.x, this.y,
				32, 32,
				0, 0,
				32, 32,
				32, 32
		);
		RenderSystem.disableBlend();
	}

	public TechniqueAspect getAspect() {
		return WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.aspect);
	}

	@Override
	public void updateNarration(@Nonnull NarrationElementOutput p_169152_) {

	}

	public void setOnClicked(BiConsumer<Double, Double> onClicked) {
		this.onClicked = onClicked;
	}

	public void setOnDragged(BiConsumer<Double, Double> onDragged) {
		this.onDragged = onDragged;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.onClicked.accept(mouseX, mouseY);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double mouseDeltaX, double mouseDeltaY) {
		this.onDragged.accept(mouseX, mouseY);
	}
}
