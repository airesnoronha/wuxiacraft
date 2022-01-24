package wuxiacraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import wuxiacraft.client.gui.widgets.AbstractWidget;
import wuxiacraft.client.gui.widgets.IWidgetClickedHandler;
import wuxiacraft.util.MathUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
public abstract class AbstractWuxiaWidgetScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

	public LinkedList<AbstractWidget> widgets;

	public AbstractWuxiaWidgetScreen(T menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.widgets = new LinkedList<>();
		this.topPos = 0;
		this.leftPos = 0;
	}

	public void addCustomWidget(AbstractWidget widget) {
		this.widgets.add(widget);
	}

	public void renderWidgetBG(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderTexture(0, AbstractWidget.ui_controls);
		for(var widget : this.widgets) {
			widget.renderBg(poseStack, mouseX, mouseY, partialTicks);
		}
	}

	public void renderWidgetLabels(PoseStack poseStack, int mouseX, int mouseY) {
		for(var widget : this.widgets) {
			widget.renderLabels(poseStack,this.font, mouseX, mouseY);
		}
	}

	public void renderWidgetTooltip(PoseStack poseStack, int mouseX, int mouseY ) {
		for(var widget : this.widgets) {
			widget.renderTooltip(poseStack, mouseX, mouseY);
		}
	}


	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.renderWidgetLabels(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		super.renderTooltip(poseStack, mouseX, mouseY);
		this.renderWidgetTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderWidgetBG(poseStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for(var widget : this.widgets) {
			if(widget instanceof IWidgetClickedHandler clickable) {
				if(MathUtil.inBounds(mouseX, mouseY, widget.x, widget.y, widget.width, widget.height)) {
					clickable.onClicked();
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
