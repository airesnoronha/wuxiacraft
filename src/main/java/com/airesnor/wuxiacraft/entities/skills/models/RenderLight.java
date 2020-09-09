package com.airesnor.wuxiacraft.entities.skills.models;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.skills.LightThrowable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RenderLight<T extends LightThrowable> extends Render<T> {

	public RenderLight(RenderManager renderManager) {
		super(renderManager);
	}

	private static final ResourceLocation TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/light_block.png");

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		bindEntityTexture(entity);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(45, 0, 1, 0);
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(-0.15, 0, 0).tex(0, 0).endVertex();
		builder.pos(-0.15, 0.3, 0).tex(0, 1).endVertex();
		builder.pos(0.15, 0.3, 0).tex(1, 1).endVertex();
		builder.pos(0.15, 0, 0).tex(1, 0).endVertex();

		builder.pos(0, 0, -0.15).tex(0, 0).endVertex();
		builder.pos(0, 0.3, -0.15).tex(0, 1).endVertex();
		builder.pos(0, 0.3, 0.15).tex(1, 1).endVertex();
		builder.pos(0, 0, 0.15).tex(1, 0).endVertex();
		tessellator.draw();

		GlStateManager.enableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return TEXTURE;
	}
}
