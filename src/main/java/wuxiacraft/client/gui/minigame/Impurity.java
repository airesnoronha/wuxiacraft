package wuxiacraft.client.gui.minigame;

import com.mojang.blaze3d.matrix.MatrixStack;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.util.MathUtils;

public class Impurity {

	public float x;
	public float y;

	public float red;
	public float green;
	public float blue;

	public Impurity(int x, int y) {
		this.x = x;
		this.y = y;
		this.red = 0.20f;
		this.green = 0.35f;
		this.blue = 0.20f;
	}

	public Impurity setColor(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		return this;
	}

	public void draw(MatrixStack stack, MeditateScreen screen) {
		screen.blitColored(stack, (int) this.x - 6, (int) this.y - 6, 0, 8, 12, 12, this.red, this.green, this.blue);
	}
}
