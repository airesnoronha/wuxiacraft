package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class WuxiaTextField extends AbstractWidget {

	public String value;

	public int caretPosition;

	private Consumer<String> textChanged;

	//TODO Figure out focus logic to apply to TextBox, there is logic for this and has to do with changeFocus

	public WuxiaTextField(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
		textChanged = s -> {};
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		setFocused(this.clicked(mouseX, mouseY));
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int texPosX = 30;
		int texPosY = 0;
		int nineSliceHeight = 5;
		int nineSliceWidth = 5;
		if(this.isFocused()) {
			texPosX = 45;
		}
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
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
		RenderSystem.disableBlend();
		var font = Minecraft.getInstance().font;
		//GuiComponent.drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2+1, this.y + (this.height - font.lineHeight) / 2+1, 0x101010);
		GuiComponent.drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - font.lineHeight) / 2, 0xFFFFFF);
	}

	@Override
	public boolean keyPressed(int p_94745_, int p_94746_, int p_94747_) {
		return false;
	}

	@Override
	public boolean keyReleased(int p_94750_, int p_94751_, int p_94752_) {
		return false;
	}

	public void setTextChanged(Consumer<String> textChanged) {
		this.textChanged = textChanged;
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {

	}
}
