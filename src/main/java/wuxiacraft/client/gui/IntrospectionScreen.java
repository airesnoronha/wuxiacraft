package wuxiacraft.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.widgets.WuxiaButton;
import wuxiacraft.container.IntrospectionMenu;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IntrospectionScreen extends AbstractContainerScreen<IntrospectionMenu> {

	public static final ResourceLocation tab_icons = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/tab_icons.png");

	public static final ResourceLocation ui_controls = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/ui_controls.png");


	public IntrospectionScreen(IntrospectionMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.addRenderableWidget(new WuxiaButton(10, 10, 100, 30, new TextComponent("hopefully you noticed me!"), () -> {}));
	}

	@Override
	protected void renderBg(PoseStack poseStack, float mouseX, int mouseY, int something) {
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		super.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		super.renderLabels(poseStack, mouseX, mouseY);
	}
}
