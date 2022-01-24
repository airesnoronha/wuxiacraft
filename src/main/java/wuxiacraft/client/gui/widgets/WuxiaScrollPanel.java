package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;

/**
 * A basic scroll panel that has basic scrolling controls
 * This does not support negative values yet, so I'll discard every widget on negative space
 */
@ParametersAreNonnullByDefault
public class WuxiaScrollPanel extends AbstractWidget {

	public enum OverflowType {
		AUTO,
		HIDDEN,
		SCROLL_X,
		SCROLL_Y,
		SCROLL
	}

	protected LinkedList<AbstractWidget> children = new LinkedList<>();

	protected int contentStartingX = 0;

	protected int contentStartingY = 0;

	/**
	 * Starting from 0
	 * If content does not start from 0 that might be intentional
	 */
	protected int contentWidth = 0;

	protected int contentHeight = 0;

	protected int totalScrollWidth = 0;

	protected int totalScrollHeight = 0;

	public  int currentScrollHeight = 0;

	public int currentScrollWidth = 0;

	public OverflowType overflow = OverflowType.AUTO;

	public WuxiaScrollPanel(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}

	public OverflowType getOverflow() {
		return overflow;
	}

	public void setOverflow(OverflowType overflow) {
		this.overflow = overflow;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
	}

	public void addChild(AbstractWidget child) {
		this.children.add(child);
		if(child.x+child.getWidth() > this.contentWidth);
	}

	public boolean isShowingHorizontalScroll() {
		if(this.overflow == OverflowType.HIDDEN) return false;
		if(this.overflow == OverflowType.SCROLL_X) return true;
		if(this.overflow == OverflowType.SCROLL) return true;
		if(this.overflow == OverflowType.AUTO) {
			return this.width < this.contentWidth;
		}
		return false;
	}

	public boolean isShowingVerticalScroll() {
		if(this.overflow == OverflowType.HIDDEN) return false;
		if(this.overflow == OverflowType.SCROLL_Y) return true;
		if(this.overflow == OverflowType.SCROLL) return true;
		if(this.overflow == OverflowType.AUTO) {
			return this.height < this.contentHeight;
		}
		return false;
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}
}
