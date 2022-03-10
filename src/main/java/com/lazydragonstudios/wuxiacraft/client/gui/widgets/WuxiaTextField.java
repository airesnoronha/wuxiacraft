package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.google.common.base.Equivalence;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class WuxiaTextField extends AbstractWidget {

	public EditBox editBox;

	public WuxiaTextField(int x, int y, int width, int height) {
		super(x, y, width, height, new TextComponent(""));
		this.editBox = new EditBox(Minecraft.getInstance().font, x + 5, y + 5, width - 10, height - 10, new TextComponent(""));
		this.editBox.setBordered(false);
	}


	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int texPosX = 30;
		int texPosY = 0;
		int nineSliceHeight = 5;
		int nineSliceWidth = 5;
		if (this.editBox.isFocused()) {
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
				this.x, this.y + this.height - nineSliceHeight - verticalRemainingFillSpace, //position in screen
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
					texPosX + nineSliceWidth * 2, texPosY + nineSliceHeight, // position in texture
					nineSliceWidth, nineSliceHeight, //size in texture
					256, 256); //image size
		}
		GuiComponent.blit(poseStack,
				this.x + this.width - nineSliceWidth, this.y + this.height - nineSliceHeight - verticalRemainingFillSpace, //position in screen
				nineSliceWidth, verticalRemainingFillSpace, // size in screen
				texPosX + nineSliceWidth * 2, texPosY + nineSliceHeight, // position in texture
				nineSliceWidth, verticalRemainingFillSpace, //size in texture
				256, 256); //image size

		//middle
		for (int j = 0; j < verticalFillSteps; j++) {
			int yPos = this.y + nineSliceHeight + j * nineSliceHeight;
			for (int i = 0; i < horizontalFillSteps; i++) {
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
		for (int i = 0; i < horizontalFillSteps; i++) {
			int xPos = this.x + nineSliceWidth + i * nineSliceWidth;
			GuiComponent.blit(poseStack,
					xPos, yPos, //position in screen
					nineSliceWidth, verticalRemainingFillSpace, // size in screen
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
		this.editBox.render(poseStack, mouseX, mouseX, partialTicks);
	}

	@Override
	public boolean changeFocus(boolean focus) {
		return this.editBox.changeFocus(focus);
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (MathUtil.inBounds(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
			if (MathUtil.inBounds(mouseX, mouseY, this.editBox.x, this.editBox.y, this.editBox.getWidth(), this.editBox.getHeight())) {
				return this.editBox.mouseClicked(mouseX, mouseY, button);
			} else {
				return this.editBox.mouseClicked(this.x + 6, this.y + 6, button);
			}
		} else {
			return this.editBox.mouseClicked(this.x, this.y, button);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.editBox.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char character, int keyCode) {
		return this.editBox.charTyped(character, keyCode);
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
		this.editBox.updateNarration(p_169152_);
	}
}
