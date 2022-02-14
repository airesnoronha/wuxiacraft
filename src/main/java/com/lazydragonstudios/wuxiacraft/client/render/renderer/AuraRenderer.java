package com.lazydragonstudios.wuxiacraft.client.render.renderer;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.client.render.WuxiaRenderTypes;
import com.lazydragonstudios.wuxiacraft.client.render.aura.Aura;
import com.lazydragonstudios.wuxiacraft.client.render.aura.BaseAura;
import com.lazydragonstudios.wuxiacraft.client.render.aura.ElectricAura;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AuraRenderer extends EntityRenderer<AbstractClientPlayer> {

	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aura/base_aura.png");

	public static EntityType<AbstractClientPlayer> auraEntityType;

	private Aura aura;

	public AuraRenderer(EntityRendererProvider.Context p_174008_) {
		super(p_174008_);
		aura = new BaseAura();
	}

	public void setAura(Aura aura) {
		this.aura = aura;
	}

	@Override
	public ResourceLocation getTextureLocation(AbstractClientPlayer player) {
		return TEXTURE_LOCATION;
	}

	@Override
	public void render(AbstractClientPlayer player, float notKnown, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
		var renderType = WuxiaRenderTypes.getAuraRenderer.apply(this.getTextureLocation(player));
		var vertexConsumer = bufferSource.getBuffer(renderType);
		int overlay = LivingEntityRenderer.getOverlayCoords(player, 0f); //guess this is used for creepers
		aura.update();
		aura.render(poseStack, vertexConsumer, packedLight, overlay, 1f, 1f, 1f, 1f);
		if (this.aura instanceof ElectricAura eAura) {
			eAura.renderSpecial(poseStack, bufferSource, packedLight, overlay, 0.4f, 0.1f, 0.8f, 1f);
		}
		poseStack.popPose();
	}
}
