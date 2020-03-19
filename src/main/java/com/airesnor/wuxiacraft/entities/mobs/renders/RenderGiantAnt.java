package com.airesnor.wuxiacraft.entities.mobs.renders;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.utils.OBJModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenderGiantAnt<T extends GiantAnt> extends Render<T> {

	private static final ResourceLocation GIANT_ANT_TEXTURE = new ResourceLocation(WuxiaCraft.MODID, "textures/entities/giant_ant.png");
	private static final ResourceLocation GIANT_ANT_MODEL_LOCATION = new ResourceLocation(WuxiaCraft.MODID, "models/entity/giant_ant.obj");

	private static Map<String, OBJModelLoader.Part>  GIANT_ANT_MODEL;

	private Map<String, Vector3f> partRotations;

	public static void init() {
		try {
			GIANT_ANT_MODEL = OBJModelLoader.getPartsFromFile(GIANT_ANT_MODEL_LOCATION);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RenderGiantAnt(RenderManager renderManager) {
		super(renderManager);
		this.partRotations = new HashMap<>();
	}

	public void setPartRotations(T entity, float partialTicks) {
		float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
		float swingAmount = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
		float headPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
		float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
		float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
		float headYaw = f1-f;
		float angleLeg1Y = -(MathHelper.cos(swing * 0.6662F * 2.0F + 0.0F) * 0.4F) * swingAmount *60f;
		float angleLeg2Y = -(MathHelper.cos(swing * 0.6662F * 2.0F + (float)Math.PI*2/3F) * 0.4F) * swingAmount*60f;
		float angleLeg3Y = -(MathHelper.cos(swing * 0.6662F * 2.0F + (float)Math.PI*2*2/3F) * 0.4F) * swingAmount*60f;
		float angleLeg1Z = Math.abs(MathHelper.cos(swing * 0.6662F * 2.0F + 0.0F) * 0.4F) * swingAmount*60f;
		float angleLeg2Z = Math.abs(MathHelper.cos(swing * 0.6662F * 2.0F + (float)Math.PI*2/3F) * 0.4F) * swingAmount*60f;
		float angleLeg3Z = Math.abs(MathHelper.cos(swing * 0.6662F * 2.0F + (float)Math.PI*2*2/3F) * 0.4F) * swingAmount*60f;
		this.partRotations.put("Head", new Vector3f(0f-headPitch*0.7f,0f-headYaw*0.7f,0f));
		this.partRotations.put("Chest", new Vector3f(30f,0f,0f));
		this.partRotations.put("Abs", new Vector3f(0f,0f,0f));
		this.partRotations.put("Tail", new Vector3f(0f,0f,0f));
		this.partRotations.put("RAntler", new Vector3f(0f,0f,0f));
		this.partRotations.put("LAntler", new Vector3f(0f,0f,0f));
		this.partRotations.put("RJaw", new Vector3f(0f,0f,0f));
		this.partRotations.put("LJaw", new Vector3f(0f,0f,0f));
		this.partRotations.put("LFLeg", new Vector3f(-30f,-30f+angleLeg1Y,0f+angleLeg1Z));
		this.partRotations.put("LMLeg", new Vector3f(-30f,0f+angleLeg2Y,0f+angleLeg2Z));
		this.partRotations.put("LRLeg", new Vector3f(-30f,0f+angleLeg3Y,20f+angleLeg3Z));
		this.partRotations.put("RFLeg", new Vector3f(-30f,30f-angleLeg1Y,0f+angleLeg1Z));
		this.partRotations.put("RMLeg", new Vector3f(-30f,0f-angleLeg2Y,0f-angleLeg2Z));
		this.partRotations.put("RRLeg", new Vector3f(-30f,0f-angleLeg3Y,-20f-angleLeg3Z));
	}

	protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks)
	{
		float f;

		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F)
		{
			;
		}

		while (f >= 180.0F)
		{
			f -= 360.0F;
		}

		return prevYawOffset + partialTicks * f;
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		this.setPartRotations(entity, partialTicks);

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x,y,z);
		applyRotations(entity, entityYaw, partialTicks);


		GlStateManager.color(1,1,1,1);

		Minecraft.getMinecraft().renderEngine.bindTexture(GIANT_ANT_TEXTURE);
		for(Map.Entry<String, OBJModelLoader.Part> p : GIANT_ANT_MODEL.entrySet()) {
			GlStateManager.pushMatrix();
			applyLocalRotation(p.getValue(), this.partRotations.get(p.getKey()));
			p.getValue().draw();
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	private void applyLocalRotation(OBJModelLoader.Part part, Vector3f rotation) {
		if(!part.parent.equals("")) {
			applyLocalRotation(GIANT_ANT_MODEL.get(part.parent), this.partRotations.get(part.parent));
		}
		if(rotation != null) {
			GlStateManager.translate(part.origin.x, part.origin.y, part.origin.z);
			GlStateManager.rotate(rotation.x, 1, 0, 0);
			GlStateManager.rotate(rotation.y, 0, 1, 0);
			GlStateManager.rotate(rotation.z, 0, 0, 1);
			GlStateManager.translate(-part.origin.x, -part.origin.y, -part.origin.z);
		}
	}

	protected void applyRotations(T entityLiving, float rotationYaw, float partialTicks)
	{
		GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);

		if (entityLiving.deathTime > 0)
		{
			float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);

			if (f > 1.0F)
			{
				f = 1.0F;
			}

			GlStateManager.rotate(f * 180f, 0.0F, 0.0F, 1.0F);
		}
	}



	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return GIANT_ANT_TEXTURE;
	}
}
