package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.WuxiaCraft;

public abstract class AbstractWidget {

	public static final ResourceLocation ui_controls = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/ui_controls.png");

	public int x;
	public int y;
	public int width;
	public int height;

	public AbstractWidget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public abstract void renderBg(PoseStack poseStack, int mouseX, int mouseY, float partialTicks);

	public void renderLabels(PoseStack poseStack, Font font, int mouseX, int mouseY ) {
	}

	public void renderTooltip(PoseStack poseStack, int mouseX, int mouseY ) {
	}

}
