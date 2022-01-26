package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WuxiaLabel extends AbstractWidget {

	private final int color;

	public WuxiaLabel(int x, int y, Component message, int color) {
		super(x, y, 0, 0, message);
		this.color = color;
		this.setMessage(message);
	}

	@Override
	public void setMessage(Component message) {
		super.setMessage(message);
		int width = Minecraft.getInstance().font.width(message.getString());
		int height = Minecraft.getInstance().font.lineHeight;
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		GuiComponent.drawString(poseStack, Minecraft.getInstance().font, this.getMessage(), this.x, this.y, this.color);
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}
}
