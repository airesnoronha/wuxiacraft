package wuxiacraft.client.render.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GhostModel extends EntityModel<PlayerEntity> implements IHasArm, IHasHead {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer right_arm;
	private final ModelRenderer left_arm;
	private final ModelRenderer leg;
	private final ModelRenderer foot;

	public float swimAnimation;

	public BipedModel.ArmPose leftArmPose = BipedModel.ArmPose.EMPTY;
	public BipedModel.ArmPose rightArmPose = BipedModel.ArmPose.EMPTY;

	public GhostModel() {
		super(WuxiaRenderTypes::getEntitySeeTrough);
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		body.addChild(right_arm);
		right_arm.setTextureOffset(48, 12).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
		left_arm.mirror = true;
		body.addChild(left_arm);
		left_arm.setTextureOffset(48, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

		leg = new ModelRenderer(this);
		leg.setRotationPoint(0.0F, -12.0F, -1.0F);
		body.addChild(leg);
		leg.setTextureOffset(0, 56).addBox(-2.0F, 24.0F, 0.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

		foot = new ModelRenderer(this);
		foot.setRotationPoint(0.0F, 4.0F, 1.0F);
		leg.addChild(foot);
		foot.setTextureOffset(24, 57).addBox(-1.0F, 24.0F, 0.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		this.swimAnimation = 0;
	}

	@Override
	public void setLivingAnimations(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
		this.swimAnimation = entityIn.getSwimAnimation(partialTick);
		super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
	}

	@Override
	public void setRotationAngles(PlayerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//previously the render function, render code was moved to a method below
		boolean flag = entity.getTicksElytraFlying() > 4;
		boolean flag1 = entity.isActualySwimming();
		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
		if (flag) {
			this.head.rotateAngleX = (-(float) Math.PI / 4F);
		} else if (this.swimAnimation > 0.0F) {
			if (flag1) {
				this.head.rotateAngleX = this.rotLerpRad(this.swimAnimation, this.head.rotateAngleX, (-(float) Math.PI / 4F));
			} else {
				this.head.rotateAngleX = this.rotLerpRad(this.swimAnimation, this.head.rotateAngleX, headPitch * ((float) Math.PI / 180F));
			}
		} else {
			this.head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
		}

		this.right_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.left_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.right_arm.rotateAngleZ = 0.0F;
		this.left_arm.rotateAngleZ = 0.0F;

		this.right_arm.rotateAngleY = 0.0F;
		this.left_arm.rotateAngleY = 0.0F;

		if (this.isSitting) {
			this.right_arm.rotateAngleX += (-(float) Math.PI / 5F);
			this.left_arm.rotateAngleX += (-(float) Math.PI / 5F);
		}

		this.right_arm.rotateAngleY = 0.0F;
		this.left_arm.rotateAngleY = 0.0F;
		boolean flag2 = entity.getPrimaryHand() == HandSide.RIGHT;
		boolean flag3 = flag2 ? this.leftArmPose.func_241657_a_() : this.rightArmPose.func_241657_a_();
		if (flag2 != flag3) {
			this.poseLeftArm(entity);
			this.poseRightArm(entity);
		} else {
			this.poseRightArm(entity);
			this.poseLeftArm(entity);
		}

		swingArms(entity, ageInTicks);
		if (entity.isSneaking()) {
			this.body.rotateAngleX = 0.5F;
			this.body.rotationPointY = 3.2F;
		} else {
			this.body.rotateAngleX = 0.0F;
			this.body.rotationPointY = 0.0F;
		}
		ModelHelper.func_239101_a_(this.right_arm, this.left_arm, ageInTicks);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
		this.getArmForSide(sideIn).translateRotate(matrixStackIn);
	}

	protected ModelRenderer getArmForSide(HandSide side) {
		return side == HandSide.LEFT ? this.left_arm : this.right_arm;
	}

	@Override
	public ModelRenderer getModelHead() {
		return this.head;
	}

	protected void swingArms(PlayerEntity entity, float ageinticks) {
		if (!(this.swingProgress <= 0.0F)) {
			HandSide handside = this.getMainHand(entity);
			ModelRenderer modelrenderer = this.getArmForSide(handside);
			float f = this.swingProgress;
			this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
			if (handside == HandSide.LEFT) {
				this.body.rotateAngleY *= -1.0F;
			}

			this.right_arm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
			this.right_arm.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
			this.left_arm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
			this.left_arm.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
			this.right_arm.rotateAngleY += this.body.rotateAngleY;
			this.left_arm.rotateAngleY += this.body.rotateAngleY;
			this.left_arm.rotateAngleX += this.body.rotateAngleY;
			f = 1.0F - this.swingProgress;
			f = f * f;
			f = f * f;
			f = 1.0F - f;
			float f1 = MathHelper.sin(f * (float) Math.PI);
			float f2 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
			modelrenderer.rotateAngleX = (float) ((double) modelrenderer.rotateAngleX - ((double) f1 * 1.2D + (double) f2));
			modelrenderer.rotateAngleY += this.body.rotateAngleY * 2.0F;
			modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
		}
	}

	protected HandSide getMainHand(PlayerEntity entityIn) {
		HandSide handside = entityIn.getPrimaryHand();
		return entityIn.swingingHand == Hand.MAIN_HAND ? handside : handside.opposite();
	}

	protected float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {
		float f = (mulIn - maxAngleIn) % ((float) Math.PI * 2F);
		if (f < -(float) Math.PI) {
			f += ((float) Math.PI * 2F);
		}

		if (f >= (float) Math.PI) {
			f -= ((float) Math.PI * 2F);
		}

		return maxAngleIn + angleIn * f;
	}

	private void poseRightArm(PlayerEntity player) {
		switch (this.rightArmPose) {
			case EMPTY:
				this.right_arm.rotateAngleY = 0.0F;
				break;
			case BLOCK:
				this.right_arm.rotateAngleX = this.right_arm.rotateAngleX * 0.5F - 0.9424779F;
				this.right_arm.rotateAngleY = (-(float) Math.PI / 6F);
				break;
			case ITEM:
				this.right_arm.rotateAngleX = this.right_arm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
				this.right_arm.rotateAngleY = 0.0F;
				break;
			case THROW_SPEAR:
				this.right_arm.rotateAngleX = this.right_arm.rotateAngleX * 0.5F - (float) Math.PI;
				this.right_arm.rotateAngleY = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.right_arm.rotateAngleY = -0.1F + this.head.rotateAngleY;
				this.left_arm.rotateAngleY = 0.1F + this.head.rotateAngleY + 0.4F;
				this.right_arm.rotateAngleX = (-(float) Math.PI / 2F) + this.head.rotateAngleX;
				this.left_arm.rotateAngleX = (-(float) Math.PI / 2F) + this.head.rotateAngleX;
				break;
			case CROSSBOW_CHARGE:
				ModelHelper.func_239102_a_(this.right_arm, this.left_arm, player, true);
				break;
			case CROSSBOW_HOLD:
				ModelHelper.func_239104_a_(this.right_arm, this.left_arm, this.head, true);
		}

	}

	private void poseLeftArm(PlayerEntity player) {
		switch (this.leftArmPose) {
			case EMPTY:
				this.left_arm.rotateAngleY = 0.0F;
				break;
			case BLOCK:
				this.left_arm.rotateAngleX = this.left_arm.rotateAngleX * 0.5F - 0.9424779F;
				this.left_arm.rotateAngleY = ((float) Math.PI / 6F);
				break;
			case ITEM:
				this.left_arm.rotateAngleX = this.left_arm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
				this.left_arm.rotateAngleY = 0.0F;
				break;
			case THROW_SPEAR:
				this.left_arm.rotateAngleX = this.left_arm.rotateAngleX * 0.5F - (float) Math.PI;
				this.left_arm.rotateAngleY = 0.0F;
				break;
			case BOW_AND_ARROW:
				this.right_arm.rotateAngleY = -0.1F + this.head.rotateAngleY - 0.4F;
				this.left_arm.rotateAngleY = 0.1F + this.head.rotateAngleY;
				this.right_arm.rotateAngleX = (-(float) Math.PI / 2F) + this.head.rotateAngleX;
				this.left_arm.rotateAngleX = (-(float) Math.PI / 2F) + this.head.rotateAngleX;
				break;
			case CROSSBOW_CHARGE:
				ModelHelper.func_239102_a_(this.right_arm, this.left_arm, player, false);
				break;
			case CROSSBOW_HOLD:
				ModelHelper.func_239104_a_(this.right_arm, this.left_arm, this.head, false);
		}

	}
}