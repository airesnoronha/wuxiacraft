package wuxiacraft.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.container.IntrospectionContainer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IntrospectionScreen extends ContainerScreen<IntrospectionContainer> {

	private static ResourceLocation CULT_GUI = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/cult_gui.png");

	public IntrospectionScreen(IntrospectionContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
		if (this.minecraft == null) return;
		if (this.minecraft.player == null) return;
		stack.push();
		this.renderBackground(stack);
		stack.translate(this.guiLeft, this.guiTop, 0);

		stack.pop();
	}
}
