package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import wuxiacraft.client.gui.MeditateScreen;

public interface IMinigame {

	void handleClick(int mouseX, int mouseY, MeditateScreen screen);

	void handleMouseDown(double mouseX, double mouseY, MeditateScreen screen);

	void handleMouseUp(double mouseX, double mouseY, MeditateScreen screen);

	void renderBackground(MatrixStack stack, MeditateScreen screen);

	void renderForeground(MatrixStack stack, MeditateScreen screen);

	void handleTooltips(MatrixStack stack, int mouseX, int mouseY);

	void tick();

}
