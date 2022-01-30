package com.lazydragonstudios.wuxiacraft.client.gui.tab;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaAspectWidget;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaFlowPanel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaScrollPanel;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.widgets.*;

import java.awt.*;
import java.util.HashMap;

public class AspectsTab extends IntrospectionTab {

	private WuxiaFlowPanel aspectsPanel;
	private WuxiaScrollPanel aspectsStatsPanel;

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private HashMap<ResourceLocation, WuxiaAspectWidget> aspectWidgets;

	private ResourceLocation selectedAspectWidget = null;

	public AspectsTab(String name) {
		super(name, new Point(32,36));
	}

	@Override
	public void init(IntrospectionScreen screen) {
		aspectWidgets = new HashMap<>();
		aspectsPanel = new WuxiaFlowPanel(36, 36, 120, 30, new TextComponent(""));
		aspectsStatsPanel = new WuxiaScrollPanel(36, 36, 200, 30, new TextComponent(""));
		screen.addRenderableWidget(aspectsPanel);
		screen.addRenderableWidget(aspectsStatsPanel);
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if(player == null) return;
		var cultivation = Cultivation.get(player);
		var aspects = cultivation.getAspects();
		if(aspects.getKnownAspectsCount() != this.aspectsPanel.getChildrenCount()) {
			this.aspectsPanel.clearChildren();
			this.aspectWidgets.clear();
			for(var aspect : aspects.getKnownAspects()) {
				var aspectWidget = new WuxiaAspectWidget(0,0,aspect);
				aspectWidget.setOnClicked((mx, my) -> {
					changeSelected(aspect);
				});
				this.aspectsPanel.addChild(aspectWidget);
				this.aspectWidgets.put(aspect, aspectWidget);
			}
		}
		var freeXSpace = Minecraft.getInstance().getWindow().getGuiScaledWidth() - 36;
		var freeYSpace = Minecraft.getInstance().getWindow().getGuiScaledHeight() - 36;
		var stretchedSpace = freeXSpace - 200;
		this.aspectsPanel.setWidth(stretchedSpace);
		this.aspectsPanel.setHeight(freeYSpace);
		this.aspectsStatsPanel.x = 36+stretchedSpace;
		this.aspectsStatsPanel.setHeight(freeYSpace);
	}

	private void changeSelected(ResourceLocation location) {
		if(this.aspectWidgets.containsKey(location)) {
			this.selectedAspectWidget = location;
			changeSelectedAspectStats();
		}
	}

	private void changeSelectedAspectStats() {
		var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.selectedAspectWidget);
		if(aspect == null) return;
		this.aspectsStatsPanel.clearChildren();
		for(var widget : aspect.getStatsSheetDescriptor(this.selectedAspectWidget)) {
			this.aspectsStatsPanel.addChild(widget);
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
