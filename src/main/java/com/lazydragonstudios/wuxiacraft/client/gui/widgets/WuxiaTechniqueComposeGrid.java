package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
public class WuxiaTechniqueComposeGrid extends AbstractWidget {

	public final TechniqueGrid grid;

	private int gridRadius;

	private final LinkedList<Point> hexagonCoordinates;

	private final HashMap<Point, Point> hexagonToCartesianQuickAssess;

	public WuxiaTechniqueComposeGrid(int x, int y, TechniqueGrid grid) {
		super(x, y, 0, 0, new TextComponent(""));
		this.grid = grid;
		this.hexagonCoordinates = new LinkedList<>();
		this.hexagonToCartesianQuickAssess = new HashMap<>();
		this.setGridRadius(1);
	}

	public void setGridRadius(int gridRadius) {
		this.gridRadius = gridRadius;
		this.hexagonCoordinates.clear();
		this.hexagonToCartesianQuickAssess.clear();
		//get all hexagon coordinates from this radius
		for (int r = -this.gridRadius; r <= this.gridRadius; r++) {
			for (int q = Math.max(-this.gridRadius, -this.gridRadius - r); q <= Math.min(this.gridRadius, this.gridRadius - r); q++) {
				Point p = new Point(q, r);
				this.hexagonCoordinates.add(p);
				this.hexagonToCartesianQuickAssess.put(p, transformHexagonalToCartesian(p));
			}
		}
		int distance = 38;
		this.setWidth((2 * this.gridRadius + 1 + 1) * distance);
		int verticalDistance = (int) (distance * Math.sin(60d * Math.PI / 180d));
		this.setHeight((2 * this.gridRadius + 1) * verticalDistance + distance);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		var mc = Minecraft.getInstance();
		var player = mc.player;
		if (player == null) return;

		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
		poseStack.pushPose();
		poseStack.translate(this.x + this.width / 2f, this.y + this.height / 2f, 0);
		HashSet<Point> visited = new HashSet<>();
		for (var hexC : hexagonCoordinates) {
			visited.add(hexC);
			var cartC = hexagonToCartesianQuickAssess.get(hexC);
			int texX1 = 40;
			int translatedMouseX = mouseX - this.x - this.width / 2;
			int translatedMouseY = mouseY - this.y - this.height / 2;

			if (getHexPointFromCartesian(new Point(translatedMouseX, translatedMouseY)).equals(hexC)) {
				if (distance(cartC, new Point(translatedMouseX, translatedMouseY)) <= 17) {
					texX1 += 38;
				}
			}
			poseStack.pushPose();
			poseStack.translate(cartC.x, cartC.y, 0);
			var tesselator = Tesselator.getInstance();
			var buffer = tesselator.getBuilder();
			RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
			RenderSystem.disableCull();
			RenderSystem.disableTexture();
			int guiScale = (int)(mc.getWindow().getGuiScale());
			RenderSystem.lineWidth(guiScale*2.5f);
			buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
			for (var neighbour : TechniqueGrid.hexagonalNeighbours) {
				Point neighbourC = new Point(hexC.x + neighbour.x, hexC.y + neighbour.y);
				if (hexagonToCartesianQuickAssess.containsKey(neighbourC) && !visited.contains(neighbourC)) {
					Point p = hexagonToCartesianQuickAssess.get(neighbour);
					buffer.vertex(poseStack.last().pose(), 0, 0, 0).color(0x7c303030).normal(1f, 0f, 0f).endVertex();
					buffer.vertex(poseStack.last().pose(), p.x, p.y, 0).color(0x7c303030).normal(1f, 0f, 0f).endVertex();
				}
			}
			tesselator.end();
			RenderSystem.enableCull();
			RenderSystem.enableTexture();
			GuiComponent.blit(poseStack,
					-19, -19,
					38, 38,
					texX1, 25,
					38, 38,
					256, 256
			);
			poseStack.popPose();
		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int transformedX = (int) (mouseX - this.x - this.width / 2);
		int transformedY = (int) (mouseY - this.y - this.height / 2);
		Point p = getHexPointFromCartesian(new Point(transformedX, transformedY));
		System.out.printf("MouseX: %d  MouseY: %d\n", transformedX, transformedY);
		System.out.printf("PointX: %d  PointY: %d\n", p.x, p.y);
		return false;
	}

	@Override
	public boolean mouseReleased(double p_93684_, double p_93685_, int p_93686_) {
		return false;
	}

	@Override
	public boolean mouseDragged(double p_93645_, double p_93646_, int p_93647_, double p_93648_, double p_93649_) {
		return false;
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {

	}

	/**
	 * transforms a hexagon coordinate into a cartesian coordinate multiplied by distance between nodes
	 *
	 * @param hexagonal the hexagonal axial coordinate
	 * @return the cartesian coordinate with a distance between nodes
	 */
	public static Point transformHexagonalToCartesian(Point hexagonal) {
		int distance = 38;
		int verticalDistance = (int) (distance * Math.sin(60d * Math.PI / 180d));
		int newX = hexagonal.y * distance / 2 + hexagonal.x * distance;
		int newY = hexagonal.y * verticalDistance;
		return new Point(newX, newY);
	}

	/**
	 * this gets the hexagonal coordinate from a cartesian coordinates
	 *
	 * @param cart cartesian coordinates to be transformed
	 * @return hexagonal coordinates retrieved
	 */
	public static Point getHexPointFromCartesian(Point cart) {
		int distance = 38;
		int verticalDistance = (int) (distance * Math.sin(60d * Math.PI / 180d));
		int r = (Math.abs(cart.y) + verticalDistance / 2) / verticalDistance;
		r = cart.y < 0 ? r * -1 : r;
		int xOffset = r * distance / 2;
		int offset = cart.x - xOffset;
		int testX = (Math.abs(offset) + distance / 2);
		testX = offset < 0 ? testX * -1 : testX;
		int q = testX / distance;
		return new Point(q, r);
	}

	public static double distance(Point a, Point b) {
		int dx = b.x - a.x;
		int dy = b.y - a.y;
		return Math.sqrt(dx * dx + dy * dy);
	}
}
