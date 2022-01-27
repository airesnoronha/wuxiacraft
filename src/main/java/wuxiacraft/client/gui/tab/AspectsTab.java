package wuxiacraft.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.widgets.WuxiaAspectWidget;
import wuxiacraft.client.gui.widgets.WuxiaFlowPanel;
import wuxiacraft.cultivation.Cultivation;

import java.awt.*;
import java.util.HashMap;

public class AspectsTab extends IntrospectionTab {

	private WuxiaFlowPanel aspectsPanel;
	private WuxiaFlowPanel aspectsStatsPanel;

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
		aspectsStatsPanel = new WuxiaFlowPanel(36, 36, 300, 30, new TextComponent(""));
		screen.addRenderableWidget(aspectsPanel);
	}

	@Override
	public void close(IntrospectionScreen screen) {
		screen.clearWidgets();
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if(player == null) return;
		var cultivation = Cultivation.get(player);
		var aspects = cultivation.getAspects();
		if(aspects.getKnownAspectsCount() != this.aspectsPanel.getChildrenCount()) {
			this.aspectsPanel.clearChildren();
			for(var aspect : aspects.getKnownAspects()) {
				var aspectWidget = new WuxiaAspectWidget(0,0,aspect);
				aspectWidget.setOnClicked((mx, my) -> {
					changeSelected(aspect);
				});
				this.aspectsPanel.addChild(aspectWidget);
				this.aspectWidgets.put(aspect, aspectWidget);
			}
		}
		var freeXSpace = Minecraft.getInstance().getWindow().getScreenWidth() - 36;
		var freeYSpace = Minecraft.getInstance().getWindow().getScreenHeight() - 36;
		var stretchedSpace = freeXSpace - 300;
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

	//TODO make this change the stats sheet when we do have one;
	private void changeSelectedAspectStats() {
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
