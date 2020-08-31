package com.airesnor.wuxiacraft.aura;

import com.sun.javafx.geom.Vec3f;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ElectricAura extends Aura {

	private static class LightningStrand {
		public final List<Vec3d> points;
		public final double motionX;
		public final double motionY;
		public final double motionZ;
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
			points = new ArrayList<>();
			int pointsSize = 2 + (int) (Math.random() * 4);
			for (int i = 0; i < pointsSize; i++) {
				points.add(new Vec3d(Math.random() * 0.2 - 0.1, Math.random() * 0.05 + i * 0.05, Math.random() * 0.2 - 0.1));
			}
		}

		public void update() {
			this.posX += motionX;
			this.posY += motionY;
			this.posZ += motionZ;
			this.aliveTime++;
			for (int i = 0; i < points.size(); i++) {
				points.set(i, new Vec3d(Math.random() * 0.2 - 0.1, Math.random() * 0.05 + i * 0.05, Math.random() * 0.2 - 0.1));
			}
		}

	}

	private final List<LightningStrand> strands;

	private final Vec3f color;

	private boolean dirty;

	public ElectricAura(String name, float red, float green, float blue) {
		super(name);
		this.color = new Vec3f(red, green, blue);
		this.strands = new ArrayList<>();
		for (int i = 0; i < 32; i++) {
			this.strands.add(new LightningStrand());
		}
	}

	@Override
	public void renderPre(double x, double y, double z) {
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		double squareSize = 0.005;
		double outerSquareSize = 4;
		GlStateManager.glLineWidth(3f);
		for (LightningStrand strand : strands) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(strand.posX, strand.posY, strand.posZ);
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			for (int i = 0; i < strand.points.size() - 1; i++) {
				Vec3d point = strand.points.get(i);
				Vec3d next = strand.points.get(i + 1);
				builder.pos(point.x - squareSize * outerSquareSize, point.y, point.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x - squareSize * outerSquareSize, next.y, next.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x + squareSize * outerSquareSize, next.y, next.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(point.x + squareSize * outerSquareSize, point.y, point.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();

				builder.pos(point.x + squareSize * outerSquareSize, point.y, point.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x + squareSize * outerSquareSize, next.y, next.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x - squareSize * outerSquareSize, next.y, next.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(point.x - squareSize * outerSquareSize, point.y, point.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();

				builder.pos(point.x - squareSize * outerSquareSize, point.y, point.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x - squareSize * outerSquareSize, next.y, next.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x - squareSize * outerSquareSize, next.y, next.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(point.x - squareSize * outerSquareSize, point.y, point.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();

				builder.pos(point.x + squareSize * outerSquareSize, point.y, point.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x + squareSize * outerSquareSize, next.y, next.z + squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(next.x + squareSize * outerSquareSize, next.y, next.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();
				builder.pos(point.x + squareSize * outerSquareSize, point.y, point.z - squareSize * outerSquareSize).color(color.x, color.y, color.z, 0.3f).endVertex();

				builder.pos(point.x - squareSize, point.y, point.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x - squareSize, next.y, next.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x + squareSize, next.y, next.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(point.x + squareSize, point.y, point.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();

				builder.pos(point.x + squareSize, point.y, point.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x + squareSize, next.y, next.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x - squareSize, next.y, next.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(point.x - squareSize, point.y, point.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();

				builder.pos(point.x - squareSize, point.y, point.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x - squareSize, next.y, next.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x - squareSize, next.y, next.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(point.x - squareSize, point.y, point.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();

				builder.pos(point.x + squareSize, point.y, point.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x + squareSize, next.y, next.z + squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(next.x + squareSize, next.y, next.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
				builder.pos(point.x + squareSize, point.y, point.z - squareSize).color(1f, 1f, 1f, 0.5f).endVertex();
			}
			tessellator.draw();
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		dirty = true;
	}

	@Override
	public void renderPost(double x, double y, double z) {
	}

	@Override
	public void update() {
		if (dirty) {
			List<Integer> toReplace = new ArrayList<>();
			for (LightningStrand strand : strands) {
				strand.update();
				if (strand.aliveTime > 15) {
					toReplace.add(strands.indexOf(strand));
				}
			}
			for (int index : toReplace) {
				this.strands.set(index, new LightningStrand());
			}
		}
	}
}
