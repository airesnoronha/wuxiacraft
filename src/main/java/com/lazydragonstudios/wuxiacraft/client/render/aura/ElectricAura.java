package com.lazydragonstudios.wuxiacraft.client.render.aura;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;

public class ElectricAura extends BaseAura {

	private static final int LIGHTNING_COUNT = 32;

	private final List<LightningStrand> lightnings;

	public ElectricAura() {
		lightnings = new LinkedList<>();
		for (int i = 0; i < LIGHTNING_COUNT; i++) {
			this.lightnings.add(new LightningStrand());
		}
	}


	public void renderSpecial(PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		var vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
		float squareSize = 0.005f;
		float outerSquareSize = 4f;
		for (var strand : this.lightnings) {
			poseStack.pushPose();
			poseStack.translate(strand.posX, strand.posY, strand.posZ);
			for (int i = 0; i < strand.points.size() - 1; i++) {
				var point = strand.points.get(i);
				var next = strand.points.get(i + 1);
				Vector3f[] vertices = new Vector3f[]{
						new Vector3f((float) (point.x - squareSize * outerSquareSize), (float) point.y, (float) (point.z - squareSize * outerSquareSize)),
						new Vector3f((float) (next.x - squareSize * outerSquareSize), (float) next.y, (float) (next.z - squareSize * outerSquareSize)),
						new Vector3f((float) (next.x + squareSize * outerSquareSize), (float) next.y, (float) (next.z - squareSize * outerSquareSize)),
						new Vector3f((float) (point.x + squareSize * outerSquareSize), (float) (point.y), (float) (point.z - squareSize * outerSquareSize)),

						new Vector3f((float) (point.x + squareSize * outerSquareSize), (float) (point.y), (float) (point.z + squareSize * outerSquareSize)),
						new Vector3f((float) (next.x + squareSize * outerSquareSize), (float) (next.y), (float) (next.z + squareSize * outerSquareSize)),
						new Vector3f((float) (next.x - squareSize * outerSquareSize), (float) (next.y), (float) (next.z + squareSize * outerSquareSize)),
						new Vector3f((float) (point.x - squareSize * outerSquareSize), (float) (point.y), (float) (point.z + squareSize * outerSquareSize)),

						new Vector3f((float) (point.x - squareSize * outerSquareSize), (float) (point.y), (float) (point.z - squareSize * outerSquareSize)),
						new Vector3f((float) (next.x - squareSize * outerSquareSize), (float) (next.y), (float) (next.z - squareSize * outerSquareSize)),
						new Vector3f((float) (next.x - squareSize * outerSquareSize), (float) (next.y), (float) (next.z + squareSize * outerSquareSize)),
						new Vector3f((float) (point.x - squareSize * outerSquareSize), (float) (point.y), (float) (point.z + squareSize * outerSquareSize)),

						new Vector3f((float) (point.x + squareSize * outerSquareSize), (float) (point.y), (float) (point.z + squareSize * outerSquareSize)),
						new Vector3f((float) (next.x + squareSize * outerSquareSize), (float) (next.y), (float) (next.z + squareSize * outerSquareSize)),
						new Vector3f((float) (next.x + squareSize * outerSquareSize), (float) (next.y), (float) (next.z - squareSize * outerSquareSize)),
						new Vector3f((float) (point.x + squareSize * outerSquareSize), (float) (point.y), (float) (point.z - squareSize * outerSquareSize)),

						new Vector3f((float) (point.x + squareSize), (float) (point.y), (float) (point.z - squareSize)),
						new Vector3f((float) (next.x + squareSize), (float) (next.y), (float) (next.z - squareSize)),
						new Vector3f((float) (next.x - squareSize), (float) (next.y), (float) (next.z - squareSize)),
						new Vector3f((float) (point.x - squareSize), (float) (point.y), (float) (point.z - squareSize)),

						new Vector3f((float) (point.x + squareSize), (float) (point.y), (float) (point.z + squareSize)),
						new Vector3f((float) (next.x + squareSize), (float) (next.y), (float) (next.z + squareSize)),
						new Vector3f((float) (next.x - squareSize), (float) (next.y), (float) (next.z + squareSize)),
						new Vector3f((float) (point.x - squareSize), (float) (point.y), (float) (point.z + squareSize)),

						new Vector3f((float) (point.x - squareSize), (float) (point.y), (float) (point.z - squareSize)),
						new Vector3f((float) (next.x - squareSize), (float) (next.y), (float) (next.z - squareSize)),
						new Vector3f((float) (next.x - squareSize), (float) (next.y), (float) (next.z + squareSize)),
						new Vector3f((float) (point.x - squareSize), (float) (point.y), (float) (point.z + squareSize)),

						new Vector3f((float) (point.x + squareSize), (float) (point.y), (float) (point.z + squareSize)),
						new Vector3f((float) (next.x + squareSize), (float) (next.y), (float) (next.z + squareSize)),
						new Vector3f((float) (next.x + squareSize), (float) (next.y), (float) (next.z - squareSize)),
						new Vector3f((float) (point.x + squareSize), (float) (point.y), (float) (point.z - squareSize)),
				};
				for(var vertex : vertices) {
					var vertexInWorld = new Vector4f(vertex);
					vertexInWorld.transform(poseStack.last().pose());
					vertexConsumer.vertex(vertexInWorld.x(), vertexInWorld.y(), vertexInWorld.z());
					vertexConsumer.color(red, green, blue, alpha*0.5f);
					vertexConsumer.endVertex();
				}
			}
			poseStack.popPose();
		}
	}

