package com.lazydragonstudios.wuxiacraft.client.particle;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class QiFogParticle extends TextureSheetParticle {

	public QiFogParticle(ClientLevel level, double posX, double posY, double posZ, double deltaX, double deltaY, double deltaZ) {
		super(level, posX, posY, posZ, deltaX, deltaY, deltaZ);
		this.xd = deltaX * (Math.random() * 2.0D - 1.0D);
		this.yd = deltaY * Math.random();
		this.zd = deltaZ * (Math.random() * 2.0D - 1.0D);
		setLifetime(40);

	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}

		public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
			QiFogParticle qiFogParticle = (QiFogParticle) new QiFogParticle(level, x, y, z, xd, yd, zd).scale(5f);
			qiFogParticle.setSpriteFromAge(this.sprites);
			return qiFogParticle;
		}
	}
}
