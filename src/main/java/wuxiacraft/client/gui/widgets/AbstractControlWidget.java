package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public abstract class AbstractControlWidget extends AbstractWidget implements IWidgetClickedHandler{
	public int texPosX;
	public int texPosY;
	public int texWidth;
	public int texHeight;

	public AbstractControlWidget(int x, int y, int width, int height, int texPosX, int texPosY) {
		super(x, y, width, height);
		this.texPosX = texPosX;
		this.texPosY = texPosY;
		this.texWidth = width;
		this.texHeight = height;
	}

	@Override
	public void renderBg(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		GuiComponent.blit(poseStack, this.x, this.y, this.width, this.height, this.texPosX, this.texPosY, this.texWidth, this.texHeight, 256, 256);
	}

	@Override
	public abstract Runnable onClicked();
}