	@Override
	public void update() {
		super.update();
		for (var lightning : this.lightnings) {
			lightning.update();
		}
	}

	private static class LightningStrand {
		public final List<Vec3> points;
		public double motionX;
		public double motionY;
		public double motionZ;
		private double posX;
		private double posY;
		private double posZ;
		private int aliveTime;

		public LightningStrand() {
			this.posY = 0.5f + Math.random() * 1.5f;
			this.posX = (Math.random() * 1.8f - 0.9);
			this.posZ = (Math.random() * 1.8f - 0.9);
			this.motionY = Math.random() * 0.015 * (posY > 1.2 ? -1 : 0);
			this.motionX = Math.random() * 0.006 * (posX > 0 ? -1 : 1);
			this.motionZ = Math.random() * 0.006 * (posZ > 0 ? -1 : 1);
			this.aliveTime = (int) (Math.random() * 15);
			points = new LinkedList<>();
			int pointsSize = 2 + (int) (Math.random() * 4);
			for (int i = 0; i < pointsSize; i++) {
				points.add(new Vec3(Math.random() * 0.2 - 0.1, Math.random() * 0.05 + i * 0.05, Math.random() * 0.2 - 0.1));
			}
		}

		public void update() {
			this.posX += motionX;
			this.posY += motionY;
			this.posZ += motionZ;
			for (int i = 0; i < points.size(); i++) {
				points.set(i, new Vec3(Math.random() * 0.2 - 0.1, Math.random() * 0.05 + i * 0.05, Math.random() * 0.2 - 0.1));
			}
			this.aliveTime++;
			if (this.aliveTime >= 15) {
				this.createNew();
			}
		}

		public void createNew() {
			this.posY = 0.5f + Math.random() * 1.5f;
			this.posX = (Math.random() * 1.8f - 0.9);
			this.posZ = (Math.random() * 1.8f - 0.9);
			this.motionY = Math.random() * 0.015 * (posY > 1.2 ? -1 : 0);
			this.motionX = Math.random() * 0.006 * (posX > 0 ? -1 : 1);
			this.motionZ = Math.random() * 0.006 * (posZ > 0 ? -1 : 1);
			this.aliveTime = (int) (Math.random() * 15);
			points.clear();
			int pointsSize = 2 + (int) (Math.random() * 4);
			for (int i = 0; i < pointsSize; i++) {
				points.add(new Vec3(Math.random() * 0.2 - 0.1, Math.random() * 0.05 + i * 0.05, Math.random() * 0.2 - 0.1));
			}
		}

	}
}
