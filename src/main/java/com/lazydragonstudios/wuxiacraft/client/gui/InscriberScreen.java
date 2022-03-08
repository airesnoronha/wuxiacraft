package com.lazydragonstudios.wuxiacraft.client.gui;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.container.InscriberMenu;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.HashMap;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InscriberScreen extends AbstractContainerScreen<InscriberMenu> {

	public static final ResourceLocation INSCRIBER_SCREEN = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/inscription_table.png");

	private System selectedSystem = System.ESSENCE;

	private static final HashMap<System, Rectangle> buttons = new HashMap<>();

	static {//x, y, texX, texY
		buttons.put(System.BODY, new Rectangle(19, 27, 36, 134));
		buttons.put(System.DIVINE, new Rectangle(39, 27, 52, 134));
		buttons.put(System.ESSENCE, new Rectangle(59, 27, 68, 134));
	}

	private static final Rectangle writeBtn = new Rectangle(103, 23, 18, 18);

	public InscriberScreen(InscriberMenu inscriberMenu, Inventory inventory, Component title) {
		super(inscriberMenu, inventory, title);
		this.width = 201;
		this.height = 134;
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		this.renderBackground(poseStack);
		poseStack.pushPose();
		poseStack.translate(this.leftPos, this.topPos, 0);
		RenderSystem.setShaderTexture(0, INSCRIBER_SCREEN);
		blit(poseStack, 0, 0, 0, 0, 201, 134);
		for (var system : System.values()) {
			Rectangle button = buttons.get(system);
			if (selectedSystem == system) {
				blit(poseStack, button.x, button.y, 18, 134, 18, 18);
			}
			if (MathUtil.inBounds(mouseX - this.leftPos, mouseY - this.topPos, button.x, button.y, 18, 18)) {
				blit(poseStack, button.x, button.y, 0, 134, 18, 18);
			}
			blit(poseStack, button.x + 1, button.y + 1, button.width, button.height, 16, 16);
		}
		if (MathUtil.inBounds(mouseX - this.leftPos, mouseY - this.topPos, writeBtn.x, writeBtn.y, 18, 18)) {
			blit(poseStack, writeBtn.x, writeBtn.y, 0, 134, 18, 18);
		}
		poseStack.popPose();
	}

	@Override
	protected void renderLabels(PoseStack p_97808_, int p_97809_, int p_97810_) {
	}
}
