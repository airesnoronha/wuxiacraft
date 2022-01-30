package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
public class WuxiaTechniqueComposeGrid extends AbstractWidget {

	public final TechniqueGrid grid;

	private int gridRadius;

	private final LinkedList<Point> hexagonCoordinates;

	public WuxiaTechniqueComposeGrid(int x, int y, TechniqueGrid grid) {
		super(x, y, 0, 0, new TextComponent(""));
		this.grid = grid;
		this.hexagonCoordinates = new LinkedList<>();
		this.setGridRadius(3);
	}

	public void setGridRadius(int gridRadius) {
		this.gridRadius = gridRadius;
		this.hexagonCoordinates.clear();
		//get all hexagon coordinates from this radius
		for (int r = -this.gridRadius; r <= this.gridRadius; r++) {
			for (int q = Math.max(-this.gridRadius, -this.gridRadius - r); q <= Math.min(this.gridRadius, this.gridRadius - r); q++) {
				this.hexagonCoordinates.add(new Point(q, r));
			}
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		var mc = Minecraft.getInstance();
		var player = mc.player;
		if (player == null) return;

		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
		poseStack.pushPose();
		poseStack.translate(this.x, this.y, 0);
		for (var hexC : hexagonCoordinates) {
			var cartC = transformHexagonalToCartesian(hexC);
			poseStack.pushPose();
			poseStack.translate(cartC.x, cartC.y, 0);
			GuiComponent.blit(poseStack,
					-5, -5,
					11, 11,
					18, 25,
					11, 11,
					256, 256
			);
			poseStack.popPose();
		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	/**
	 * transforms a hexagon coordinate into a cartesian coordinate multiplied by distance between nodes
	 *
	 * @param hexagonal the hexagonal axial coordinate
	 * @return the cartesian coordinate with a distance between nodes
	 */
	public static Point transformHexagonalToCartesian(Point hexagonal) {
		int distance = 64;
		int verticalDistance = (int) (distance * Math.sin(60d * Math.PI / 180d));
		int newX = hexagonal.y * distance / 2 + hexagonal.x * distance;
		int newY = hexagonal.y * verticalDistance;
		return new Point(newX, newY);
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {

	}
}
