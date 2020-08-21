package com.airesnor.wuxiacraft.aura.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class BaseAuraParticle extends Particle {
	
	private double yaw;

	public BaseAuraParticle(World worldIn, double posXIn, double posYIn, double posZIn, double yaw) {
		super(worldIn, posXIn, posYIn, posZIn);
		super.setMaxAge(6);
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		//super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
}
