package com.lazydragonstudios.wuxiacraft.client.render.renderer;

import com.lazydragonstudios.wuxiacraft.entity.ThrowSkill;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThrowSkillRenderer extends EntityRenderer<ThrowSkill> {

	public ThrowSkillRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(ThrowSkill p_114482_) {
		return null;
	}

	@Override
	public void render(ThrowSkill skillEntity, float unknown, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(skillEntity, unknown, partialTick, poseStack, bufferSource, packedLight);
	}

}
