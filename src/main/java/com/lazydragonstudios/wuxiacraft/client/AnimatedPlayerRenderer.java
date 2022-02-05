package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnimatedPlayerRenderer extends PlayerRenderer {

	public static EntityType<AbstractClientPlayer> animatedEntityType;

	private Vector3f position = Vector3f.ZERO;

	public AnimatedPlayerRenderer(EntityRendererProvider.Context ctx, boolean slim) {
		super(ctx, slim);
	}



	@Override
	public void render(AbstractClientPlayer player, float notKnowWhatThisIs, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn) {
		this.setModelProperties(player);
		//if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Pre(player, this, partialTicks, poseStack, multiBufferSource, packedLightIn)))
		//	return;
		poseStack.pushPose();
		this.model.attackTime = this.getAttackAnim(player, partialTicks);

		boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
		this.model.riding = shouldSit;
		this.model.young = player.isBaby();

		float rotationYaw = Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
		float interpolatedHeadYaw = Mth.rotLerp(partialTicks, player.yHeadRotO, player.yHeadRot);
		float netHeadYaw = interpolatedHeadYaw - rotationYaw;

		if (shouldSit && player.getVehicle() instanceof LivingEntity livingentity) {
			rotationYaw = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
			netHeadYaw = interpolatedHeadYaw - rotationYaw;
			float f3 = Mth.wrapDegrees(netHeadYaw);
			if (f3 < -85.0F) {
				f3 = -85.0F;
			}

			if (f3 >= 85.0F) {
				f3 = 85.0F;
			}

			rotationYaw = interpolatedHeadYaw - f3;
			if (f3 * f3 > 2500.0F) {
				rotationYaw += f3 * 0.2F;
			}

			netHeadYaw = interpolatedHeadYaw - rotationYaw;
		}

		float headPitch = Mth.lerp(partialTicks, player.xRotO, player.getXRot());
		if (isEntityUpsideDown(player)) {
			headPitch *= -1.0F;
			netHeadYaw *= -1.0F;
		}

		if (player.getPose() == Pose.SLEEPING) {
			Direction direction = player.getBedOrientation();
			if (direction != null) {
				float f4 = player.getEyeHeight(Pose.STANDING) - 0.1F;
				poseStack.translate((float) (-direction.getStepX()) * f4, 0.0D, (float) (-direction.getStepZ()) * f4);
			}
		}
		poseStack.translate(this.position.x(), this.position.y(), this.position.z());
		float headBob = this.getBob(player, partialTicks);
		this.setupRotations(player, poseStack, headBob, rotationYaw, partialTicks);
		poseStack.scale(-1, -1, 1);
		this.scale(player, poseStack, partialTicks);
		poseStack.translate(0.0D, (double) -1.501F, 0.0D);

		float limbSwingAmount = 0.0F;
		float limbSwing = 0.0F;
		if (!shouldSit && player.isAlive()) {
			limbSwingAmount = Mth.lerp(partialTicks, player.animationSpeedOld, player.animationSpeed);
			limbSwing = player.animationPosition - player.animationSpeed * (1.0F - partialTicks);
			if (player.isBaby()) {
				limbSwing *= 3.0F;
			}

			if (limbSwingAmount > 1.0F) {
				limbSwingAmount = 1.0F;
			}
		}

		this.model.prepareMobModel(player, limbSwing, limbSwingAmount, partialTicks);
		//this.model.setupAnim(player, limbSwing, limbSwingAmount, headBob, netHeadYaw, headPitch);
		this.setAnimations(player, this.model, partialTicks);

		var mc = Minecraft.getInstance();
		if(mc.player == null) return;
		boolean flag = this.isBodyVisible(player);
		boolean flag1 = !flag && !player.isInvisibleTo(mc.player);
		boolean flag2 = mc.shouldEntityAppearGlowing(player);
		var renderType= this.getRenderType(player, flag, flag1, flag2);
		if(renderType != null) {
			VertexConsumer vertexconsumer = multiBufferSource.getBuffer(renderType);
			int i = getOverlayCoords(player, this.getWhiteOverlayProgress(player, partialTicks));
			this.model.renderToBuffer(poseStack, vertexconsumer, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		}
		if(!player.isSpectator()) {
			for(var renderLayer : this.layers) {
				renderLayer.render(poseStack, multiBufferSource, packedLightIn, player, limbSwing, limbSwingAmount, partialTicks, headBob, netHeadYaw, headPitch);
			}
		}
		poseStack.popPose();
		this.position = Vector3f.ZERO;
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderPlayerEvent.Post(player, this, partialTicks, poseStack, multiBufferSource, packedLightIn));
		net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(player, player.getDisplayName(), this, poseStack, multiBufferSource, packedLightIn, partialTicks);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
		if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(player))) {
			this.renderNameTag(player, renderNameplateEvent.getContent(), poseStack, multiBufferSource, packedLightIn);
		}
	}

	private void setAnimations(AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> model, float partialTicks) {
		var animationState = ClientAnimationState.get(player);
		if(animationState.isMeditating()) {
			this.position.setY(-0.55f);
			model.leftLeg.x = 1.9f+2f;
			model.leftLeg.y = 12f+1f;
			model.leftLeg.z = 0;
			model.leftLeg.xRot = (float) Math.toRadians(-52.5);
			model.leftLeg.yRot = 0f;
			model.leftLeg.zRot = (float) Math.toRadians(100);
			model.rightLeg.x = -1.9f-2f;
			model.rightLeg.y = 12f+1f;
			model.rightLeg.z = 0;
			model.rightLeg.xRot = (float) Math.toRadians(-40);
			model.rightLeg.yRot = 0f;//100f;
			model.rightLeg.zRot = (float) Math.toRadians(-100);
			model.leftArm.xRot = (float) Math.toRadians(-45);
			model.leftArm.yRot = 0f;
			model.leftArm.zRot = (float) Math.toRadians(45);
			model.rightArm.xRot = (float) Math.toRadians(-45);
			model.rightArm.yRot = 0f;
			model.rightArm.zRot = (float) Math.toRadians(-45);
			model.head.xRot = (float) Math.toRadians(0f);
		}
	}


	private void setModelProperties(AbstractClientPlayer p_117819_) {
		PlayerModel<AbstractClientPlayer> playerModel = this.getModel();
		if (p_117819_.isSpectator()) {
			playerModel.setAllVisible(false);
			playerModel.head.visible = true;
			playerModel.hat.visible = true;
		} else {
			playerModel.setAllVisible(true);
			playerModel.hat.visible = p_117819_.isModelPartShown(PlayerModelPart.HAT);
			playerModel.jacket.visible = p_117819_.isModelPartShown(PlayerModelPart.JACKET);
			playerModel.leftPants.visible = p_117819_.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
			playerModel.rightPants.visible = p_117819_.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
			playerModel.leftSleeve.visible = p_117819_.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
			playerModel.rightSleeve.visible = p_117819_.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
			playerModel.crouching = p_117819_.isCrouching();
			HumanoidModel.ArmPose humanoidModel$armPose = getArmPose(p_117819_, InteractionHand.MAIN_HAND);
			HumanoidModel.ArmPose humanoidModel$armPose1 = getArmPose(p_117819_, InteractionHand.OFF_HAND);
			if (humanoidModel$armPose.isTwoHanded()) {
				humanoidModel$armPose1 = p_117819_.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
			}

			if (p_117819_.getMainArm() == HumanoidArm.RIGHT) {
				playerModel.rightArmPose = humanoidModel$armPose;
				playerModel.leftArmPose = humanoidModel$armPose1;
			} else {
				playerModel.rightArmPose = humanoidModel$armPose1;
				playerModel.leftArmPose = humanoidModel$armPose;
			}
		}

	}

	private static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer p_117795_, InteractionHand p_117796_) {
		ItemStack itemstack = p_117795_.getItemInHand(p_117796_);
		if (itemstack.isEmpty()) {
			return HumanoidModel.ArmPose.EMPTY;
		} else {
			if (p_117795_.getUsedItemHand() == p_117796_ && p_117795_.getUseItemRemainingTicks() > 0) {
				UseAnim useanim = itemstack.getUseAnimation();
				if (useanim == UseAnim.BLOCK) {
					return HumanoidModel.ArmPose.BLOCK;
				}

				if (useanim == UseAnim.BOW) {
					return HumanoidModel.ArmPose.BOW_AND_ARROW;
				}

				if (useanim == UseAnim.SPEAR) {
					return HumanoidModel.ArmPose.THROW_SPEAR;
				}

				if (useanim == UseAnim.CROSSBOW && p_117796_ == p_117795_.getUsedItemHand()) {
					return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
				}

				if (useanim == UseAnim.SPYGLASS) {
					return HumanoidModel.ArmPose.SPYGLASS;
				}
			} else if (!p_117795_.swinging && itemstack.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemstack)) {
				return HumanoidModel.ArmPose.CROSSBOW_HOLD;
			}

			return HumanoidModel.ArmPose.ITEM;
		}
	}
}
