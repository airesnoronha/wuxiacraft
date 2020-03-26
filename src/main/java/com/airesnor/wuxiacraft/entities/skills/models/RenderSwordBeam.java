package com.airesnor.wuxiacraft.entities.skills.models;

import com.airesnor.wuxiacraft.entities.skills.SwordBeamThrowable;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;

public class RenderSwordBeam<T extends SwordBeamThrowable> extends Render<T> {

	public RenderSwordBeam(RenderManager renderManager) {
		super(renderManager);
		renderOutlines = true;
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();

		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		float pitch = entity.rotationPitch;//entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

		GlStateManager.translate(x,y,z);
		GlStateManager.rotate(entityYaw, 0, 1, 0);
		GlStateManager.rotate(-pitch, 1, 0, 0);
		GlStateManager.rotate(entity.roll, 0, 0, 1);

		Color color = new Color(entity.color);
		float colorEffectR = entity.world.rand.nextFloat() * 0.05f;
		float colorEffectG = entity.world.rand.nextFloat() * 0.05f;
		float colorEffectB = entity.world.rand.nextFloat() * 0.05f;
		float r = MathUtils.clamp((color.getRed()/256f)+colorEffectR, 0f, 1f);
		float g = MathUtils.clamp((color.getGreen()/256f)+colorEffectG, 0f, 1f);
		float b = MathUtils.clamp((color.getBlue()/256f)+colorEffectB, 0f, 1f);

		GlStateManager.color(r,g,b,0.6f);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		for(int i = 0; i < 18; i++) {
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			double dx = Math.sin( i * Math.PI / 16f)*0.05f;
			double dy = 0.5 + Math.cos( i * Math.PI / 16f)*0.5;
			double dz = i*(Math.sin(18f*Math.PI/16f))/16f;
			builder.pos(dx, dy, dz).endVertex();
			dx = 0;
			dz = Math.sin( i * Math.PI / 16f);
			builder.pos(dx, dy, dz).endVertex();
			int j = i+1;
			dx = 0;
			dy = 0.5 + Math.cos( j * Math.PI / 16f)*0.5;
			dz = Math.sin( j * Math.PI / 16f);
			builder.pos(dx, dy, dz).endVertex();
			dx = Math.sin( j * Math.PI / 16f)*0.05f;
			dz = j*(Math.sin(18f*Math.PI/16f))/16f;
			builder.pos(dx, dy, dz).endVertex();
			tessellator.draw();
		}
		GlStateManager.scale(-1,1,1);
		for(int i = 0; i < 18; i++) {
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			double dx = Math.sin( i * Math.PI / 16f)*0.05f;
			double dy = 0.5 + Math.cos( i * Math.PI / 16f)*0.5;
			double dz = i*(Math.sin(18f*Math.PI/16f))/16f;
			builder.pos(dx, dy, dz).endVertex();
			dx = 0;
			dz = Math.sin( i * Math.PI / 16f);
			builder.pos(dx, dy, dz).endVertex();
			int j = i+1;
			dx = 0;
			dy = 0.5 + Math.cos( j * Math.PI / 16f)*0.5;
			dz = Math.sin( j * Math.PI / 16f);
			builder.pos(dx, dy, dz).endVertex();
			dx = Math.sin( j * Math.PI / 16f)*0.05f;
			dz = j*(Math.sin(18f*Math.PI/16f))/16f;
			builder.pos(dx, dy, dz).endVertex();
			tessellator.draw();
		}
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}
}
