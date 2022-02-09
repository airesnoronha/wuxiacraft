package com.lazydragonstudios.wuxiacraft.client.render;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.render.models.GhostModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GhostRenderer extends LivingEntityRenderer<AbstractClientPlayer, GhostModel> {

	private final static ResourceLocation GHOST_TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/entity/ghost.png");

	public static EntityType<AbstractClientPlayer> ghostEntityType;

	public GhostRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new GhostModel(ctx.bakeLayer(GhostModel.LOCATION)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractClientPlayer player) {
		return GHOST_TEXTURE;
	}

	@Override
	public void render(AbstractClientPlayer player, float notKnowWhatThisIs, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();

		boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
		this.model.riding = shouldSit;
		this.model.young = player.isBaby();

		this.model.crouching = player.isCrouching();

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

		float headBob = this.getBob(player, partialTicks);
		this.setupRotations(player, poseStack, headBob, rotationYaw, partialTicks);
		poseStack.scale(-1, -1, 1);
		this.scale(player, poseStack, partialTicks);
		poseStack.translate(0.0D, -1.501F, 0.0D);

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
		this.model.setupAnim(player, limbSwing, limbSwingAmount, headBob, netHeadYaw, headPitch);

		var mc = Minecraft.getInstance();
		if (mc.player == null) return;
		boolean flag = this.isBodyVisible(player);
		boolean flag1 = !flag && !player.isInvisibleTo(mc.player);
		var renderType = this.model.renderType(new ResourceLocation(WuxiaCraft.MOD_ID, "textures/entity/ghost.png"));
		VertexConsumer vertexconsumer = multiBufferSource.getBuffer(renderType);
		int i = getOverlayCoords(player, this.getWhiteOverlayProgress(player, partialTicks));
		this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
		poseStack.popPose();
	}

	public static boolean isEntityUpsideDown(LivingEntity p_194454_) {
		if (p_194454_ instanceof Player || p_194454_.hasCustomName()) {
			String s = ChatFormatting.stripFormatting(p_194454_.getName().getString());
			//noinspection SpellCheckingInspection
			if ("Dinnerbone".equals(s) || "Grumm".equals(s)) {
				return !(p_194454_ instanceof Player) || ((Player) p_194454_).isModelPartShown(PlayerModelPart.CAPE);
			}
		}

		return false;
	}

	@Override
	protected void scale(AbstractClientPlayer player, PoseStack poseStack, float partialTicks) {
		float f = 0.9375F;
		poseStack.scale(f, f, f);
	}
}
