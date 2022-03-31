package com.lazydragonstudios.wuxiacraft.client.render.renderer;

import com.lazydragonstudios.wuxiacraft.client.render.WuxiaRenderTypes;
import com.lazydragonstudios.wuxiacraft.entity.ThrowSkill;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
		var consumer = bufferSource.getBuffer(WuxiaRenderTypes.getTranslucentNoTexture.get());
		poseStack.pushPose();
		poseStack.translate(0,0.3,0);
		renderUVSphere(poseStack, consumer, 0.24d, 0xEF403005);
		renderUVSphere(poseStack, consumer, 0.3d, 0x7CFFDD10);
		poseStack.popPose();
	}

	private static void renderUVSphere(PoseStack poseStack, VertexConsumer vertexConsumer, double radius, int color) {
		int verticalSections = 16;
		int horizontalSections = 32;
		for (int i = 1; i < verticalSections - 1; i++) {
			double y0 = Math.cos(Math.PI * (double) i / (double) verticalSections);
			double y0Radius = radius * Math.sin(Math.PI * (double) i / (double) verticalSections);
			double y1 = Math.cos(Math.PI * (double) (i + 1) / (double) verticalSections);
			double y1Radius = radius * Math.sin(Math.PI * (double) (i + 1) / (double) verticalSections);
			for (int j = 0; j < horizontalSections; j++) {
				double x0Pos = Math.cos(2 * Math.PI * (double) (j) / (double) horizontalSections);
				double x1Pos = Math.cos(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
				double z0Pos = Math.sin(2 * Math.PI * (double) (j) / (double) horizontalSections);
				double z1Pos = Math.sin(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
				Vector3f[] vertices = new Vector3f[]{
						new Vector3f((float) (x0Pos * y0Radius), (float) (y0 * radius), (float) (z0Pos * y0Radius)),
						new Vector3f((float) (x1Pos * y0Radius), (float) (y0 * radius), (float) (z1Pos * y0Radius)),
						new Vector3f((float) (x1Pos * y1Radius), (float) (y1 * radius), (float) (z1Pos * y1Radius)),
						new Vector3f((float) (x0Pos * y1Radius), (float) (y1 * radius), (float) (z0Pos * y1Radius)),
				};
				for (var vertex : vertices) {
					vertexConsumer.vertex(poseStack.last().pose(), vertex.x(), vertex.y(), vertex.z()).color(color).endVertex();
				}
			}
		}
		double y0 = Math.cos(Math.PI / (double) verticalSections);
		double y0Radius = radius * Math.sin(Math.PI / (double) verticalSections);
		double y1 = Math.cos(Math.PI * (double) (verticalSections - 1) / (double) verticalSections);
		double y1Radius = radius * Math.sin(Math.PI * (double) (verticalSections - 1) / (double) verticalSections);
		for(int j = 0; j < horizontalSections; j+=2) {
			double x0Pos = Math.cos(2 * Math.PI * (double) (j) / (double) horizontalSections);
			double z0Pos = Math.sin(2 * Math.PI * (double) (j) / (double) horizontalSections);
			double x1Pos = Math.cos(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
			double z1Pos = Math.sin(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
			double x2Pos = Math.cos(2 * Math.PI * (double) (j + 2) / (double) horizontalSections);
			double z2Pos = Math.sin(2 * Math.PI * (double) (j + 2) / (double) horizontalSections);
			Vector3f[] vertices = new Vector3f[]{
					new Vector3f((float) (x2Pos * y0Radius), (float) (y0 * radius), (float) (z2Pos * y0Radius)),
					new Vector3f((float) (x1Pos * y0Radius), (float) (y0 * radius), (float) (z1Pos * y0Radius)),
					new Vector3f((float) (x0Pos * y0Radius), (float) (y0 * radius), (float) (z0Pos * y0Radius)),
					new Vector3f((float) 0, (float) radius, (float) 0),
			};
			for (var vertex : vertices) {
				vertexConsumer.vertex(poseStack.last().pose(), vertex.x(), vertex.y(), vertex.z()).color(color).endVertex();
			}
			vertices = new Vector3f[]{
					new Vector3f((float) 0, (float) -radius, (float) 0),
					new Vector3f((float) (x0Pos * y1Radius), (float) (y1 * radius), (float) (z0Pos * y1Radius)),
					new Vector3f((float) (x1Pos * y1Radius), (float) (y1 * radius), (float) (z1Pos * y1Radius)),
					new Vector3f((float) (x2Pos * y1Radius), (float) (y1 * radius), (float) (z2Pos * y1Radius)),
			};
			for (var vertex : vertices) {
				vertexConsumer.vertex(poseStack.last().pose(), vertex.x(), vertex.y(), vertex.z()).color(color).endVertex();
			}
		}
	}

}
