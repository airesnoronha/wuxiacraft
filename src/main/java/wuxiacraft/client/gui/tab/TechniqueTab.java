package wuxiacraft.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.cultivation.System;

import java.awt.*;

public class TechniqueTab extends IntrospectionTab {

	public final System system;

	public TechniqueTab(String name, Point icon, System system) {
		super(name, icon);
		this.system = system;
	}

	@Override
	public void init(IntrospectionScreen screen) {
		super.init(screen);
	}

	@Override
	public void close(IntrospectionScreen screen) {
		super.close(screen);
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {

	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
