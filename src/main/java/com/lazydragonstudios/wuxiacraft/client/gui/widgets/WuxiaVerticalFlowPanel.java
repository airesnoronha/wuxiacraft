package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import net.minecraft.network.chat.Component;

public class WuxiaVerticalFlowPanel extends WuxiaFlowPanel {
	public WuxiaVerticalFlowPanel(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}

	@Override
	protected void rearrangeItems() {
		int currentLeftPos = 0;
		int currentTopPos = margin;
		int currentLineHeight = 0;
		this.contentWidth = 0;
		for (var widget : this.children) {
			currentTopPos += currentLineHeight + margin;
			widget.x = currentLeftPos + margin;
			widget.y = currentTopPos;
			currentLineHeight = Math.max(currentLineHeight, widget.getHeight());
			this.contentWidth = Math.max(this.contentWidth, widget.x + widget.getWidth());
			this.contentHeight = Math.max(this.contentHeight, widget.y + widget.getHeight());
		}
	}
}
