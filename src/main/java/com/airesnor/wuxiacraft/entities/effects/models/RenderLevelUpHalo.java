package com.airesnor.wuxiacraft.entities.effects.models;

import com.airesnor.wuxiacraft.entities.effects.EntityLevelUpHalo;
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
public class RenderLevelUpHalo extends Render<EntityLevelUpHalo> {

	public RenderLevelUpHalo(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityLevelUpHalo entity, double x, double y, double z, float entityYaw, float partialTicks) {

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.color(0.9f, 0.9f, 0.6f, 0.4f);

		GlStateManager.translate(x, y, z);
		GlStateManager.scale((entity.ticksExisted + partialTicks)*3, 1, (entity.ticksExisted + partialTicks)*3);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		//4 walls around position
		buf.pos(-0.9f, -30f,-0.9f).endVertex();
		buf.pos(-0.9f, -30f, 0.9f).endVertex();
		buf.pos(-0.9f, 100f, 0.9f).endVertex();
		buf.pos(-0.9f, 100f,-0.9f).endVertex();

		buf.pos( 0.9f, -30f,-0.9f).endVertex();
		buf.pos( 0.9f, -30f, 0.9f).endVertex();
		buf.pos( 0.9f, 100f, 0.9f).endVertex();
		buf.pos( 0.9f, 100f,-0.9f).endVertex();

		buf.pos( 0.9f, -30f,-0.9f).endVertex();
		buf.pos(-0.9f, -30f,-0.9f).endVertex();
		buf.pos(-0.9f, 100f,-0.9f).endVertex();
		buf.pos( 0.9f, 100f,-0.9f).endVertex();

		buf.pos( 0.9f, -30f, 0.9f).endVertex();
		buf.pos(-0.9f, -30f, 0.9f).endVertex();
		buf.pos(-0.9f, 100f, 0.9f).endVertex();
		buf.pos( 0.9f, 100f, 0.9f).endVertex();
		tessellator.draw();

		GlStateManager.color(0.9f, 0.9f, 0.2f, 0.4f);
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		buf.pos(-0.1f, -30f,-0.1f).endVertex();
		buf.pos(-0.1f, -30f, 0.1f).endVertex();
		buf.pos(-0.1f, 100f, 0.1f).endVertex();
		buf.pos(-0.1f, 100f,-0.1f).endVertex();

		buf.pos( 0.1f, -30f,-0.1f).endVertex();
		buf.pos( 0.1f, -30f, 0.1f).endVertex();
		buf.pos( 0.1f, 100f, 0.1f).endVertex();
		buf.pos( 0.1f, 100f,-0.1f).endVertex();

		buf.pos( 0.1f, -30f,-0.1f).endVertex();
		buf.pos(-0.1f, -30f,-0.1f).endVertex();
		buf.pos(-0.1f, 100f,-0.1f).endVertex();
		buf.pos( 0.1f, 100f,-0.1f).endVertex();

		buf.pos( 0.1f, -30f, 0.1f).endVertex();
		buf.pos(-0.1f, -30f, 0.1f).endVertex();
		buf.pos(-0.1f, 100f, 0.1f).endVertex();
		buf.pos( 0.1f, 100f, 0.1f).endVertex();
		tessellator.draw();

		GlStateManager.color(0.9f, 0.9f, 0.4f, 0.4f);
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		buf.pos(-0.2f, -30f,-0.2f).endVertex();
		buf.pos(-0.2f, -30f, 0.2f).endVertex();
		buf.pos(-0.2f, 100f, 0.2f).endVertex();
		buf.pos(-0.2f, 100f,-0.2f).endVertex();

		buf.pos( 0.2f, -30f,-0.2f).endVertex();
		buf.pos( 0.2f, -30f, 0.2f).endVertex();
		buf.pos( 0.2f, 100f, 0.2f).endVertex();
		buf.pos( 0.2f, 100f,-0.2f).endVertex();

		buf.pos( 0.2f, -30f,-0.2f).endVertex();
		buf.pos(-0.2f, -30f,-0.2f).endVertex();
		buf.pos(-0.2f, 100f,-0.2f).endVertex();
		buf.pos( 0.2f, 100f,-0.2f).endVertex();

		buf.pos( 0.2f, -30f, 0.2f).endVertex();
		buf.pos(-0.2f, -30f, 0.2f).endVertex();
		buf.pos(-0.2f, 100f, 0.2f).endVertex();
		buf.pos( 0.2f, 100f, 0.2f).endVertex();
		tessellator.draw();
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityLevelUpHalo entity) {
		return null;
	}


}
