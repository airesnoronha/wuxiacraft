package wuxiacraft.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.render.model.GhostModel;
import wuxiacraft.client.render.model.WuxiaRenderTypes;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.util.MathUtils;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class EntityRenderHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderPlayerThroughWallsPre(RenderLivingEvent.Pre<AbstractClientPlayerEntity, ? extends Model> event) {
		if (event.getEntity() instanceof RemoteClientPlayerEntity && Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSneaking()) {
			ICultivation other = Cultivation.get(event.getEntity());
			ICultivation mine = Cultivation.get(Minecraft.getInstance().player);
			if (MathUtils.between(other.getDivineModifier(), 0, mine.getDivineModifier()) &&
					event.getEntity().getDistance(Minecraft.getInstance().player) <= mine.getDivineModifier() * 1.2) {
				float partialTicks = event.getPartialRenderTick();
				AbstractClientPlayerEntity target = (AbstractClientPlayerEntity) event.getEntity();
				event.setCanceled(true);
				MatrixStack matrixStack = event.getMatrixStack();
				matrixStack.push();
				EntityModel<AbstractClientPlayerEntity> baseModel = event.getRenderer().getEntityModel();
				GhostModel ghostModel = new GhostModel();
				baseModel.swingProgress = target.getSwingProgress(partialTicks);
				ghostModel.swingProgress = target.getSwingProgress(partialTicks);

				boolean shouldSit = target.isPassenger() && (target.getRidingEntity() != null && target.getRidingEntity().shouldRiderSit());
				baseModel.isSitting = shouldSit;
				baseModel.isChild = target.isChild();
				ghostModel.isSitting = shouldSit;
				ghostModel.isChild = target.isChild();

				float rotationYaw = MathHelper.interpolateAngle(partialTicks, target.prevRenderYawOffset, target.renderYawOffset);
				float interpolatedHeadYaw = MathHelper.interpolateAngle(partialTicks, target.prevRotationYawHead, target.rotationYawHead);
				float netHeadYaw = interpolatedHeadYaw - rotationYaw;
				Pair<Float, Float> rotationYawPair = handleIfRidingEntity(shouldSit, target, rotationYaw, netHeadYaw, interpolatedHeadYaw, partialTicks);
				rotationYaw = rotationYawPair.getLeft();
				netHeadYaw = rotationYawPair.getRight();

				float headPitch = MathHelper.lerp(partialTicks, target.prevRotationPitch, target.rotationPitch);
				if (target.getPose() == Pose.SLEEPING) {
					Direction direction = target.getBedDirection();
					if (direction != null) {
						float f4 = target.getEyeHeight(Pose.STANDING) - 0.1F;
						matrixStack.translate((float) (-direction.getXOffset()) * f4, 0.0D, (float) (-direction.getZOffset()) * f4);
					}
				}

				float ageInTicks = (float) target.ticksExisted + partialTicks;
				applyRotations(target, matrixStack, rotationYaw, partialTicks);
				matrixStack.scale(-1.0F, -1.0F, 1.0F);
				preRenderCallback(matrixStack);
				matrixStack.translate(0.0D, -1.501F, 0.0D);
				float limbSwingAmount = 0.0F;
				float limbSwing = 0.0F;
				if (!shouldSit && target.isAlive()) {
					limbSwingAmount = MathHelper.lerp(partialTicks, target.prevLimbSwingAmount, target.limbSwingAmount);
					limbSwing = target.limbSwing - target.limbSwingAmount * (1.0F - partialTicks);
					if (target.isChild()) {
						limbSwing *= 3.0F;
					}
					if (limbSwingAmount > 1.0F) {
						limbSwingAmount = 1.0F;
					}
				}

				baseModel.setLivingAnimations(target, limbSwing, limbSwingAmount, partialTicks);
				baseModel.setRotationAngles(target, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				ghostModel.setLivingAnimations(target, limbSwing, limbSwingAmount, partialTicks);
				ghostModel.setRotationAngles(target, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

				ghostModel.rightArmPose = getArmPose(target, Hand.MAIN_HAND);
				ghostModel.leftArmPose = getArmPose(target, Hand.OFF_HAND);
				if (ghostModel.rightArmPose.func_241657_a_()) {
					ghostModel.leftArmPose = target.getHeldItemOffhand().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM;
				}

				if (target.getPrimaryHand() != HandSide.RIGHT) {
					BipedModel.ArmPose aux = ghostModel.rightArmPose;
					ghostModel.rightArmPose = ghostModel.leftArmPose;
					ghostModel.leftArmPose = aux;
				}

				Minecraft minecraft = Minecraft.getInstance();
				boolean flag = !target.isInvisible();
				//noinspection ConstantConditions
				boolean flag1 = !flag && !target.isInvisibleToPlayer(minecraft.player);
				boolean flag2 = minecraft.isEntityGlowing(target);
				RenderType renderTypeBase = this.getRenderTypeForEntity(baseModel, target, flag, flag1, flag2);
				RenderType renderTypeGhost = WuxiaRenderTypes.getEntitySeeTrough(new ResourceLocation(WuxiaCraft.MOD_ID, "textures/entities/ghost.png"));
				IRenderTypeBuffer.Impl bufferIn = minecraft.getRenderTypeBuffers().getBufferSource();
				int packedLightIn = event.getRenderer().getPackedLight(target, event.getPartialRenderTick());
				if (renderTypeBase != null) {
					IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderTypeBase);
					int i = LivingRenderer.getPackedOverlay(target, 0);
					baseModel.render(matrixStack, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
				}

				renderBipedModelLayers(event, partialTicks, target, matrixStack, netHeadYaw, headPitch, ageInTicks, limbSwingAmount, limbSwing, bufferIn, packedLightIn);

				IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderTypeGhost);
				int i = LivingRenderer.getPackedOverlay(target, 0);
				ghostModel.render(matrixStack, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 0.4F);


				matrixStack.pop();
				//draw name plate
				net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(target, target.getDisplayName(), event.getRenderer(), matrixStack, bufferIn, packedLightIn, partialTicks);
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
				if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || canRenderName())) {
					renderName(target, renderNameplateEvent.getContent(), matrixStack, bufferIn, packedLightIn, event.getRenderer().getRenderManager());
				}
				//throw again a post event for compatibility
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(target, event.getRenderer(), partialTicks, matrixStack, bufferIn, packedLightIn));
			}
		}
	}

	@SubscribeEvent
	public void renderPlayerCultivationAnimation(RenderLivingEvent.Pre<AbstractClientPlayerEntity, ? extends Model> event) {
		if (!(event.getEntity() instanceof AbstractClientPlayerEntity)) return;
		ICultivation cultivation = Cultivation.get(event.getEntity());
		if (cultivation.isExercising()) {
			float partialTicks = event.getPartialRenderTick();
			AbstractClientPlayerEntity target = (AbstractClientPlayerEntity) event.getEntity();
			event.setCanceled(true);
			MatrixStack matrixStack = event.getMatrixStack();
			matrixStack.push();
			EntityModel<AbstractClientPlayerEntity> baseModel = event.getRenderer().getEntityModel();
			baseModel.swingProgress = target.getSwingProgress(partialTicks);

			boolean shouldSit = target.isPassenger() && (target.getRidingEntity() != null && target.getRidingEntity().shouldRiderSit());
			baseModel.isSitting = shouldSit;
			baseModel.isChild = target.isChild();

			float rotationYaw = MathHelper.interpolateAngle(partialTicks, target.prevRenderYawOffset, target.renderYawOffset);
			float interpolatedHeadYaw = MathHelper.interpolateAngle(partialTicks, target.prevRotationYawHead, target.rotationYawHead);
			float netHeadYaw = interpolatedHeadYaw - rotationYaw;
			Pair<Float, Float> rotationYawPair = handleIfRidingEntity(shouldSit, target, rotationYaw, netHeadYaw, interpolatedHeadYaw, partialTicks);
			rotationYaw = rotationYawPair.getLeft();
			netHeadYaw = rotationYawPair.getRight();

			float headPitch = MathHelper.lerp(partialTicks, target.prevRotationPitch, target.rotationPitch);
			if (target.getPose() == Pose.SLEEPING) {
				Direction direction = target.getBedDirection();
				if (direction != null) {
					float f4 = target.getEyeHeight(Pose.STANDING) - 0.1F;
					matrixStack.translate((float) (-direction.getXOffset()) * f4, 0.0D, (float) (-direction.getZOffset()) * f4);
				}
			}

			float ageInTicks = (float) target.ticksExisted + partialTicks;
			applyRotations(target, matrixStack, rotationYaw, partialTicks);
			matrixStack.scale(-1.0F, -1.0F, 1.0F);
			preRenderCallback(matrixStack);
			matrixStack.translate(0.0D, -1.501F, 0.0D);
			float limbSwingAmount = 0.0F;
			float limbSwing = 0.0F;
			if (!shouldSit && target.isAlive()) {
				limbSwingAmount = MathHelper.lerp(partialTicks, target.prevLimbSwingAmount, target.limbSwingAmount);
				limbSwing = target.limbSwing - target.limbSwingAmount * (1.0F - partialTicks);
				if (target.isChild()) {
					limbSwing *= 3.0F;
				}

				if (limbSwingAmount > 1.0F) {
					limbSwingAmount = 1.0F;
				}
			}

			//baseModel.setLivingAnimations(target, f5, f8, partialTicks);
			//baseModel.setRotationAngles(target, f5, f8, f7, f2, f6);
			animatePlayerModelExercising((BipedModel<AbstractClientPlayerEntity>) baseModel, target, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

			Minecraft minecraft = Minecraft.getInstance();
			IRenderTypeBuffer.Impl bufferIn = minecraft.getRenderTypeBuffers().getBufferSource();
			int packedLightIn = event.getRenderer().getPackedLight(target, event.getPartialRenderTick());

			boolean flag = !target.isInvisible();
			//noinspection ConstantConditions
			boolean flag1 = !flag && !target.isInvisibleToPlayer(minecraft.player);
			boolean flag2 = minecraft.isEntityGlowing(target);
			RenderType renderTypeBase = this.getRenderTypeForEntity(baseModel, target, flag, flag1, flag2);
			if (renderTypeBase != null) {
				IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderTypeBase);
				int i = LivingRenderer.getPackedOverlay(target, 0);
				baseModel.render(matrixStack, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
			}


			renderBipedModelLayers(event, partialTicks, target, matrixStack, netHeadYaw, headPitch, ageInTicks, limbSwingAmount, limbSwing, bufferIn, packedLightIn);

			matrixStack.pop();
			//draw name plate
			net.minecraftforge.client.event.RenderNameplateEvent renderNameplateEvent = new net.minecraftforge.client.event.RenderNameplateEvent(target, target.getDisplayName(), event.getRenderer(), matrixStack, bufferIn, packedLightIn, partialTicks);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
			if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || canRenderName())) {
				renderName(target, renderNameplateEvent.getContent(), matrixStack, bufferIn, packedLightIn, event.getRenderer().getRenderManager());
			}
			//throw again a post event for compatibility
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(target, event.getRenderer(), partialTicks, matrixStack, bufferIn, packedLightIn));
		}
	}

	private Pair<Float, Float> handleIfRidingEntity(boolean shouldSit, AbstractClientPlayerEntity target, float rotationYaw, float netHeadYaw, float interpolatedHeadYaw, float partialTicks) {
		if (shouldSit && target.getRidingEntity() instanceof LivingEntity) {
			LivingEntity livingentity = (LivingEntity) target.getRidingEntity();
			rotationYaw = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
			netHeadYaw = interpolatedHeadYaw - rotationYaw;
			float f3 = MathHelper.wrapDegrees(netHeadYaw);
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
		return Pair.of(rotationYaw, netHeadYaw);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void renderBipedModelLayers(RenderLivingEvent.Pre<AbstractClientPlayerEntity, ? extends Model> event, float partialTicks, AbstractClientPlayerEntity target, MatrixStack matrixStack, float netHeadYaw, float headPitch, float ageInTicks, float limbSwingAmount, float limbSwing, IRenderTypeBuffer.Impl bufferIn, int packedLightIn) {
		if (!target.isSpectator()) {
			List<LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> layerList = new LinkedList<>();
			layerList.add(new BipedArmorLayer(event.getRenderer(), new BipedModel(0.5F), new BipedModel(1.0F)));
			layerList.add(new HeldItemLayer(event.getRenderer()));
			layerList.add(new ArrowLayer(event.getRenderer()));
			layerList.add(new HeadLayer(event.getRenderer()));
			layerList.add(new ElytraLayer(event.getRenderer()));
			layerList.add(new ParrotVariantLayer(event.getRenderer()));
			layerList.add(new SpinAttackEffectLayer(event.getRenderer()));
			layerList.add(new BeeStingerLayer(event.getRenderer()));
			for (LayerRenderer<AbstractClientPlayerEntity, ?> layerRenderer : layerList) {
				layerRenderer.render(matrixStack, bufferIn, packedLightIn, target, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
			}
		}
	}

	private static boolean canRenderName() {
		return true;
	}

	private static void renderName(PlayerEntity entityIn, ITextComponent displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRendererManager rendererManager) {
		double d0 = rendererManager.squareDistanceTo(entityIn);
		if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(entityIn, d0)) {
			boolean flag = !entityIn.isDiscrete();
			float f = entityIn.getHeight() + 0.5F;
			//noinspection SpellCheckingInspection
			int i = "deadmau5".equals(displayNameIn.getString()) ? -10 : 0;
			matrixStackIn.push();
			matrixStackIn.translate(0.0D, f, 0.0D);
			matrixStackIn.rotate(rendererManager.getCameraOrientation());
			matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int j = (int) (f1 * 255.0F) << 24;
			FontRenderer fontrenderer = rendererManager.getFontRenderer();
			float f2 = (float) (-fontrenderer.getStringPropertyWidth(displayNameIn) / 2);
			fontrenderer.func_243247_a(displayNameIn, f2, (float) i, 553648127, false, matrix4f, bufferIn, flag, j, packedLightIn);
			if (flag) {
				fontrenderer.func_243247_a(displayNameIn, f2, (float) i, -1, false, matrix4f, bufferIn, false, 0, packedLightIn);
			}

			matrixStackIn.pop();
		}
	}

	private static BipedModel.ArmPose getArmPose(AbstractClientPlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (itemstack.isEmpty()) {
			return BipedModel.ArmPose.EMPTY;
		} else {
			if (player.getActiveHand() == hand && player.getItemInUseCount() > 0) {
				UseAction useaction = itemstack.getUseAction();
				if (useaction == UseAction.BLOCK) {
					return BipedModel.ArmPose.BLOCK;
				}

				if (useaction == UseAction.BOW) {
					return BipedModel.ArmPose.BOW_AND_ARROW;
				}

				if (useaction == UseAction.SPEAR) {
					return BipedModel.ArmPose.THROW_SPEAR;
				}

				if (useaction == UseAction.CROSSBOW && hand == player.getActiveHand()) {
					return BipedModel.ArmPose.CROSSBOW_CHARGE;
				}
			} else if (!player.isSwingInProgress && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
				return BipedModel.ArmPose.CROSSBOW_HOLD;
			}

			return BipedModel.ArmPose.ITEM;
		}
	}

	@Nullable
	protected RenderType getRenderTypeForEntity(EntityModel<? extends PlayerEntity> model, AbstractClientPlayerEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = p_230496_1_.getLocationSkin();
		if (p_230496_3_) {
			return RenderType.getItemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return model.getRenderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.getOutline(resourcelocation) : null;
		}
	}

	protected static void applyRotations(PlayerEntity entityLiving, MatrixStack matrixStackIn, float rotationYaw, float partialTicks) {
		Pose pose = entityLiving.getPose();
		if (pose != Pose.SLEEPING) {
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
		}

		if (entityLiving.deathTime > 0) {
			float f = ((float) entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);
			if (f > 1.0F) {
				f = 1.0F;
			}

			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * getDeathMaxRotation()));
		} else if (entityLiving.isSpinAttacking()) {
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.rotationPitch));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(((float) entityLiving.ticksExisted + partialTicks) * -75.0F));
		} else if (pose == Pose.SLEEPING) {
			Direction direction = entityLiving.getBedDirection();
			float f1 = direction != null ? getFacingAngle(direction) : rotationYaw;
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(getDeathMaxRotation()));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270.0F));
		} else if (entityLiving.hasCustomName()) {
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName().getString());
			//noinspection SpellCheckingInspection
			if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && entityLiving.isWearing(PlayerModelPart.CAPE)) {
				matrixStackIn.translate(0.0D, entityLiving.getHeight() + 0.1F, 0.0D);
				matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
			}
		}

	}

	private static float getFacingAngle(Direction facingIn) {
		switch (facingIn) {
			case SOUTH:
				return 90.0F;
			case NORTH:
				return 270.0F;
			case EAST:
				return 180.0F;
			default:
				return 0.0F;
		}
	}

	private static float getDeathMaxRotation() {
		return 90.0F;
	}

	protected static void preRenderCallback(MatrixStack matrixStackIn) {
		matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
	}

	private static float[] leftArmX = {0, -45, -90, -90, -90, 0, 0, 0, -90, -135, -180, -90, 0};;
	private static float[] leftArmY = {0, 0, 0, 0, -45, -90, -45, 0, 0, 0, 0, 0, 0};
	private static float[] leftArmZ = {0, 0, 0, 0, 0, 0, 0, 45, 90, 60, 45, 0, 0};

	private static float[] rightArmX = {0, -45, -90, -90, -90, 0, 0, 0, -90, -135, -180, -90, 0};
	private static float[] rightArmY = {0, 0, 0, 45, 90, 90, 45, 0, 0, 0, 0, 0, 0};
	private static float[] rightArmZ = {0, 0, 0, 0, 0, 0, 0, -45, -90, -60, -45, 0, 0};

	private static float[] lefLegArmX = {0, 0, 0, 0, 0, -5, -10, -10, -10, 0, 0, 0, 0};
	private static float[] lefLegArmY = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private static float[] lefLegArmZ = {0, 0, 0, -10, -10, -10, -10, -7.5f, -5, -7.5f, -10, -5, 0};

	private static float[] rightLegArmX = {0, 0, 0, 0, 0, 5, 10, 15, 20, 10, 0, 0, 0};
	private static float[] rightLegArmY = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private static float[] rightLegArmZ = {0, 0, 0, 5, 10, 10, 10, 10, 10, 10, 10, 5, 0};

	private static float getFrameRotation(float[] framesPositions, float animationPosition) {
		int position = (int) animationPosition;
		float partial = animationPosition - position;
		float angleInitial = framesPositions[position];
		float angleEnd = framesPositions[position + 1];
		return angleInitial + (angleEnd - angleInitial) * partial;
	}

	private static void animatePlayerModelExercising(BipedModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity target, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		ICultivation cultivation = Cultivation.get(target);

		leftArmX = new float[]{0, -45, -90, -90, -90, 0, 0, 0, -90, -135, -180, -90, 0};
		leftArmY = new float[]{0, 0, 0, 0, -45, -90, -45, 0, 0, 0, 0, 0, 0};
		leftArmZ = new float[]{0, 0, 0, 0, 0, 0, 0, 45, 90, 60, 45, 0, 0};

		rightArmX = new float[]{0, -45, -90, -90, -90, 0, 0, 0, -90, -135, -180, -90, 0};
		rightArmY = new float[]{0, 0, 0, 45, 90, 90, 45, 0, 0, 0, 0, 0, 0};
		rightArmZ = new float[]{0, 0, 0, 0, 0, 0, 0, -45, -90, -60, -45, 0, 0};

		lefLegArmX = new float[]{0, 0, 0, 0, 0, -5, -10, -10, -10, 0, 0, 0, 0};
		lefLegArmY = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		lefLegArmZ = new float[]{0, 0, 0, -10, -10, -10, -10, -7.5f, -5, -7.5f, -10, -5, 0};

		rightLegArmX = new float[]{0, 0, 0, 0, 0, 5, 10, 15, 20, 10, 0, 0, 0};
		rightLegArmY = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		rightLegArmZ = new float[]{0, 0, 0, 5, 10, 10, 10, 10, 10, 10, 10, 5, 0};

		float radianConversion = ((float) Math.PI / 180F);

		model.bipedLeftArm.rotateAngleX = getFrameRotation(leftArmX, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedLeftArm.rotateAngleY = getFrameRotation(leftArmY, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedLeftArm.rotateAngleZ = getFrameRotation(leftArmZ, cultivation.getExerciseAnimation()) * radianConversion;

		model.bipedRightArm.rotateAngleX = getFrameRotation(rightArmX, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedRightArm.rotateAngleY = getFrameRotation(rightArmY, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedRightArm.rotateAngleZ = getFrameRotation(rightArmZ, cultivation.getExerciseAnimation()) * radianConversion;

		model.bipedLeftLeg.rotateAngleX = getFrameRotation(lefLegArmX, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedLeftLeg.rotateAngleY = getFrameRotation(lefLegArmY, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedLeftLeg.rotateAngleZ = getFrameRotation(lefLegArmZ, cultivation.getExerciseAnimation()) * radianConversion;

		model.bipedRightLeg.rotateAngleX = getFrameRotation(rightLegArmX, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedRightLeg.rotateAngleY = getFrameRotation(rightLegArmY, cultivation.getExerciseAnimation()) * radianConversion;
		model.bipedRightLeg.rotateAngleZ = getFrameRotation(rightLegArmZ, cultivation.getExerciseAnimation()) * radianConversion;

		model.bipedBody.rotateAngleX = 0;
		model.bipedBody.rotateAngleY = 0;
		model.bipedBody.rotateAngleZ = 0;

		model.bipedHead.rotateAngleX = headPitch * radianConversion;
		model.bipedHead.rotateAngleY = netHeadYaw * radianConversion;
		model.bipedHead.rotateAngleZ = 0;

		model.bipedHeadwear.rotateAngleX = 0;
		model.bipedHeadwear.rotateAngleY = 0;
		model.bipedHeadwear.rotateAngleZ = 0;
	}
}
