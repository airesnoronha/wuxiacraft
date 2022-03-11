package com.lazydragonstudios.wuxiacraft.client.gui;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaScrollPanel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaTechniqueComposeGrid;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ManualScreen extends Screen {

	private WuxiaScrollPanel panel;
	private WuxiaTechniqueComposeGrid gridComposer;
	private TechniqueGrid grid;

	private int guiTop = 0;
	private int guiLeft = 0;

	private static final ResourceLocation BOOK_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/book_gui.png");
	private int radius;

	public ManualScreen(TechniqueGrid grid, int radius) {
		super(new TextComponent(""));
		this.grid = grid;
		this.radius = radius;
	}

	@Override
	protected void init() {
		var scaledResX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		var scaledResY = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		this.guiTop = (scaledResY - 200) / 2;
		this.guiLeft = (scaledResX - 200) / 2;
		this.panel = new WuxiaScrollPanel(this.guiLeft, this.guiTop, 210, 210, new TextComponent(""));
		this.gridComposer = new WuxiaTechniqueComposeGrid(0, 0, this.grid);
		this.gridComposer.setGridRadius(this.radius);
		this.gridComposer.setShouldRenderCompiledTooltips(false);
		this.panel.addChild(this.gridComposer);
		this.addRenderableWidget(this.panel);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		poseStack.translate(this.guiLeft, this.guiTop, 0);
		RenderSystem.setShaderTexture(0, BOOK_GUI);
		blit(poseStack, 0, 0, 0, 0, 200, 200);
		poseStack.popPose();
		super.render(poseStack, mouseX, mouseY, partialTick);
		for (var child : this.renderables) {
			if (child instanceof AbstractWidget widget) {
				widget.renderToolTip(poseStack, mouseX, mouseY);
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
