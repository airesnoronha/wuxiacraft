package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueModifier;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.lazydragonstudios.wuxiacraft.init.WuxiaTechniqueAspects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
public class WuxiaTechniqueComposeGrid extends AbstractWidget {

	private final TechniqueGrid grid;

	private int gridRadius;

	private final LinkedList<Point> hexagonCoordinates;

	private final HashMap<Point, Point> hexagonToCartesianQuickAssess;

	private final LinkedList<ColoredLineSegment> connectionLines;

	public MouseInputPredicate onClicked;

	public MouseInputPredicate onDrag;

	public MouseInputPredicate onRelease;

	public WuxiaTechniqueComposeGrid(int x, int y, TechniqueGrid grid) {
		super(x, y, 0, 0, new TextComponent(""));
		this.grid = grid;
		this.hexagonCoordinates = new LinkedList<>();
		this.hexagonToCartesianQuickAssess = new HashMap<>();
		this.connectionLines = new LinkedList<>();
		this.setGridRadius(1);
		onClicked = (mx, my, mb) -> false;
		onDrag = (mx, my, mb) -> false;
		onRelease = (mx, my, mb) -> false;
		recalculateLines();
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

	public TechniqueGrid getGrid() {
		return this.grid.copy();
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		var mc = Minecraft.getInstance();
		var player = mc.player;
		if (player == null) return;
		final ResourceLocation emptyId = WuxiaTechniqueAspects.EMPTY.getId();

		RenderSystem.enableBlend();
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
			int guiScale = (int) (mc.getWindow().getGuiScale());
			RenderSystem.lineWidth(guiScale * 2.5f);
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
			RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
			GuiComponent.blit(poseStack,
					-19, -19,
					38, 38,
					texX1, 25,
					38, 38,
					256, 256
			);
			var aspectInPlace = this.grid.getAspectAtGrid(hexC);
			if (aspectInPlace != emptyId) {
				var techAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(aspectInPlace);
				if (techAspect != null) {
					RenderSystem.setShaderTexture(0, techAspect.getTextureLocation());
					GuiComponent.blit(poseStack,
							-16, -16,
							32, 32,
							0, 0,
							32, 32,
							32, 32
					);
				}
			}
			poseStack.popPose();
		}
		if (!this.connectionLines.isEmpty()) {
			var tesselator = Tesselator.getInstance();
			var buffer = tesselator.getBuilder();
			RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
			RenderSystem.disableCull();
			RenderSystem.disableTexture();
			int guiScale = (int) (mc.getWindow().getGuiScale());
			RenderSystem.lineWidth(guiScale * 2.5f);
			buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
			for (var line : this.connectionLines) {
				Point start = hexagonToCartesianQuickAssess.get(line.start);
				Point end = hexagonToCartesianQuickAssess.get(line.finish);
				int color = line.color;
				buffer.vertex(poseStack.last().pose(), start.x, start.y, 0).color(color).normal(1f, 0f, 0f).endVertex();
				buffer.vertex(poseStack.last().pose(), end.x, end.y, 0).color(color).normal(1f, 0f, 0f).endVertex();
			}
			tesselator.end();
			RenderSystem.enableCull();
			RenderSystem.enableTexture();
			RenderSystem.lineWidth(1f);
		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	public void addAspectToGrid(Point p, ResourceLocation aspectLocation, BigDecimal proficiency) {
		if (!this.hexagonToCartesianQuickAssess.containsKey(p)) return;
		this.grid.addGridNode(p, aspectLocation, proficiency);
		this.recalculateLines();
	}

	public void removeAspectToGrid(Point p) {
		if (!this.hexagonToCartesianQuickAssess.containsKey(p)) return;
		this.grid.removeGridNode(p);
		this.recalculateLines();
	}

	private void recalculateLines() {
		this.connectionLines.clear();
		if (grid.getStartNodePoint() == null) return;
		TechniqueGrid.traverseGridFromStart(this.grid, (node) -> {
				},
				(node, neighbour) -> {
					this.connectionLines.add(new ColoredLineSegment(node, neighbour, 0xAC3090FF));
				},
				(junk, neighbour) -> {
					this.connectionLines.add(new ColoredLineSegment(junk, neighbour, 0xACFF6040));
				}, (disconnected) -> {

				}
		);
	}

	@Nullable
	public Point getHexCoordinateFromMousePosition(int mouseX, int mouseY) {
		int translatedMouseX = mouseX - this.x - this.width / 2;
		int translatedMouseY = mouseY - this.y - this.height / 2;

		var hexC = getHexPointFromCartesian(new Point(translatedMouseX, translatedMouseY));
		if (this.hexagonToCartesianQuickAssess.containsKey(hexC)) {
			var cartC = hexagonToCartesianQuickAssess.get(hexC);
			if (distance(cartC, new Point(translatedMouseX, translatedMouseY)) <= 17) {
				return hexC;
			}
		}
		return null;
	}

	public TechniqueModifier gridCompile() {
		return this.grid.compile();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.onClicked.apply(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return this.onRelease.apply(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDeltaX, double mouseDeltaY) {
		return this.onDrag.apply(mouseX, mouseY, button);
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

	public record ColoredLineSegment(Point start, Point finish, int color) {

	}
}
