package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WuxiaParticleTypes {

	public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WuxiaCraft.MOD_ID);

	public static RegistryObject<ParticleType<SimpleParticleType>> BODY_QI_FOG = PARTICLE_TYPES.register("body_qi_fog", () -> new SimpleParticleType(false));
	public static RegistryObject<ParticleType<SimpleParticleType>> DIVINE_QI_FOG = PARTICLE_TYPES.register("divine_qi_fog", () -> new SimpleParticleType(false));
	public static RegistryObject<ParticleType<SimpleParticleType>> ESSENCE_QI_FOG = PARTICLE_TYPES.register("essence_qi_fog", () -> new SimpleParticleType(false));
}
