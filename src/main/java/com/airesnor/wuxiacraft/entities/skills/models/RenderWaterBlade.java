package com.airesnor.wuxiacraft.entities.skills.models;

import com.airesnor.wuxiacraft.entities.skills.WaterBladeThrowable;
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
import java.util.Random;

@ParametersAreNonnullByDefault
public class RenderWaterBlade<T extends WaterBladeThrowable> extends Render<T> {

	public RenderWaterBlade(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		Random rand = entity.world.rand;
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.color(0.2f, 0.3f, 0.8f, 1f);

		float roll = entity.prevRotationRoll + (entity.rotationRoll - entity.prevRotationRoll) * partialTicks;

		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
		GlStateManager.rotate(-entity.rotationPitch, 1, 0, 0);
		GlStateManager.rotate(roll, 0, 0, 1);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
		for (int i = 0; i <= 16; i++) {
			float angle = i * 180f / 16f;
			buf.pos(Math.cos(angle), rand.nextFloat() * 0.05f, 0.1f * Math.sin(angle)).endVertex();
		}
		tessellator.draw();

		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}


}
