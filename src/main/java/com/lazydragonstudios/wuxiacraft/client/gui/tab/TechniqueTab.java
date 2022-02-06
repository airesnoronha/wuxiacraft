package com.lazydragonstudios.wuxiacraft.client.gui.tab;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.*;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.lazydragonstudios.wuxiacraft.networking.RequestTechniqueDataChange;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class TechniqueTab extends IntrospectionTab {

	public final System system;
	private HashMap<Object, Object> aspectWidgets;
	private WuxiaFlowPanel aspectsPanel;
	private WuxiaScrollPanel techniqueStatsPanel;
	private WuxiaScrollPanel composerPanel;
	private WuxiaButton compileBtn;
	private WuxiaButton saveBtn;
	private WuxiaTechniqueComposeGrid gridComposer;
	private WuxiaTextField searchField;

	private boolean isCompiled = false;

	private ResourceLocation draggingAspect = null;
	private double[] dragPosition = new double[]{0d, 0d, 0d, 0d};

	public TechniqueTab(String name, Point icon, System system) {
		super(name, icon);
		this.system = system;
	}

	@Override
	public void init(IntrospectionScreen screen) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var renderGrid = cultivation.getSystemData(this.system).techniqueData.grid.copy();
		int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int stretchedSpace = scaledWidth - 36 - 316;
		aspectWidgets = new HashMap<>();

		searchField = new WuxiaTextField(36 + 3, 36, 110, 20, new TextComponent(""));

		aspectsPanel = new WuxiaFlowPanel(36, 36 + 20 + 3, 116, scaledHeight - 16, new TextComponent(""));
		aspectsPanel.margin = 5;
		techniqueStatsPanel = new WuxiaScrollPanel(scaledWidth - 200, 36, 200, scaledHeight - 55 - 36, new TextComponent("")) {
			//a small hack to release the aspect when inside here, turns out release is only propagated on hovering aspect
			@Override
			public boolean mouseReleased(double mouseX, double mouseY, int button) {
				draggingAspect = null;
				return super.mouseReleased(mouseX, mouseY, button);
			}
		};


		composerPanel = new WuxiaScrollPanel(36 + 116, 36, stretchedSpace, scaledHeight - 36, new TextComponent(""));
		composerPanel.setOverflow(WuxiaScrollPanel.OverflowType.HIDDEN);

		compileBtn = new WuxiaButton(scaledWidth - 190, scaledHeight - 50, 180, 20, new TextComponent("Compile"), () -> {
			try {
				var modifiers = this.gridComposer.gridCompile();
				this.isCompiled = true;
			} catch (Exception e) {
				WuxiaCraft.LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		});
		saveBtn = new WuxiaButton(scaledWidth - 190, scaledHeight - 25, 180, 20, new TextComponent("Save"), () -> {
			if (this.isCompiled) {
				TechniqueContainer techniqueData = cultivation.getSystemData(this.system).techniqueData;
				techniqueData.grid.deserialize(gridComposer.getGrid().serialize());
				WuxiaPacketHandler.INSTANCE.sendToServer(new RequestTechniqueDataChange(this.system, techniqueData.serialize()));
			}
			this.isCompiled = false;
		});

		gridComposer = new WuxiaTechniqueComposeGrid(5, 5, renderGrid);
		gridComposer.onClicked = onGridComposerClick;
		gridComposer.onRelease = onGridComposerRelease;
		composerPanel.addChild(gridComposer);

		screen.addRenderableWidget(aspectsPanel);
		screen.addRenderableWidget(composerPanel);
		screen.addRenderableWidget(techniqueStatsPanel);
		screen.addRenderableWidget(searchField);
		screen.addRenderableWidget(compileBtn);
		screen.addRenderableWidget(saveBtn);
	}

	private final WuxiaTechniqueComposeGrid.MouseInputPredicate onGridComposerRelease = (mouseX, mouseY, button) -> {
		if (this.draggingAspect != null) {
			var hexC = this.gridComposer.getHexCoordinateFromMousePosition((int) mouseX, (int) mouseY);
			if (hexC != null) {
				this.gridComposer.addAspectToGrid(hexC, this.draggingAspect);
			}
		}
		this.draggingAspect = null;
		this.dragPosition = new double[]{0d, 0d, 0d, 0d};
		return false;
	};


	private final WuxiaTechniqueComposeGrid.MouseInputPredicate onGridComposerClick = (mouseX, mouseY, button) -> {
		var hexC = this.gridComposer.getHexCoordinateFromMousePosition((int) mouseX, (int) mouseY);
		if (hexC != null && button == 1) {
			this.gridComposer.removeAspectToGrid(hexC);
		}
		return false;
	};

	private BiConsumer<Double, Double> onAspectClick(ResourceLocation aspectLocation) {
		return (mx, my) -> {
			this.draggingAspect = aspectLocation;
			this.dragPosition = new double[]{mx, my, 0d, 0d};
		};
	}

	private BiConsumer<Double, Double> onAspectRelease() {
		return (mx, my) -> {
			this.draggingAspect = null;
			this.dragPosition = new double[]{0d, 0d, 0d, 0d};
		};
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var aspects = cultivation.getAspects();
		if (aspects.getKnownAspectsCount() != this.aspectsPanel.getChildrenCount()) {
			this.aspectsPanel.clearChildren();
			this.aspectWidgets.clear();
			for (var aspect : aspects.getKnownAspects()) {
				var aspectWidget = new WuxiaAspectWidget(0, 0, aspect);
				aspectWidget.setOnClicked(onAspectClick(aspect));
				aspectWidget.setOnRelease(onAspectRelease());
				this.aspectsPanel.addChild(aspectWidget);
				this.aspectWidgets.put(aspect, aspectWidget);
			}
		}
		if (this.draggingAspect != null) {
			var techAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.draggingAspect);
			if (techAspect != null) {
				poseStack.pushPose();
				poseStack.translate(mouseX, mouseY, 0);
				RenderSystem.setShaderTexture(0, techAspect.textureLocation);
				GuiComponent.blit(poseStack,
						-16, -16,
						32, 32,
						0, 0,
						32, 32, 32, 32);
				poseStack.popPose();
			}
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
