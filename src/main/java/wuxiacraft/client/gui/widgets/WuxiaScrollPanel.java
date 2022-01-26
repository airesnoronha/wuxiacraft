package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import wuxiacraft.util.MathUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;

//TODO probably add compatibility to negative space widgets

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

	protected int scrollBarHeight = 10;

	protected int scrollBarWidth = 10;

	/**
	 * this is planned for negative space widgets
	 * can't figure out the logic for widget's content width yet for negative space
	 */
	protected int contentStartingX = 0;

	/**
	 * same as above but vertical
	 */
	protected int contentStartingY = 0;

	/**
	 * Starting from 0
	 * If content does not start from 0 that might be intentional
	 */
	protected int contentWidth = 0;

	/**
	 * Starting from 0
	 * If content does not start from 0 that might be intentional
	 */
	protected int contentHeight = 0;

	/**
	 * this is how much we can scroll the scroll bar
	 * probably this goes from 0 to (this.contentWidth-this.width)
	 */
	protected int totalScrollWidth = 0;

	/**
	 * this is how much we can scroll the scroll bar
	 * probably this goes from 0 to (this.contentHeight-this.height)
	 */
	protected int totalScrollHeight = 0;

	/**
	 * this should be pretty obvious
	 */
	public int currentScrollY = 0;

	/**
	 * this as well
	 */
	public int currentScrollX = 0;

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

	/**
	 * Renders all children widgets.
	 * Using OpenGl Scissor Test, much better than using Stencil Test
	 * Using poseStack translate to move content
	 *
	 * @param poseStack    the rendering pose stack
	 * @param mouseX       the current mouse X
	 * @param mouseY       the current mouse Y
	 * @param partialTicks the partial tick
	 */
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;

		Vector3f windowCoordinatesInit = getWindowCoordinates(this.x, this.y);
		Vector3f windowCoordinatesEnd = getWindowCoordinates(this.x + innerWidth, this.y + innerHeight);
		Vector3f windowCoordinatesSize = new Vector3f(windowCoordinatesEnd.x() - windowCoordinatesInit.x(), windowCoordinatesInit.y() - windowCoordinatesEnd.y(), 0f);

		RenderSystem.enableScissor((int) windowCoordinatesInit.x(), (int) windowCoordinatesEnd.y(), (int) windowCoordinatesSize.x(), (int) windowCoordinatesSize.y());
		poseStack.pushPose();
		poseStack.translate(this.x - this.currentScrollX, this.y - this.currentScrollY, 0);
		for (var widget : this.children) {
			widget.render(poseStack, mouseX + this.currentScrollX - this.x, mouseY + this.currentScrollY - this.y, partialTicks);
		}
		poseStack.popPose();
		RenderSystem.disableScissor();
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
		if (isShowingHorizontalScroll()) {
			//bg bar
			int horizontalBarRenderSteps = innerWidth / scrollBarWidth;
			int horizontalBarRemainderFill = innerWidth % scrollBarWidth;
			for (int i = 0; i < horizontalBarRenderSteps; i++) {
				int xPos = this.x + i * scrollBarWidth;
				//scroll track bg
				GuiComponent.blit(poseStack,
						xPos, this.y + innerHeight, //in screen position
						10, 10, //in screen width
						80, 15, //tex position
						10, 10, //texture width
						256, 256 //image size
				);
				//scroll track bg
				GuiComponent.blit(poseStack,
						this.x + innerWidth - horizontalBarRemainderFill, this.y + innerHeight, //in screen position
						horizontalBarRemainderFill, 10, //in screen width
						80, 15, //tex position
						horizontalBarRemainderFill, 10, //texture width
						256, 256 //image size
				);
			}
			//button left
			int texX = 0;
			if (MathUtil.inBounds(mouseX, mouseY, this.x, this.y + innerHeight, scrollBarWidth, scrollBarHeight)) {
				texX = 10;
			}
			GuiComponent.blit(poseStack,
					this.x, this.y + innerHeight, //in screen position
					10, 10, //in screen width
					texX, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//arrow left
			GuiComponent.blit(poseStack,
					this.x, this.y + innerHeight, //in screen position
					10, 10, //in screen width
					50, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//button right
			texX = 0;
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth - scrollBarWidth, this.y + innerHeight, scrollBarWidth, scrollBarHeight)) {
				texX = 10;
			}
			GuiComponent.blit(poseStack,
					this.x + innerWidth - scrollBarWidth, this.y + innerHeight, //in screen position
					10, 10, //in screen width
					texX, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//arrow right
			GuiComponent.blit(poseStack,
					this.x + innerWidth - scrollBarWidth, this.y + innerHeight, //in screen position
					10, 10, //in screen width
					30, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//scroll indicator
			int indPos = getHorizontalScrollIndicatorPosition();
			texX = 60;
			if (MathUtil.inBounds(mouseX, mouseY, this.x + scrollBarWidth + indPos, this.y + innerHeight, scrollBarWidth, scrollBarHeight)) {
				texX = 70;
			}
			GuiComponent.blit(poseStack,
					this.x + scrollBarWidth + indPos, this.y + innerHeight, //in screen position
					10, 10, //in screen width
					texX, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
		}
		if (isShowingVerticalScroll()) {
			//bg bar
			int verticalBarRenderSteps = innerHeight / scrollBarHeight;
			int verticalBarRemainderFill = innerHeight % scrollBarHeight;
			for (int i = 0; i < verticalBarRenderSteps; i++) {
				int yPos = this.y + i * scrollBarHeight;
				//scroll track bg
				GuiComponent.blit(poseStack,
						this.x + innerWidth, yPos, //in screen position
						10, 10, //in screen width
						90, 15, //tex position
						10, 10, //texture width
						256, 256 //image size
				);
				//scroll track bg
				GuiComponent.blit(poseStack,
						this.x + innerWidth, this.y + innerHeight - verticalBarRemainderFill, //in screen position
						10, verticalBarRemainderFill, //in screen width
						90, 15, //tex position
						10, verticalBarRemainderFill, //texture width
						256, 256 //image size
				);
			}
			//button top
			int texX = 0;
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y, scrollBarWidth, scrollBarHeight)) {
				texX = 10;
			}
			GuiComponent.blit(poseStack,
					this.x + innerWidth, this.y, //in screen position
					10, 10, //in screen width
					texX, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//arrow top
			GuiComponent.blit(poseStack,
					this.x + innerWidth, this.y, //in screen position
					10, 10, //in screen width
					20, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//button bottom
			texX = 0;
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y + innerHeight - scrollBarHeight, scrollBarWidth, scrollBarHeight)) {
				texX = 10;
			}
			GuiComponent.blit(poseStack,
					this.x + innerWidth, this.y + innerHeight - scrollBarHeight, //in screen position
					10, 10, //in screen width
					texX, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//arrow bottom
			GuiComponent.blit(poseStack,
					this.x + innerWidth, this.y + innerHeight - scrollBarHeight, //in screen position
					10, 10, //in screen width
					40, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
			//scroll indicator
			int indPos = getVerticalScrollIndicatorPosition();
			texX = 60;
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y + scrollBarHeight + indPos, scrollBarWidth, scrollBarHeight)) {
				texX = 70;
			}
			GuiComponent.blit(poseStack,
					this.x + innerWidth, this.y + scrollBarHeight + indPos, //in screen position
					10, 10, //in screen width
					texX, 15, //tex position
					10, 10, //texture width
					256, 256 //image size
			);
		}
		RenderSystem.disableBlend();
	}

	public void addChild(AbstractWidget child) {
		this.children.add(child);
		if (child.x + child.getWidth() > this.contentWidth) {
			this.contentWidth = child.x + child.getWidth();
			this.totalScrollWidth = Math.max(0, this.contentWidth - this.width + scrollBarWidth);
		}
		if (child.y + child.getWidth() > this.contentHeight) {
			this.contentHeight = child.y + child.getHeight();
			this.totalScrollHeight = Math.max(0, this.contentHeight - this.height + scrollBarHeight);
		}
	}

	@Override
	public void setHeight(int value) {
		super.setHeight(value);
		this.totalScrollHeight = Math.max(0, this.contentHeight - this.height + scrollBarHeight);
		this.currentScrollY = (int)MathUtil.clamp(this.currentScrollY, 0, this.totalScrollHeight);
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.totalScrollWidth = Math.max(0, this.contentWidth - this.width + scrollBarWidth);
		this.currentScrollX = (int)MathUtil.clamp(this.currentScrollX, 0, this.totalScrollWidth);
	}

	public int getHorizontalScrollIndicatorPosition() {
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		return (int) (((float) innerWidth - (float) scrollBarWidth * 3) * ((float) currentScrollX / (float) totalScrollWidth));
	}

	public int getVerticalScrollIndicatorPosition() {
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;
		return (int) (((float) innerHeight - (float) scrollBarHeight * 3) * ((float) currentScrollY / (float) totalScrollHeight));
	}


	public boolean isShowingHorizontalScroll() {
		if (this.overflow == OverflowType.HIDDEN) return false;
		if (this.overflow == OverflowType.SCROLL_X) return true;
		if (this.overflow == OverflowType.SCROLL) return true;
		if (this.overflow == OverflowType.AUTO) {
			return this.width < this.contentWidth;
		}
		return false;
	}

	public boolean isShowingVerticalScroll() {
		if (this.overflow == OverflowType.HIDDEN) return false;
		if (this.overflow == OverflowType.SCROLL_Y) return true;
		if (this.overflow == OverflowType.SCROLL) return true;
		if (this.overflow == OverflowType.AUTO) {
			return this.height < this.contentHeight;
		}
		return false;
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}

	private int scrollIndicatorGrabbed;
	private boolean isHorizontalScrollIndicatorGrabbed = false;
	private boolean isVerticalScrollIndicatorGrabbed = false;

	@Override
	public void onClick(double mouseX, double mouseY) {
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;
		if (isShowingHorizontalScroll()) {
			//button left
			if (MathUtil.inBounds(mouseX, mouseY, this.x, this.y + innerHeight, scrollBarWidth, scrollBarHeight)) {
				this.currentScrollX = Math.max(0, this.currentScrollX - this.totalScrollWidth / 10);
			}
			//button right
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth - scrollBarWidth, this.y + innerHeight, scrollBarWidth, scrollBarHeight)) {
				this.currentScrollX = Math.min(this.totalScrollWidth, this.currentScrollX + this.totalScrollWidth / 10);
			}
			//scroll indicator
			if (MathUtil.inBounds(mouseX, mouseY, this.x + scrollBarWidth + getHorizontalScrollIndicatorPosition(), this.y + innerHeight, scrollBarWidth, scrollBarHeight)) {
				scrollIndicatorGrabbed = (int) mouseX - (this.x + scrollBarWidth + getHorizontalScrollIndicatorPosition());
				isHorizontalScrollIndicatorGrabbed = true;
			}
		}
		if (isShowingVerticalScroll()) {
			//button top
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y, scrollBarWidth, scrollBarHeight)) {
				this.currentScrollY = Math.max(0, this.currentScrollY - this.totalScrollHeight / 10);
			}
			//button bottom
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y + innerHeight - scrollBarHeight, scrollBarWidth, scrollBarHeight)) {
				this.currentScrollY = Math.min(this.totalScrollHeight, this.currentScrollY + this.totalScrollHeight / 10);
			}
			//scroll indicator
			if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y + this.scrollBarHeight + getVerticalScrollIndicatorPosition(), scrollBarWidth, scrollBarHeight)) {
				scrollIndicatorGrabbed = (int) mouseY - (this.y + this.scrollBarHeight + getVerticalScrollIndicatorPosition());
				isVerticalScrollIndicatorGrabbed = true;
			}
		}
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		isHorizontalScrollIndicatorGrabbed = false;
		isVerticalScrollIndicatorGrabbed = false;
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double mouseDeltaX, double mouseDeltaY) {
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;
		if (isShowingHorizontalScroll()) {
			if (isHorizontalScrollIndicatorGrabbed) {
				double spaceToDislocate = (innerWidth - scrollBarWidth * 3);
				double positionInScrollBar = mouseX - this.x - this.scrollBarWidth - scrollIndicatorGrabbed;
				positionInScrollBar = MathUtil.clamp(positionInScrollBar, 0, spaceToDislocate);
				this.currentScrollX = (int) ((positionInScrollBar / spaceToDislocate) * (double) this.totalScrollWidth);
			}
		}
		if (isShowingVerticalScroll()) {
			if (isVerticalScrollIndicatorGrabbed) {
				double spaceToDislocate = (innerHeight - scrollBarHeight * 3);
				double positionInScrollBar = mouseY - this.y - this.scrollBarHeight - scrollIndicatorGrabbed;
				positionInScrollBar = MathUtil.clamp(positionInScrollBar, 0, spaceToDislocate);
				this.currentScrollY = (int) ((positionInScrollBar / spaceToDislocate) * (double) this.totalScrollHeight);
			}
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollCount) {
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;
		boolean somethingClicked = false;
		for (var widget : this.children) {
			if (MathUtil.inBounds(mouseX, mouseY, this.x - this.currentScrollX + widget.x, this.y - this.currentScrollY + widget.y, widget.getWidth(), widget.getHeight())) {
				somethingClicked = somethingClicked || widget.mouseScrolled(mouseX + this.currentScrollX - this.x, mouseY + this.currentScrollY - this.y, scrollCount);
			}
		}
		if (!somethingClicked) {
			this.currentScrollY = (int) MathUtil.clamp(this.currentScrollY + this.totalScrollHeight * scrollCount * (-0.07), 0, this.totalScrollHeight);
		}
		//horizontal scroll bar
		if (MathUtil.inBounds(mouseX, mouseY, this.x, this.y + innerHeight, innerWidth, scrollBarHeight)) {
			this.currentScrollX = (int) MathUtil.clamp(this.currentScrollX + this.totalScrollWidth * scrollCount * (-0.07), 0, this.totalScrollWidth);
		}
		//vertical scroll bar
		if (MathUtil.inBounds(mouseX, mouseY, this.x + innerWidth, this.y, scrollBarWidth, innerHeight)) {
			this.currentScrollY = (int) MathUtil.clamp(this.currentScrollY + this.totalScrollHeight * scrollCount * (-0.07), 0, this.totalScrollHeight);
		}
		return somethingClicked || super.mouseScrolled(mouseX, mouseY, scrollCount);
	}

	public double[] innerPartGrabbed = new double[]{0, 0, 0, 0};
	public boolean isInnerPartGrabbed = false;

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean somethingClicked = false;
		for (var widget : this.children) {
			somethingClicked = somethingClicked || widget.mouseClicked(mouseX + this.currentScrollX - this.x, mouseY + this.currentScrollY - this.y, button);
		}
		if (!somethingClicked && this.isValidClickButton(button)) {
			isInnerPartGrabbed = true;
			innerPartGrabbed = new double[]{mouseX, mouseY, this.currentScrollX, this.currentScrollY};
		} else {
			if (this.active && this.visible) {
				if (this.isValidClickButton(button)) {
					boolean flag = this.clicked(mouseX, mouseY);
					if (flag) {
						this.playDownSound(Minecraft.getInstance().getSoundManager());
						this.onClick(mouseX, mouseY);
						return true;
					}
				}
			}
		}
		return somethingClicked;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDeltaX, double mouseDeltaY) {
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;
		boolean somethingClicked = false;
		if (MathUtil.inBounds(mouseX, mouseY, this.x, this.y, innerWidth, innerHeight)) {
			for (var widget : this.children) {
				if (MathUtil.inBounds(mouseX, mouseY, this.x - this.currentScrollX + widget.x, this.y - this.currentScrollY + widget.y, widget.getWidth(), widget.getHeight())) {
					somethingClicked = somethingClicked ||
							widget.mouseDragged(mouseX + this.currentScrollX - this.x, mouseY + this.currentScrollY - this.y,
									button,
									mouseDeltaX + this.currentScrollX - this.x, mouseDeltaY + this.currentScrollY - this.y);
				}
			}
			if (!somethingClicked && this.isValidClickButton(button)) {
				var deltaX = mouseX - innerPartGrabbed[0];
				var deltaY = mouseY - innerPartGrabbed[1];

				this.currentScrollX = (int) MathUtil.clamp(innerPartGrabbed[2] - deltaX, 0, this.totalScrollWidth);
				this.currentScrollY = (int) MathUtil.clamp(innerPartGrabbed[3] - deltaY, 0, this.totalScrollHeight);
			}
		} else {
			if (this.isValidClickButton(button)) {
				this.onDrag(mouseX, mouseY, mouseDeltaX, mouseDeltaY);
				return true;
			}
		}
		return somethingClicked;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		isInnerPartGrabbed = false;
		int scrollBarWidth = 10; //the number of pixels the scroll bar is going to have horizontally
		int scrollBarHeight = 10; // the number of pixels the scroll bar is going to have vertically
		int innerWidth = isShowingVerticalScroll() ? this.width - scrollBarWidth : this.width;
		int innerHeight = isShowingHorizontalScroll() ? this.height - scrollBarHeight : this.height;
		boolean somethingClicked = false;
		if (MathUtil.inBounds(mouseX, mouseY, this.x, this.y, innerWidth, innerHeight)) {
			for (var widget : this.children) {
				if (MathUtil.inBounds(mouseX, mouseY, this.currentScrollX + widget.x, this.currentScrollY + widget.y, widget.getWidth(), widget.getHeight())) {
					somethingClicked = somethingClicked || widget.mouseReleased(mouseX, mouseX, button);
				}
			}
		} else {
			if (this.isValidClickButton(button)) {
				this.onRelease(mouseX, mouseY);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean changeFocus(boolean p_93691_) {
		return super.changeFocus(p_93691_);
	}

	public static Vector3f getWindowCoordinates(int x, int y) {
		Vector4f pos = new Vector4f(x, y, 0f, 1f);
		pos.transform(RenderSystem.getModelViewMatrix());
		pos.transform(RenderSystem.getProjectionMatrix());
		Vector3f ndc = new Vector3f(pos.x() / pos.w(), pos.y() / pos.w(), pos.z() / pos.w());
		float windowWidth = Minecraft.getInstance().getWindow().getWidth();
		float windowHeight = Minecraft.getInstance().getWindow().getHeight();
		return new Vector3f((windowWidth / 2f) * (1f + ndc.x()), (windowHeight / 2f) * (1f + ndc.y()), 0);
	}
}
