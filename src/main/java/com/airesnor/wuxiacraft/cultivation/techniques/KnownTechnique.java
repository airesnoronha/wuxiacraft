package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class KnownTechnique {

	private final Technique technique;
	private double progress;

	public KnownTechnique(Technique technique, double progress) {
		this.technique = technique;
		this.progress = progress;
	}

	public void progress(double amount) {
		this.progress = Math.min(Math.max(0, this.progress + amount), this.technique.getTier().smallProgress + this.technique.getTier().greatProgress + this.technique.getTier().perfectionProgress + 10f);
	}

	public double getProgress() {
		return this.progress;
	}

	public Technique getTechnique() {
		return technique;
	}

	public List<PotionEffect> getTechniqueEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		TechniqueTier tier = this.getTechnique().getTier();
		if(this.progress >= tier.smallProgress) {
			effects.addAll(this.getTechnique().getSmallCompletionEffects());
		}
		if(this.progress >= tier.smallProgress + tier.greatProgress) {
			effects.addAll(this.getTechnique().getGreatCompletionEffects());
		}
		if(this.progress >= tier.smallProgress + tier.greatProgress + tier.perfectionProgress) {
			effects.addAll(this.getTechnique().getPerfectionCompletionEffects());
		}
		return effects;
	}
}
