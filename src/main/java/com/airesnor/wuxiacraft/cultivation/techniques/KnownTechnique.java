package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.entity.player.EntityPlayer;

public class KnownTechnique {

	private Technique technique;
	private float progress;

	public KnownTechnique(Technique technique, float progress) {
		this.technique = technique;
		this.progress = progress;
	}

	public TechniquesModifiers onUpdate(EntityPlayer player, ICultivation cultivation) {
		if (this.progress >= this.technique.getTier().perfectionProgress + this.technique.getTier().greatProgress + this.technique.getTier().smallProgress) {
			//WuxiaCraft.logger.info("Calling {} perfection update ", technique.getName());
			return this.technique.updatePerfection(player, cultivation);
		} else if (this.progress >= this.technique.getTier().greatProgress + this.technique.getTier().smallProgress) {
			//WuxiaCraft.logger.info("Calling {} great update ", technique.getName());
			return this.technique.updateGreatSuccess(player, cultivation);
		} else if (this.progress >= this.technique.getTier().smallProgress) {
			//WuxiaCraft.logger.info("Calling {} small update ", technique.getName());
			return this.technique.updateSmallSuccess(player, cultivation);
		}
		return new TechniquesModifiers(0f, 0f, 0f, 0f, 0f);
	}

	public void progress(float amount) {
		this.progress = Math.min(Math.max(0, this.progress + amount), this.technique.getTier().smallProgress + this.technique.getTier().greatProgress + this.technique.getTier().perfectionProgress + 10f);
	}

	public float getProgress() {
		return this.progress;
	}

	public Technique getTechnique() {
		return technique;
	}
}
