package com.airesnor.wuxiacraft.entities.skills.models;

import com.airesnor.wuxiacraft.entities.skills.ThunderBoltThrowable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderThunderBolt<T extends ThunderBoltThrowable> extends Render<T> {

	public RenderThunderBolt(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(-entity.rotationPitch, 1, 0, 0);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();

		GlStateManager.color(0.3f, 0.1f, 0.8f, 0.6f);
		GlStateManager.glLineWidth(12f);

		buf.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		buf.pos(0, 0, 0.0f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.1, entity.world.rand.nextFloat()*0.1, -0.1f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.3, entity.world.rand.nextFloat()*0.3, -0.3f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.5, entity.world.rand.nextFloat()*0.5, -0.5f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.7, entity.world.rand.nextFloat()*0.7, -1f).endVertex();
		tessellator.draw();

		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.glLineWidth(2f);

		buf.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		buf.pos(0, 0, 0.0f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.1, entity.world.rand.nextFloat()*0.1, -0.1f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.3, entity.world.rand.nextFloat()*0.3, -0.3f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.5, entity.world.rand.nextFloat()*0.5, -0.5f).endVertex();
		buf.pos(entity.world.rand.nextFloat()*0.7, entity.world.rand.nextFloat()*0.7, -1f).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}


}
