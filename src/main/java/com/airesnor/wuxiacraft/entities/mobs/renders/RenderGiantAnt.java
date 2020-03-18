package com.airesnor.wuxiacraft.entities.mobs.renders;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.utils.OBJModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RenderGiantAnt<T extends GiantAnt> extends Render<T> {

	private static final ResourceLocation GIANT_ANT_TEXTURE = new ResourceLocation(WuxiaCraft.MODID, "textures/entities/giant_ant.png");
	private static final ResourceLocation GIANT_ANT_MODEL_LOCATION = new ResourceLocation(WuxiaCraft.MODID, "models/entity/giant_ant.obj");

	private static Map<String, OBJModelLoader.Part>  GIANT_ANT_MODEL;

	public static void init() {
		try {
			GIANT_ANT_MODEL = OBJModelLoader.getPartsFromFile(GIANT_ANT_MODEL_LOCATION);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RenderGiantAnt(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x,y,z);

		GlStateManager.disableDepth();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		GlStateManager.color(1,1,1,1);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		builder.pos(0.5,0,0.5).endVertex();
		builder.pos(0.5,1,0.5).endVertex();
		builder.pos(-0.5,1,-0.5).endVertex();
		builder.pos(-0.5,0,-0.5).endVertex();
		tessellator.draw();

		//Minecraft.getMinecraft().renderEngine.bindTexture(GIANT_ANT_TEXTURE);
		for(Map.Entry<String, OBJModelLoader.Part> p : GIANT_ANT_MODEL.entrySet()) {
			for(OBJModelLoader.Face f : p.getValue().faces) {
				builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
				for(OBJModelLoader.Vertex v : f.vertices) {
					builder.pos(v.x,v.y,v.z).endVertex();
				}
				tessellator.draw();
			}
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();

		//super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return GIANT_ANT_TEXTURE;
	}
}
