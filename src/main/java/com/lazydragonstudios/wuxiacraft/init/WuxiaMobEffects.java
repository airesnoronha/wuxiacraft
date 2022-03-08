package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.effects.WuxiaEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WuxiaMobEffects {

	public static DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MobEffect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<MobEffect> SPIRITUAL_RESONANCE = EFFECTS.register("spiritual_resonance",
			() -> new WuxiaEffect(MobEffectCategory.BENEFICIAL, 0xFAAC11));

	public static RegistryObject<MobEffect> PILL_RESONANCE = EFFECTS.register("pill_resonance",
			() -> new WuxiaEffect(MobEffectCategory.BENEFICIAL, 0xFAAC11));

	public static RegistryObject<MobEffect> ENLIGHTENMENT = EFFECTS.register("enlightenment",
			() -> new WuxiaEffect(MobEffectCategory.BENEFICIAL, 0xFAAC11));
}
