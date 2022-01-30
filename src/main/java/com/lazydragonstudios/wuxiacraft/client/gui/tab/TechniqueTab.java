package com.lazydragonstudios.wuxiacraft.client.gui.tab;

import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.*;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;
import java.util.HashMap;

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
		int stretchedSpace = scaledWidth - 316;
		aspectWidgets = new HashMap<>();

		searchField = new WuxiaTextField(36, 36, 116, 20, new TextComponent(""));

		aspectsPanel = new WuxiaFlowPanel(36+20+3, 36, 116, scaledHeight - 16, new TextComponent(""));
		techniqueStatsPanel = new WuxiaScrollPanel(scaledWidth - 200, 36, 200, scaledHeight - 55 - 36, new TextComponent(""));

		composerPanel = new WuxiaScrollPanel(36 + 116, 36, stretchedSpace, scaledHeight - 36, new TextComponent(""));
		composerPanel.setOverflow(WuxiaScrollPanel.OverflowType.HIDDEN);

		compileBtn = new WuxiaButton(scaledWidth - 190, scaledHeight - 50, 180, 20, new TextComponent("Compile"), () -> {
		});
		saveBtn = new WuxiaButton(scaledWidth - 190, scaledHeight - 25, 180, 20, new TextComponent("Save"), () -> {
		});

		gridComposer = new WuxiaTechniqueComposeGrid(composerPanel.getWidth()/2, composerPanel.getHeight()/2, renderGrid);
		composerPanel.addChild(gridComposer);

		screen.addRenderableWidget(aspectsPanel);
		screen.addRenderableWidget(techniqueStatsPanel);
		screen.addRenderableWidget(compileBtn);
		screen.addRenderableWidget(saveBtn);
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
				this.aspectsPanel.addChild(aspectWidget);
				this.aspectWidgets.put(aspect, aspectWidget);
			}
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
