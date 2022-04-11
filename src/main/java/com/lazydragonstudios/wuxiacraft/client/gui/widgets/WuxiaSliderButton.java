package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Consumer;

public class WuxiaSliderButton extends AbstractSliderButton {

	private final Consumer<Double> apply;

	public WuxiaSliderButton(int pX, int pY, int pWidth, int pHeight, double pValue, Consumer<Double> apply) {
		super(pX, pY, pWidth, pHeight, new TextComponent(String.format("%.1f%%", pValue * 100)), pValue);
		this.apply = apply;
	}

	@Override
	protected void updateMessage() {
		this.setMessage(new TextComponent(String.format("%.1f%%", this.value * 100)));
	}

	@Override
	protected void applyValue() {
		this.apply.accept(this.value);
	}

	public double getValue() {
		return this.value;
	}

	@Override
	protected void onDrag(double pMouseX, double pMouseY, double pDragX, double pDragY) {
		if (!MathUtil.inBounds(pMouseX, pMouseY, this.x, this.y, this.getWidth(), this.getHeight())) return;
		super.onDrag(pMouseX, pMouseY, pDragX, pDragY);
	}
}
