package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.HashMap;
import java.util.LinkedList;

public class ElementToMobEffectConsumer extends ElementalConsumer {

	public net.minecraft.world.effect.MobEffect effect;
	public int amplifier;
	public int duration; //in ticks
	public boolean ambient;
	public boolean particles;

	public ElementToMobEffectConsumer(ResourceLocation element, double cost, net.minecraft.world.effect.MobEffect effect, int amplifier, int duration, boolean ambient, boolean particles) {
		super(element, cost);
		this.effect = effect;
		this.amplifier = amplifier;
		this.duration = duration;
		this.ambient = ambient;
		this.particles = particles;
	}

	public ElementToMobEffectConsumer(ResourceLocation element, double cost, MobEffectInstance mobEffect) {
		super(element, cost);
		this.effect = mobEffect.getEffect();
		this.amplifier = mobEffect.getAmplifier();
		this.duration = mobEffect.getDuration();
		this.ambient = mobEffect.isAmbient();
		this.particles = mobEffect.isVisible();
	}

	@Override
	public void consumed(HashMap<String, Object> metaData) {
		if (!metaData.containsKey("mob-effects")) {
			metaData.put("mob-effects", new LinkedList<>());
		}
		//noinspection unchecked
		var mobEffects = (LinkedList<Object>) metaData.get("mob-effects");
		HashMap<String, Object> mobEffectDescription = new HashMap<>();
		mobEffectDescription.put("effect", this.effect);
		mobEffectDescription.put("amplifier", this.amplifier);
		mobEffectDescription.put("duration", this.duration);
		mobEffectDescription.put("ambient", this.ambient);
		mobEffectDescription.put("particles", this.particles);
		mobEffects.add(mobEffectDescription);
	}

	@Override
	public void notConsumed(HashMap<String, Object> metaData) {

	}

	public static MobEffectInstance getMobEffectInstance(ElementToMobEffectConsumer aspect) {
		return new MobEffectInstance(aspect.effect, aspect.amplifier, aspect.duration, aspect.ambient, aspect.particles);
	}
}
