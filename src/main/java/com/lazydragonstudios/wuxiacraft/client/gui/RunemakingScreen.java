package com.lazydragonstudios.wuxiacraft.client.gui;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaFlowPanel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaTexturedButton;
import com.lazydragonstudios.wuxiacraft.container.RunemakingMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.LinkedList;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RunemakingScreen extends AbstractContainerScreen<RunemakingMenu> {

	private static final ResourceLocation RUNEMAKING_SCREEN = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/runemaking_table.png");

	public static LinkedList<WuxiaTexturedButton> runeBtn = new LinkedList<>();

	public RunemakingScreen(RunemakingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		this.width = 200;
		this.height = 172;
	}

	@Override
	protected void init() {
		super.init();
		WuxiaFlowPanel panel = new WuxiaFlowPanel(this.getGuiLeft() + 10, this.getGuiTop()+ 10, 158, 73, new TextComponent(""));
		panel.margin = 2;
		this.addRenderableWidget(panel);
		if (this.minecraft == null) return;
		if (this.minecraft.gameMode == null) return;
		for (int i = 0; i < 8; i++) {
			int finalI = i;
			WuxiaTexturedButton rune = new WuxiaTexturedButton(0, 0, 18, 18,
					new TextComponent(""),
					() -> this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, finalI),
					new ResourceLocation[]{RUNEMAKING_SCREEN, RUNEMAKING_SCREEN},
					new Rectangle[]{new Rectangle(200, 0, 256, 256), new Rectangle(i * 18, 172, 256, 256)}
			);
			runeBtn.add(rune);
			panel.addChild(rune);
		}
	}

	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		this.renderBackground(poseStack);
		poseStack.pushPose();
		poseStack.translate(this.leftPos, this.topPos, 0);
		RenderSystem.setShaderTexture(0, RUNEMAKING_SCREEN);
		blit(poseStack, 0,0, 0, 0, 200, 172);
		poseStack.popPose();
	}

}
