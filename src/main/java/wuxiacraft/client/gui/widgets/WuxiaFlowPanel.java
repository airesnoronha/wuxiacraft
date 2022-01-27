package wuxiacraft.client.gui.widgets;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class WuxiaFlowPanel extends WuxiaScrollPanel {

	/**
	 * this is the margin between items and the borders as well
	 */
	public int margin = 10;

	/**
	 * this holds the height of each line, and we get the height by querying the line top position (y)
	 */
	private HashMap<Integer, Integer> lineTopAndHeight = new HashMap<>();

	public WuxiaFlowPanel(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	@Override
	public void addChild(@Nonnull AbstractWidget child) {
		this.children.add(child);
		this.rearrangeItems();
	}

	@Override
	public void setHeight(int value) {
		super.setHeight(value);
		this.rearrangeItems();
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.rearrangeItems();
		;
	}

	private void rearrangeItems() {
		int currentLeftPos = 0;
		int currentTopPos = margin;
		int currentLineHeight = 0;
		this.contentWidth = 0;
		for (var widget : this.children) {
			if (currentLeftPos + widget.getWidth() + margin > this.width - this.scrollBarWidth) {
				currentLeftPos = 0;
				currentTopPos += currentLineHeight + margin;
			}
			widget.x = currentLeftPos + margin;
			widget.y = currentTopPos;
			currentLeftPos = widget.x + widget.getWidth();
			currentLineHeight = Math.max(currentLineHeight, widget.getHeight());
			this.contentWidth = Math.max(this.contentWidth, widget.x + widget.getWidth());
			this.contentHeight = Math.max(this.contentHeight, widget.y + widget.getHeight());
		}
	}
}
