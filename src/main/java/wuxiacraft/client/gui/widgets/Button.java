package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import wuxiacraft.util.MathUtil;

public class Button extends AbstractNineSliceControlWidget {

	public Component displayValue;

	private final Runnable onClicked;

	public Button(int x, int y, int width, int height, Component displayValue, Runnable onClicked) {
		super(x, y, width, height, 0, 0, 5, 5);
		this.displayValue = displayValue;
		this.onClicked = onClicked;
	}

	@Override
	public Runnable onClicked() {
		return this.onClicked;
	}

	@Override
	public void renderBg(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if(MathUtil.inBounds(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
			this.texPosX = 15;
		} else {
			this.texPosX = 0;
		}
		super.renderBg(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void renderLabels(PoseStack poseStack, Font font, int mouseX, int mouseY) {
		GuiComponent.drawCenteredString(poseStack, font, this.displayValue, this.x + this.width / 2+1, this.y + (this.height - font.lineHeight) / 2+1, 0x101010);
		GuiComponent.drawCenteredString(poseStack, font, this.displayValue, this.x + this.width / 2, this.y + (this.height - font.lineHeight) / 2, 0xFFFFFF);
	}
}
