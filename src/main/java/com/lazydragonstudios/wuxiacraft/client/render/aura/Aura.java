package com.lazydragonstudios.wuxiacraft.client.render.aura;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public abstract class Aura {

	public abstract void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha);

	public abstract void update();

}
