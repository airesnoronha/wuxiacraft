package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.util.MathUtil;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WuxiaButton extends AbstractButton {

	public static final ResourceLocation UI_CONTROLS = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/ui_controls.png");
	public Runnable onClicked;

	public WuxiaButton(int x, int y, int width, int height, Component title, Runnable onClicked) {
		super(x, y, width, height, title);
		this.onClicked = onClicked;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int texPosX = 0;
		int texPosY = 0;
		int nineSliceHeight = 5;
		int nineSliceWidth = 5;
		if(MathUtil.inBounds(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
			texPosX = 15;
		}
		RenderSystem.setShaderTexture(0, UI_CONTROLS);
		//corners first
		//top left
		GuiComponent.blit(poseStack,
				this.x, this.y, //position in screen
				nineSliceWidth, nineSliceHeight, // size in screen
				texPosX, texPosY, // position in texture
				nineSliceWidth, nineSliceHeight, //size in texture
				256, 256); //image size
		//top right
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth, this.y, //position in screen
				nineSliceWidth, nineSliceHeight, // size in screen
				texPosX + nineSliceWidth * 2, texPosY, // position in texture
				nineSliceWidth, nineSliceHeight, //size in texture
				256, 256); //image size
		//bottom left
		GuiComponent.blit(poseStack,
				this.x, this.y + this.height - nineSliceHeight, //position in screen
				nineSliceWidth, nineSliceHeight, // size in screen
				texPosX, texPosY + nineSliceHeight * 2, // position in texture
				nineSliceWidth, nineSliceHeight, //size in texture
				256, 256); //image size
		//bottom right
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth, this.y + this.height - nineSliceHeight, //position in screen
				nineSliceWidth, nineSliceHeight, // size in screen
				texPosX + nineSliceWidth * 2, texPosY + nineSliceHeight * 2, // position in texture
				nineSliceWidth, nineSliceHeight, //size in texture
				256, 256); //image size

		//fillings
		int remainingFillWidth = this.width - nineSliceWidth * 2;
		int remainingFillHeight = this.height - nineSliceHeight * 2;
		int horizontalFillSteps = remainingFillWidth / nineSliceWidth;
		int horizontalRemainingFillSpace = remainingFillWidth % nineSliceWidth;
		int verticalFillSteps = remainingFillHeight / nineSliceHeight;
		int verticalRemainingFillSpace = remainingFillHeight % nineSliceHeight;
		//borders
		//top
		for (int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + nineSliceWidth + i * nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, this.y, //position in screen
					nineSliceWidth, nineSliceHeight, // size in screen
					texPosX + nineSliceWidth, texPosY, // position in texture
					nineSliceWidth, nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth - horizontalRemainingFillSpace, this.y, //position in screen
				horizontalRemainingFillSpace, nineSliceHeight, // size in screen
				texPosX + nineSliceWidth, texPosY, // position in texture
				horizontalRemainingFillSpace, nineSliceHeight, //size in texture
				256, 256); //image size
		//bottom
		for (int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + nineSliceWidth + i * nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, this.y + this.height - nineSliceHeight, //position in screen
					nineSliceWidth, nineSliceHeight, // size in screen
					texPosX + nineSliceWidth, texPosY + nineSliceHeight * 2, // position in texture
					nineSliceWidth, nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth - horizontalRemainingFillSpace, this.y + this.height - nineSliceHeight, //position in screen
				horizontalRemainingFillSpace, nineSliceHeight, // size in screen
				texPosX + nineSliceWidth, texPosY + nineSliceHeight * 2, // position in texture
				horizontalRemainingFillSpace, nineSliceHeight, //size in texture
				256, 256); //image size
		//left
		for (int i = 0; i < verticalFillSteps; i++) {
			int yPos = this.y + nineSliceHeight + i * nineSliceHeight;
			GuiComponent.blit(poseStack,
					this.x, yPos, //position in screen
					nineSliceWidth, nineSliceHeight, // size in screen
					texPosX, texPosY + nineSliceHeight, // position in texture
					nineSliceWidth, nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x, this.y + this.height - nineSliceHeight -verticalRemainingFillSpace, //position in screen
				nineSliceWidth, verticalRemainingFillSpace, // size in screen
				texPosX, texPosY + nineSliceHeight, // position in texture
				nineSliceWidth, verticalRemainingFillSpace, //size in texture
				256, 256); //image size
		//right
		for (int i = 0; i < verticalFillSteps; i++) {
			int yPos = this.y + nineSliceHeight + i * nineSliceHeight;
			GuiComponent.blit(poseStack,
					this.x + this.width - nineSliceWidth, yPos, //position in screen
					nineSliceWidth, nineSliceHeight, // size in screen
					texPosX + nineSliceWidth *2, texPosY + nineSliceHeight, // position in texture
					nineSliceWidth, nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth, this.y + this.height - nineSliceHeight - verticalRemainingFillSpace, //position in screen
				nineSliceWidth, verticalRemainingFillSpace, // size in screen
				texPosX + nineSliceWidth *2, texPosY + nineSliceHeight, // position in texture
				nineSliceWidth, verticalRemainingFillSpace, //size in texture
				256, 256); //image size

		//middle
		for(int j = 0; j < verticalFillSteps; j++) {
			int yPos = this.y + nineSliceHeight + j * nineSliceHeight;
			for(int i = 0; i < horizontalFillSteps; i++) {
				int xPos = this.x + nineSliceWidth + i * nineSliceWidth;
				GuiComponent.blit(poseStack,
						xPos, yPos, //position in screen
						nineSliceWidth, nineSliceHeight, // size in screen
						texPosX + nineSliceWidth, texPosY + nineSliceHeight, // position in texture
						nineSliceWidth, nineSliceHeight, //size in texture
						256, 256); //image size
			}
			GuiComponent.blit(poseStack,
					this.x + this.width - nineSliceWidth - horizontalRemainingFillSpace, yPos, //position in screen
					horizontalRemainingFillSpace, nineSliceHeight, // size in screen
					texPosX + nineSliceWidth, texPosY + nineSliceHeight, // position in texture
					horizontalRemainingFillSpace, nineSliceHeight, //size in texture
					256, 256); //image size
		}
		int yPos = this.y + this.height - nineSliceHeight - verticalRemainingFillSpace;
		for(int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + nineSliceWidth + i * nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, yPos, //position in screen
					nineSliceWidth,verticalRemainingFillSpace, // size in screen
					texPosX + nineSliceWidth, texPosY + nineSliceHeight, // position in texture
					nineSliceWidth, verticalRemainingFillSpace, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth - horizontalRemainingFillSpace, yPos, //position in screen
				horizontalRemainingFillSpace, verticalRemainingFillSpace, // size in screen
				texPosX + nineSliceWidth, texPosY + nineSliceHeight, // position in texture
				horizontalRemainingFillSpace, verticalRemainingFillSpace, //size in texture
				256, 256); //image size

		var font = Minecraft.getInstance().font;
		//GuiComponent.drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2+1, this.y + (this.height - font.lineHeight) / 2+1, 0x101010);
		GuiComponent.drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - font.lineHeight) / 2, 0xFFFFFF);
	}

	@Override
	public void onPress() {
		this.onClicked.run();
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}
}
