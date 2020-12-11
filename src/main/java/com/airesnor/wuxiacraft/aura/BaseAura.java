package com.airesnor.wuxiacraft.aura;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;
import java.util.ArrayList;
import java.util.List;

public class BaseAura extends Aura {

	private static final ResourceLocation AURA_TEX = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/effects/base_aura.png");

	protected List<Vector4d> strands; //pos and height which goes from 0 to 0.5 during render time

	private final Vector3f color;


	/**
	 * A check to just update if rendered, because update every aura would get expensive eventually
	 */
	protected boolean dirty = false;

	public BaseAura(String name, float red, float green, float blue) {
		super(name);
		this.color = new Vector3f(red, green, blue);
		strands = new ArrayList<>();
		for (int i = 0; i < 64; i++) {
			strands.add(new Vector4d(Math.random() * 1.2 - 0.6, Math.random() * 1.2 - 0.2, Math.random() * 1.2 - 0.6, Math.random() * 0.9));
		}
	}

	@Override
	public void renderPre(double x, double y, double z) {
		Minecraft.getMinecraft().renderEngine.bindTexture(AURA_TEX);
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableLighting();
		//GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(color.x, color.y, color.z, 0.3f);
		double squareSize = 0.5;
		double maxW = 1.1;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		for (Vector4d strand : strands) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(strand.x, strand.y, strand.z);
			float angle = (float) (Math.atan2(strand.x, strand.z) * 180 / Math.PI);
			GlStateManager.rotate(angle, 0, 1, 0);
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(-squareSize / 2, strand.w, 0.3 * (maxW - strand.w)).tex(0, 1).endVertex();
			builder.pos(-squareSize / 2, strand.w + squareSize, 0.7 * (maxW - strand.w) - 0.2).tex(0, 0).endVertex();
			builder.pos(squareSize / 2, strand.w + squareSize, 0.7 * (maxW - strand.w) - 0.2).tex(1, 0).endVertex();
			builder.pos(squareSize / 2, strand.w, 0.3 * (maxW - strand.w)).tex(1, 1).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableLighting();
		//GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		dirty = true;
	}

	@Override
	public void renderPost(double x, double y, double z) {
		Minecraft.getMinecraft().renderEngine.bindTexture(AURA_TEX);
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableLighting();
		//GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(color.x, color.y, color.z, 0.3f);
		double squareSize = 0.5;
		double maxW = 1.1;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		for (Vector4d strand : strands) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(strand.x, strand.y, strand.z);
			float angle = (float) (Math.atan2(strand.x, strand.z) * 180 / Math.PI);
			GlStateManager.rotate(angle, 0, 1, 0);
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			builder.pos(squareSize / 2, strand.w, 0.3 * (maxW - strand.w)).tex(1, 1).endVertex();
			builder.pos(squareSize / 2, strand.w + squareSize, 0.7 * (maxW - strand.w) - 0.2).tex(1, 0).endVertex();
			builder.pos(-squareSize / 2, strand.w + squareSize, 0.7 * (maxW - strand.w) - 0.2).tex(0, 0).endVertex();
			builder.pos(-squareSize / 2, strand.w, 0.3 * (maxW - strand.w)).tex(0, 1).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableLighting();
		//GlStateManager.enableDepth();
		//GlStateManager.disableBlend(); // would glitch inventory
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		dirty = true;
	}

	@Override
	public void update() {
		if (dirty) {
			double raiseSpeed = 0.08;
			double maxW = 1.1;
			List<Integer> toReplace = new ArrayList<>();
			for (Vector4d strand : strands) {
				strand.w += raiseSpeed;
				if (strand.w >= maxW) {
					toReplace.add(strands.indexOf(strand));
				}
			}
			for (int index : toReplace) {
				strands.set(index, new Vector4d(Math.random() * 1.2 - 0.6, Math.random() * 1.2 - 0.2, Math.random() * 1.2 - 0.6, Math.random() * 0.3));
			}
		}
	}
}
