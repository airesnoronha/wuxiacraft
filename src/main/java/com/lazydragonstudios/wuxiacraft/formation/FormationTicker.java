package com.lazydragonstudios.wuxiacraft.formation;

import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.client.WorldRenderHandler;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.init.WuxiaParticleTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.HashMap;

@ParametersAreNonnullByDefault
public class FormationTicker implements BlockEntityTicker<FormationCore> {

	public static HashMap<System, ParticleType<SimpleParticleType>> PARTICLE_BY_SYSTEM = new HashMap<>();

	static {
		PARTICLE_BY_SYSTEM.put(System.BODY, WuxiaParticleTypes.BODY_QI_FOG.get());
		PARTICLE_BY_SYSTEM.put(System.DIVINE, WuxiaParticleTypes.DIVINE_QI_FOG.get());
		PARTICLE_BY_SYSTEM.put(System.ESSENCE, WuxiaParticleTypes.ESSENCE_QI_FOG.get());
	}

	@Override
	public void tick(Level level, BlockPos pos, BlockState blockState, FormationCore core) {
		if (core.scheduleActivation) {
			core.activate(core.owner);
		}
		if (!core.isActive()) return;
		var owner = core.getOwner();
		if (owner != null && !level.isClientSide()) {
			var cultivation = Cultivation.get(owner);
			if (cultivation.getFormation() != null) {
				if (cultivation.getFormation().compareTo(pos) != 0) {
					core.deactivate();
					return;
				}
			} else {
				core.deactivate();
				return;
			}
		}
		var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
		var barrierMaxAmount = core.getStat(FormationStat.BARRIER_MAX_AMOUNT);
		var barrierRegen = core.getStat(FormationStat.BARRIER_REGEN);
		if (barrierAmount.compareTo(barrierMaxAmount) < 0) {
			core.setStat(FormationStat.BARRIER_AMOUNT, barrierAmount.add(barrierRegen).min(barrierMaxAmount));
		}
		if (barrierAmount.compareTo(BigDecimal.ZERO) > 0) {
			var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).intValue();
			var aabb = new AABB(core.getBlockPos()).inflate(barrierRange);
			var entities = level.getEntities(core.getOwner(),
					aabb, entity ->
							pos.distSqr(entity.getX(), entity.getY(), entity.getZ(), true) < barrierRange * barrierRange &&
									(entity instanceof LivingEntity ||
											(entity instanceof Projectile proj && proj.getOwner() != core.getOwner())));
			for (var entity : entities) {
				Vector3f movement = new Vector3f((float) (entity.getX() - pos.getX()), (float) (entity.getY() - pos.getY()), (float) (entity.getZ() - pos.getZ()));
				movement.normalize();
				movement.mul(0.6f);
				entity.setDeltaMovement(movement.x(), movement.y() + 0.007f, movement.z());
			}
		}
		if (!level.isClientSide) return;
		for (var system : System.values()) {
			var systemEnergyRegen = core.getStat(system, FormationSystemStat.ENERGY_REGEN);
			if (systemEnergyRegen.compareTo(BigDecimal.ZERO) <= 0) continue;
			var range = core.getStat(system, FormationSystemStat.ENERGY_REGEN_RANGE).doubleValue();
			int particles = Math.max(1, (int) (systemEnergyRegen.doubleValue() / 0.02f));
			particles = Math.min(particles, 40);
			for (int i = 0; i < particles; i++) {
				level.addParticle((ParticleOptions) PARTICLE_BY_SYSTEM.get(system),
						pos.getX() + 0.5d + Math.random() * 2 * range - range,
						pos.getY() + 0.5d + Math.random() * 2 * range - range,
						pos.getZ() + 0.5d + Math.random() * 2 * range - range,
						0d, 0.1d, 0d);
			}
		}
		renderClientSide(core);
	}

	@OnlyIn(Dist.CLIENT)
	public void renderClientSide(FormationCore core) {
		if (!core.isActive()) return;
		var barrierAmount = core.getStat(FormationStat.BARRIER_AMOUNT);
		if (barrierAmount.compareTo(BigDecimal.ZERO) <= 0) return;
		WorldRenderHandler.LEVEL_RENDER_QUEUE.add(1, (poseStack, partialTick) -> {
			poseStack.pushPose();
			//poseStack.setIdentity();
			var pos = core.getBlockPos();
			//poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
			Tesselator tesselator = Tesselator.getInstance();
			BufferBuilder builder = tesselator.getBuilder();
			var player = Minecraft.getInstance().player;
			if (player == null) return;
			Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
			var camPos = camera.getPosition();
			poseStack.translate(pos.getX() - camPos.x, pos.getY() - camPos.y, pos.getZ() - camPos.z);
			RenderSystem.enableBlend();
			RenderSystem.enableTexture();
			//RenderSystem.disableCull();
			var barrierRange = core.getStat(FormationStat.BARRIER_RANGE).intValue();
			var distSqr = pos.distSqr(player.getX(), player.getY(), player.getZ(), true);
			if (distSqr <= barrierRange * barrierRange) {
				RenderSystem.disableCull();
			}
			renderUVSphere(poseStack, tesselator, builder, barrierRange);
			poseStack.popPose();
		});
	}

	private static void renderUVSphere(PoseStack poseStack, Tesselator tesselator, BufferBuilder bufferBuilder, double radius) {
		int verticalSections = 32;
		int horizontalSections = 64;
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		for (int i = 1; i < verticalSections - 1; i++) {
			double y0 = Math.cos(Math.PI * (double) i / (double) verticalSections);
			double y0Radius = radius * Math.sin(Math.PI * (double) i / (double) verticalSections);
			double y1 = Math.cos(Math.PI * (double) (i + 1) / (double) verticalSections);
			double y1Radius = radius * Math.sin(Math.PI * (double) (i + 1) / (double) verticalSections);
			for (int j = 0; j < horizontalSections; j++) {
				double x0Pos = Math.cos(2 * Math.PI * (double) (j) / (double) horizontalSections);
				double x1Pos = Math.cos(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
				double z0Pos = Math.sin(2 * Math.PI * (double) (j) / (double) horizontalSections);
				double z1Pos = Math.sin(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
				Vector3f[] vertices = new Vector3f[]{
						new Vector3f((float) (x0Pos * y0Radius), (float) (y0 * radius), (float) (z0Pos * y0Radius)),
						new Vector3f((float) (x1Pos * y0Radius), (float) (y0 * radius), (float) (z1Pos * y0Radius)),
						new Vector3f((float) (x1Pos * y1Radius), (float) (y1 * radius), (float) (z1Pos * y1Radius)),
						new Vector3f((float) (x0Pos * y1Radius), (float) (y1 * radius), (float) (z0Pos * y1Radius)),
				};
				for (var vertex : vertices) {
					var vertexPosInWorld = new Vector4f(vertex.x(), vertex.y(), vertex.z(), 1f);
					vertexPosInWorld.transform(poseStack.last().pose());
					bufferBuilder.vertex(vertexPosInWorld.x(), vertexPosInWorld.y(), vertexPosInWorld.z()).color(0x4CFFFFFF).endVertex();
				}
			}
		}
		tesselator.end();
		bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		var vertexPosInWorldTop = new Vector4f(0, (float) radius, 0, 1f);
		vertexPosInWorldTop.transform(poseStack.last().pose());
		bufferBuilder.vertex(vertexPosInWorldTop.x(), vertexPosInWorldTop.y(), vertexPosInWorldTop.z()).color(0x4CFFFFFF).endVertex();
		double y0 = Math.cos(Math.PI / (double) verticalSections);
		double y0Radius = radius * Math.sin(Math.PI / (double) verticalSections);
		for (int j = horizontalSections; j >= 0; j--) {
			double x0Pos = Math.cos(2 * Math.PI * (double) (j) / (double) horizontalSections);
			double z0Pos = Math.sin(2 * Math.PI * (double) (j) / (double) horizontalSections);
			var vertexPosInWorld = new Vector4f((float) (x0Pos * y0Radius), (float) (y0 * radius), (float) (z0Pos * y0Radius), 1f);
			vertexPosInWorld.transform(poseStack.last().pose());
			bufferBuilder.vertex(vertexPosInWorld.x(), vertexPosInWorld.y(), vertexPosInWorld.z()).color(0x4CFFFFFF).endVertex();
		}
		tesselator.end();
	}

}
