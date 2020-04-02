package com.airesnor.wuxiacraft.entities.mobs.renders;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.mobs.GiantBee;
import com.airesnor.wuxiacraft.utils.OBJModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RenderGiantBee extends Render<GiantBee> {

	public static final ResourceLocation BEE_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/entities/giant_bee.png");

	public static final ResourceLocation BEE_MODEL_LOCATION = new ResourceLocation(WuxiaCraft.MOD_ID, "models/entity/giant_bee.obj");

	private static Map<String, OBJModelLoader.Part>  GIANT_BEE_MODEL;

	private Map<String, Vector3f> partRotations;

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(GiantBee entity) {
		return BEE_TEXTURE;
	}

	public RenderGiantBee(RenderManager renderManager) {
		super(renderManager);
		this.partRotations = new HashMap<>();
	}

	public static void init () {
		try {
			GIANT_BEE_MODEL = OBJModelLoader.getPartsFromFile(BEE_MODEL_LOCATION);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doRender(GiantBee entity, double x, double y, double z, float entityYaw, float partialTicks) {

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		this.setPartRotations(entity, partialTicks);

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x,y,z);
		applyRotations(entity, entityYaw, partialTicks);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);


		GlStateManager.color(1,1,1,1);

		//Apply a model correction here
		//GlStateManager.translate(0,0.376,0);

		Minecraft.getMinecraft().renderEngine.bindTexture(BEE_TEXTURE);
		for(Map.Entry<String, OBJModelLoader.Part> p : GIANT_BEE_MODEL.entrySet()) {
			GlStateManager.pushMatrix();
			applyLocalRotation(p.getValue(), this.partRotations.get(p.getKey()));
			p.getValue().draw();
			GlStateManager.popMatrix();
		}
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	protected void setPartRotations(GiantBee entity, float partialTicks) {
		float swing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
		float swingAmount = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
		float headPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
		float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
		float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
		float headYaw = f1-f;
		float swingAngle = swing * 0.6662F * 2.0F;
		float angleWingY = -(MathHelper.cos(swingAngle*6f) * 0.4F) * swingAmount *60f;
		float angleWingX = Math.abs(MathHelper.cos(swingAngle*6f) * 0.4F) * swingAmount*180f;
		float legsAngleZ = 0f;
		float tailAngleX = 0f;
		float leg1Z = 0;
		float leg2Z = 0;
		float leg3Z = 0;
		float leg1Y = 0;
		float leg2Y = 0;
		float leg3Y = 0;
		if(entity.onGround) {
			angleWingX += 45;
			tailAngleX = -30f;
			legsAngleZ = 30f;
			leg1Z = Math.abs(MathHelper.cos(swingAngle) * 0.4F) * swingAmount*60f;
			leg2Z = Math.abs(MathHelper.cos(swingAngle + (float)Math.PI/3f) * 0.4F) * swingAmount*60f;
			leg3Z = Math.abs(MathHelper.cos(swingAngle + (float)Math.PI*2f/3f) * 0.4F) * swingAmount*60f;
			leg1Y =  -(MathHelper.cos(swingAngle) * 0.4F) * swingAmount *60f;
			leg2Y =  -(MathHelper.cos(swingAngle + (float)Math.PI/3f) * 0.4F) * swingAmount *60f;
			leg3Y =  -(MathHelper.cos(swingAngle + (float)Math.PI*2f/3f) * 0.4F) * swingAmount *60f;
		}
		this.partRotations.put("Body", new Vector3f(0,0,0));
		this.partRotations.put("Head", new Vector3f(0f-headPitch*0.7f,0f-headYaw*0.7f,0));
		this.partRotations.put("Tail", new Vector3f(tailAngleX,0,0));
		this.partRotations.put("LeftAntler", new Vector3f(0,0,0));
		this.partRotations.put("LeftFrontLeg", new Vector3f(0,-leg1Y,-legsAngleZ+leg1Z));
		this.partRotations.put("LeftFrontWing", new Vector3f(-angleWingX,angleWingY,0));
		this.partRotations.put("LeftMiddleLeg", new Vector3f(0,-leg2Y,-legsAngleZ+leg2Z));
		this.partRotations.put("LeftRearLeg", new Vector3f(0,-leg3Y,-legsAngleZ*1.5f+leg3Z));
		this.partRotations.put("LeftRearWing", new Vector3f(-angleWingX,angleWingY,0));
		this.partRotations.put("RightAntler", new Vector3f(0,0,0));
		this.partRotations.put("RightFrontLeg", new Vector3f(0,leg1Y,legsAngleZ-leg1Z));
		this.partRotations.put("RightFrontWing", new Vector3f(-angleWingX,-angleWingY,0));
		this.partRotations.put("RightMiddleLeg", new Vector3f(0,leg2Y,legsAngleZ-leg2Z));
		this.partRotations.put("RightRearLeg", new Vector3f(0,leg3Y,legsAngleZ*1.5f-leg3Z));
		this.partRotations.put("RightRearWing", new Vector3f(-angleWingX,-angleWingY,0));
	}

	protected void applyRotations(GiantBee entityLiving, float rotationYaw, float partialTicks)
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

	private void applyLocalRotation(OBJModelLoader.Part part, Vector3f rotation) {
		if(!part.parent.equals("")) {
			applyLocalRotation(GIANT_BEE_MODEL.get(part.parent), this.partRotations.get(part.parent));
		}
		if(rotation != null) {
			GlStateManager.translate(part.origin.x, part.origin.y, part.origin.z);
			GlStateManager.rotate(rotation.x, 1, 0, 0);
			GlStateManager.rotate(rotation.y, 0, 1, 0);
			GlStateManager.rotate(rotation.z, 0, 0, 1);
			GlStateManager.translate(-part.origin.x, -part.origin.y, -part.origin.z);
		}
	}
}
