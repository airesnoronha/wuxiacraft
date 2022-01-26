package wuxiacraft.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.widgets.WuxiaAspectWidget;
import wuxiacraft.client.gui.widgets.WuxiaFlowPanel;
import wuxiacraft.init.WuxiaTechniqueAspects;

import java.awt.*;

public class AspectsTab extends IntrospectionTab {
	public AspectsTab(String name) {
		super(name, new Point(32,36));
	}

	@Override
	public void init(IntrospectionScreen screen) {
		var aspectsPanel = new WuxiaFlowPanel(36, 36, 120, 30, new TextComponent(""));
		aspectsPanel.addChild(new WuxiaAspectWidget(0,0, WuxiaTechniqueAspects.START.getId()));
		aspectsPanel.addChild(new WuxiaAspectWidget(0,0, WuxiaTechniqueAspects.BODY_GATHERING.getId()));
		aspectsPanel.addChild(new WuxiaAspectWidget(0,0, WuxiaTechniqueAspects.FIRE_CONNECT_TO_BODY_1.getId()));
		aspectsPanel.addChild(new WuxiaAspectWidget(0,0, WuxiaTechniqueAspects.FIRE_ASPECT_1.getId()));
		screen.addRenderableWidget(aspectsPanel);
	}

	@Override
	public void close(IntrospectionScreen screen) {
		screen.clearWidgets();
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {

	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
