package com.airesnor.wuxiacraft.entities.skills.models;

import com.airesnor.wuxiacraft.entities.skills.WaterNeedleThrowable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderWaterNeedle<T extends WaterNeedleThrowable> extends Render<T> {

    public RenderWaterNeedle(RenderManager renderManager){
        super(renderManager);
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.color(0.2f,0.3f,0.8f, 1f);

        GlStateManager.translate(x,y,z);
        GlStateManager.rotate(entity.rotationYaw, 0,1,0);
        GlStateManager.rotate(-entity.rotationPitch, 1,0,0);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buf = tessellator.getBuffer();
        buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
        buf.pos(0.025f, 0.025f, 0f).endVertex();
        buf.pos(0.0f, 0.0f, -0.1f).endVertex();
        buf.pos(0.0f, 0.0f, -0.3f).endVertex();
        buf.pos(0.05f, 0.05f, -0.3f).endVertex();
        buf.pos(0.05f, 0.05f, -0.1f).endVertex();
        tessellator.draw();
        buf.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
        buf.pos(0.025f, 0.025f, -0f).endVertex();
        buf.pos(0.05f, 0f, -0.1f).endVertex();
        buf.pos(0.05f, 0f, -0.3f).endVertex();
        buf.pos(0.0f, 0.05f, -0.3f).endVertex();
        buf.pos(0.0f, 0.05f, -0.1f).endVertex();
        tessellator.draw();

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
