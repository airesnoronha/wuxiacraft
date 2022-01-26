package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.cultivation.technique.aspects.TechniqueAspect;
import wuxiacraft.init.WuxiaRegistries;
import wuxiacraft.init.WuxiaTechniqueAspects;

import javax.annotation.Nonnull;

public class WuxiaAspectWidget extends AbstractWidget {

	public ResourceLocation aspect;

	public WuxiaAspectWidget(int x, int y, ResourceLocation aspect) {
		super(x, y, 32, 32, Component.nullToEmpty(aspect.getPath()));
		this.aspect = aspect;
	}

	@Override
	public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderTexture(0, getAspect().textureLocation);
		GuiComponent.blit(poseStack,
				this.x, this.y,
				32, 32,
				0, 0,
				32, 32,
				32, 32
		);
	}

	public TechniqueAspect getAspect() {
		return WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.aspect);
	}

	@Override
	public void updateNarration(@Nonnull NarrationElementOutput p_169152_) {

	}
}
