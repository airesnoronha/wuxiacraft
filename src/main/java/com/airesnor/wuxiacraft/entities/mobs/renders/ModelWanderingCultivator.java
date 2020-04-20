package com.airesnor.wuxiacraft.entities.mobs.renders;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModelWanderingCultivator extends ModelBiped {

	public final ModelRenderer hairPin;
	public final ModelRenderer crown;
	public final ModelRenderer ponyTail;
	public final ModelRenderer ponyTailExtension;

	public ModelWanderingCultivator() {
		super(0, 0, 64,64);
		this.crown = new ModelRenderer(this, 8, 57);
		this.crown.addBox(-1f, -11f,3f, 2,3,2);
		this.crown.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.hairPin = new ModelRenderer(this, 0, 62);
		this.hairPin.addBox(-3,-9.5f, 3.5f, 6,1,1);
		this.hairPin.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.ponyTail = new ModelRenderer(this, 0, 52);
		this.ponyTail.addBox(-1f, -8f, 4f, 2, 8, 2);
		this.ponyTail.setRotationPoint(0f,0f, 0f);
		this.ponyTailExtension = new ModelRenderer(this, 0, 52);
		this.ponyTailExtension.addBox(-1f, 0f, 4f, 2, 8, 2);
		this.ponyTailExtension.setRotationPoint(0f,0f, 0f);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.pushMatrix();
		this.crown.render(scale);
		this.hairPin.render(scale);
		this.ponyTail.render(scale);
		this.ponyTailExtension.render(scale);
		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getTicksElytraFlying() > 4;
		this.crown.rotateAngleY = netHeadYaw * 0.017453292F;
		this.hairPin.rotateAngleY = netHeadYaw * 0.017453292F;
		this.ponyTail.rotateAngleY = netHeadYaw * 0.017453292F;
		this.ponyTailExtension.rotateAngleY = netHeadYaw * 0.017453292F;

		float angleX = headPitch * 0.017453292F;
		if (flag)
		{
			float rotateAngleX = -((float) Math.PI / 4F);
			this.crown.rotateAngleX = rotateAngleX;
			this.hairPin.rotateAngleX = rotateAngleX;
			this.ponyTail.rotateAngleX = rotateAngleX;
			this.ponyTailExtension.rotateAngleX = rotateAngleX;
		}
		else
		{
			this.crown.rotateAngleX = angleX;
			this.hairPin.rotateAngleX = angleX;
			this.ponyTail.rotateAngleX = Math.max(0, angleX);
			this.ponyTailExtension.rotateAngleX = Math.max(0, angleX);
		}
		if(headPitch < 0 ) {
			float offsetZ = (float) (Math.cos(Math.PI / 2f + angleX) * 0.41f);
			float offsetY = (float) (-Math.sin(angleX) * 0.41f);
			this.ponyTail.offsetY = offsetY;
			this.ponyTail.offsetZ = offsetZ;
			this.ponyTailExtension.offsetY = offsetY;
			this.ponyTailExtension.offsetZ = offsetZ;
		}
	}
}
