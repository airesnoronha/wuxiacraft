package com.lazydragonstudios.wuxiacraft.client.render.aura;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.LinkedList;

public class BaseAura extends Aura {

	protected LinkedList<Vector4f> strands;

	private static final int STRAND_COUNT = 64;

	public BaseAura() {
		this.strands = new LinkedList<>();
		for (int i = 0; i < STRAND_COUNT; i++) {
			strands.add(createNewStrand());
		}
	}

	protected static Vector4f createNewStrand() {
		return new Vector4f((float) (Math.random() * 1.2 - 0.6), (float) (Math.random() * 1.2 - 0.2), (float) (Math.random() * 1.2 - 0.6), (float) (Math.random() * 0.9));
	}

	@Override
	public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		poseStack.pushPose();
		float squareSize = 0.5f;
		float maxW = 1.1f;
		for (var strand : this.strands) {
			poseStack.pushPose();
			poseStack.translate(strand.x(), strand.y(), strand.z());
			float angle = (float) (Math.atan2(strand.x(), strand.z()));
			poseStack.mulPose(Vector3f.YP.rotation(angle));
			//var poseNormals = poseStack.last().normal();
			var strandNormals = new Vector3f(0, 0, 1f);
			strandNormals.transform(poseStack.last().normal());
			Vector3f[] verticesPositions = new Vector3f[]{
					new Vector3f(-squareSize / 2f, strand.w(), 0.3f * (maxW - strand.w())),
					new Vector3f(-squareSize / 2f, strand.w() + squareSize, 0.7f * (maxW - strand.w()) - 0.2f),
					new Vector3f(squareSize / 2f, strand.w() + squareSize, 0.7f * (maxW - strand.w()) - 0.2f),
					new Vector3f(squareSize / 2f, strand.w(), 0.3f * (maxW - strand.w()))
			};
			Vec2[] uvPositions = new Vec2[]{
					new Vec2(0, 1),
					new Vec2(0, 0),
					new Vec2(1, 0),
					new Vec2(1, 1)
			};
			for (int i = 0; i < 4; i++) {
				var strandVertex = verticesPositions[i];
				var uv = uvPositions[i];
				var vertexPosInWorld = new Vector4f(strandVertex.x(), strandVertex.y(), strandVertex.z(), 1f);
				vertexPosInWorld.transform(poseStack.last().pose());
				vertexConsumer.vertex(vertexPosInWorld.x(), vertexPosInWorld.y(), vertexPosInWorld.z(), red, green, blue*0.1f, alpha*0.3f, uv.x, uv.y, packedOverlayIn, packedLightIn, strandNormals.x(), strandNormals.y(), strandNormals.z());
			}
			poseStack.popPose();
		}
		poseStack.popPose();
	}

	@Override
	public void update() {
		float raiseSpeed = 0.04f;
		float maxW = 1.1f;
		for (var strand : this.strands) {
			strand.setW(strand.w() + raiseSpeed);
			if (strand.w() > maxW) {
				var newStrand = createNewStrand();
				strand.setX(newStrand.x());
				strand.setY(newStrand.y());
				strand.setZ(newStrand.z());
				strand.setW(newStrand.w());
			}
		}
	}
}
