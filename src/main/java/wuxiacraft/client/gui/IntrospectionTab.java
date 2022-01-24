package wuxiacraft.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public abstract class IntrospectionTab {

	public final String name;

	/**
	 * the icon's position in the tab_icons file
	 */
	public final Point icon;

	public IntrospectionTab(String name, Point icon) {
		this.name = name;
		this.icon = icon;
	}

	public abstract void renderBG(PoseStack poseStack, int mouseX, int mouseY, int width, int height);

	public abstract void renderLabels(PoseStack poseStack, int mouseX, int mouseY, int width, int height);

}
