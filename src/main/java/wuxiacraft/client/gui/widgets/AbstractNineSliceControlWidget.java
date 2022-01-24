package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public abstract class AbstractNineSliceControlWidget extends AbstractControlWidget {

	public int nineSliceWidth;
	public int nineSliceHeight;

	public AbstractNineSliceControlWidget(int x, int y, int width, int height, int texPosX, int texPosY, int nineSliceWidth, int nineSliceHeight) {
		super(x, y, width, height, texPosX, texPosY);
		this.nineSliceWidth = nineSliceWidth;
		this.nineSliceHeight = nineSliceHeight;
	}

	@Override
	public void renderBg(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		//corners first
		//top left
		GuiComponent.blit(poseStack,
				this.x, this.y, //position in screen
				this.nineSliceWidth, this.nineSliceHeight, // size in screen
				this.texPosX, this.texPosY, // position in texture
				this.nineSliceWidth, this.nineSliceHeight, //size in texture
				256, 256); //image size
		//top right
		GuiComponent.blit(poseStack,
				this.x + this.width - this.nineSliceWidth, this.y, //position in screen
				this.nineSliceWidth, this.nineSliceHeight, // size in screen
				this.texPosX + this.nineSliceWidth * 2, this.texPosY, // position in texture
				this.nineSliceWidth, this.nineSliceHeight, //size in texture
				256, 256); //image size
		//bottom left
		GuiComponent.blit(poseStack,
				this.x, this.y + this.height - this.nineSliceHeight, //position in screen
				this.nineSliceWidth, this.nineSliceHeight, // size in screen
				this.texPosX, this.texPosY + this.nineSliceHeight * 2, // position in texture
				this.nineSliceWidth, this.nineSliceHeight, //size in texture
				256, 256); //image size
		//bottom right
		GuiComponent.blit(poseStack,
				this.x + this.width - this.nineSliceWidth, this.y + this.height - this.nineSliceHeight, //position in screen
				this.nineSliceWidth, this.nineSliceHeight, // size in screen
				this.texPosX + this.nineSliceWidth * 2, this.texPosY + this.nineSliceHeight * 2, // position in texture
				this.nineSliceWidth, this.nineSliceHeight, //size in texture
				256, 256); //image size

		//fillings
		int remainingFillWidth = this.width - this.nineSliceWidth * 2;
		int remainingFillHeight = this.height - this.nineSliceHeight * 2;
		int horizontalFillSteps = remainingFillWidth / this.nineSliceWidth;
		int horizontalRemainingFillSpace = remainingFillWidth % this.nineSliceWidth;
		int verticalFillSteps = remainingFillHeight / this.nineSliceHeight;
		int verticalRemainingFillSpace = remainingFillHeight % this.nineSliceHeight;
		//borders
		//top
		for (int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + this.nineSliceWidth + i * this.nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, this.y, //position in screen
					this.nineSliceWidth, this.nineSliceHeight, // size in screen
					this.texPosX + this.nineSliceWidth, this.texPosY, // position in texture
					this.nineSliceWidth, this.nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - this.nineSliceWidth - horizontalRemainingFillSpace, this.y, //position in screen
				horizontalRemainingFillSpace, this.nineSliceHeight, // size in screen
				this.texPosX + this.nineSliceWidth, this.texPosY, // position in texture
				horizontalRemainingFillSpace, this.nineSliceHeight, //size in texture
				256, 256); //image size
		//bottom
		for (int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + this.nineSliceWidth + i * this.nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, this.y + this.height - this.nineSliceHeight, //position in screen
					this.nineSliceWidth, this.nineSliceHeight, // size in screen
					this.texPosX + this.nineSliceWidth, this.texPosY + this.nineSliceHeight * 2, // position in texture
					this.nineSliceWidth, this.nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - this.nineSliceWidth - horizontalRemainingFillSpace, this.y + this.height - this.nineSliceHeight, //position in screen
				horizontalRemainingFillSpace, this.nineSliceHeight, // size in screen
				this.texPosX + this.nineSliceWidth, this.texPosY + this.nineSliceHeight * 2, // position in texture
				horizontalRemainingFillSpace, this.nineSliceHeight, //size in texture
				256, 256); //image size
		//left
		for (int i = 0; i < verticalFillSteps; i++) {
			int yPos = this.y + this.nineSliceHeight + i * this.nineSliceHeight;
			GuiComponent.blit(poseStack,
					this.x, yPos, //position in screen
					this.nineSliceWidth, this.nineSliceHeight, // size in screen
					this.texPosX, this.texPosY + this.nineSliceHeight, // position in texture
					this.nineSliceWidth, this.nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x, this.y + this.height -this.nineSliceHeight-verticalRemainingFillSpace, //position in screen
				this.nineSliceWidth, verticalRemainingFillSpace, // size in screen
				this.texPosX, this.texPosY + this.nineSliceHeight, // position in texture
				this.nineSliceWidth, verticalRemainingFillSpace, //size in texture
				256, 256); //image size
		//right
		for (int i = 0; i < verticalFillSteps; i++) {
			int yPos = this.y + this.nineSliceHeight + i * this.nineSliceHeight;
			GuiComponent.blit(poseStack,
					this.x + this.width - this.nineSliceWidth, yPos, //position in screen
					this.nineSliceWidth, this.nineSliceHeight, // size in screen
					this.texPosX + this.nineSliceWidth*2, this.texPosY + this.nineSliceHeight, // position in texture
					this.nineSliceWidth, this.nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - this.nineSliceWidth, this.y + this.height -this.nineSliceHeight - verticalRemainingFillSpace, //position in screen
				this.nineSliceWidth, verticalRemainingFillSpace, // size in screen
				this.texPosX + this.nineSliceWidth*2, this.texPosY + this.nineSliceHeight, // position in texture
				this.nineSliceWidth, verticalRemainingFillSpace, //size in texture
				256, 256); //image size

		//middle
		for(int j = 0; j < verticalFillSteps; j++) {
			int yPos = this.y + this.nineSliceHeight + j * this.nineSliceHeight;
			for(int i = 0; i < horizontalFillSteps; i++) {
				int xPos = this.x + this.nineSliceWidth + i * this.nineSliceWidth;
				GuiComponent.blit(poseStack,
						xPos, yPos, //position in screen
						this.nineSliceWidth, this.nineSliceHeight, // size in screen
						this.texPosX + this.nineSliceWidth, this.texPosY + this.nineSliceHeight, // position in texture
						this.nineSliceWidth, this.nineSliceHeight, //size in texture
						256, 256); //image size
			}
			GuiComponent.blit(poseStack,
					this.x + this.width - this.nineSliceWidth - horizontalRemainingFillSpace, yPos, //position in screen
					horizontalRemainingFillSpace, this.nineSliceHeight, // size in screen
					this.texPosX + this.nineSliceWidth, this.texPosY + this.nineSliceHeight, // position in texture
					horizontalRemainingFillSpace, this.nineSliceHeight, //size in texture
					256, 256); //image size
		}
		int yPos = this.y + this.height - this.nineSliceHeight - verticalRemainingFillSpace;
		for(int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + this.nineSliceWidth + i * this.nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, yPos, //position in screen
					this.nineSliceWidth,verticalRemainingFillSpace, // size in screen
					this.texPosX + this.nineSliceWidth, this.texPosY + this.nineSliceHeight, // position in texture
					this.nineSliceWidth, verticalRemainingFillSpace, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - this.nineSliceWidth - horizontalRemainingFillSpace, yPos, //position in screen
				horizontalRemainingFillSpace, verticalRemainingFillSpace, // size in screen
				this.texPosX + this.nineSliceWidth, this.texPosY + this.nineSliceHeight, // position in texture
				horizontalRemainingFillSpace, verticalRemainingFillSpace, //size in texture
				256, 256); //image size
	}
}
