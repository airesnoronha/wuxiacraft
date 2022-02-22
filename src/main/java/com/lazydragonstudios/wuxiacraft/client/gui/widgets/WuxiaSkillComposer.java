package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillDescriptor;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WuxiaSkillComposer extends AbstractWidget {

	public MouseInputPredicate onClick;
	public MouseInputPredicate onDrag;
	public MouseInputPredicate onRelease;

	public BiConsumer<Integer, Integer> onResize;

	private SkillDescriptor skill;

	public WuxiaSkillComposer(int x, int y, Component message, SkillDescriptor descriptor) {
		super(x, y, 40, 100, message);
		this.onClick = (mx, my, b) -> false;
		this.onDrag = (mx, my, b) -> false;
		this.onRelease = (mx, my, b) -> false;
		this.onResize = (sx, sy) -> {
		};
		this.skill = descriptor;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int yIndex = 0;
		//the skills in the chain
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
		RenderSystem.enableBlend();
		for (var link : this.skill.getSkillChain()) {
			int yPos = yIndex * 40 + 20;
			int xPos = 20;
			int texX = 116;
			if (MathUtil.inBounds(mouseX, mouseY, xPos - 19, yPos - 19, 38, 38)) {
				texX = 154;
			}
			RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
			poseStack.pushPose();
			poseStack.translate(xPos, yPos, 0f);
			GuiComponent.blit(poseStack,
					-19, -19,
					38, 38,
					texX, 0,
					38, 38,
					256, 256);
			//render the aspect
			var aspectTypeLocation = link.getType().getRegistryName();
			var aspectTextureLocation = new ResourceLocation(aspectTypeLocation.getNamespace(), "textures/skills/" + aspectTypeLocation.getPath() + ".png");
			RenderSystem.setShaderTexture(0, aspectTextureLocation);
			GuiComponent.blit(poseStack,
					-16, -16,
					32, 32,
					0, 0,
					32, 32,
					32, 32);
			poseStack.popPose();
			yIndex++;
		}
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
		//a blank link in the chain
		int yPos = yIndex * 40 + 20;
		int xPos = 20;
		int texX = 116;
		if (MathUtil.inBounds(mouseX, mouseY, xPos - 19, yPos - 19, 38, 38)) {
			texX = 154;
		}
		poseStack.pushPose();
		poseStack.translate(xPos, yPos, 0f);
		GuiComponent.blit(poseStack,
				-19, -19,
				38, 38,
				texX, 0,
				38, 38,
				256, 256);
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	public int getLinkAtPosition(double mouseX, double mouseY) {
		int size = this.skill.getSkillChain().size();
		size++; //the last blank link
		for (int i = 0; i < size; i++) {
			int yPos = i * 40 + 20;
			int xPos = 20;
			if (MathUtil.inBounds(mouseX, mouseY, xPos - 19, yPos - 19, 38, 38)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.onClick.apply(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return this.onRelease.apply(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDeltaX, double mouseDeltaY) {
		return this.onDrag.apply(mouseX, mouseY, button);
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}

	public void addSkillToPosition(ResourceLocation aspectLocation, int pos) {
		var skillAspectType = WuxiaRegistries.SKILL_ASPECT.getValue(aspectLocation);
		if (skillAspectType == null) return;
		var skillAspect = skillAspectType.creator.create();
		this.skill.insertSkillAt(pos, skillAspect);
	}

	public void removeSkillAtPosition(int pos) {
		this.skill.getSkillChain().remove(pos);
	}

	public SkillDescriptor getSkill() {
		return skill;
	}
}
