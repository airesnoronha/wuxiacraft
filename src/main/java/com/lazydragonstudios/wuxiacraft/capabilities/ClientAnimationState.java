package com.lazydragonstudios.wuxiacraft.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class ClientAnimationState implements IClientAnimationState {

	public static IClientAnimationState get(Player player) {
		var stateOpt = player.getCapability(ClientAnimationStateProvider.ANIMATION_PROVIDER).resolve();
		return stateOpt.orElseGet(ClientAnimationState::new);
	}

	boolean meditating = false;

	boolean exercising = false;

	boolean semiDead = false;

	boolean swordFlight = false;

	int animationFrame = 0;

	@Override
	public boolean isMeditating() {
		return this.meditating;
	}

	@Override
	public boolean isExercising() {
		return this.exercising;
	}

	@Override
	public boolean isSwordFlight() {
		return swordFlight;
	}

	@Override
	public void setSwordFlight(boolean swordFlight) {
		this.swordFlight = swordFlight;
	}

	@Override
	public int getAnimationFrame() {
		return this.animationFrame;
	}

	@Override
	public void resetAnimation() {
		this.animationFrame = 0;
	}

	@Override
	public void setMeditating(boolean meditating) {
		this.meditating = meditating;
	}

	@Override
	public void setExercising(boolean exercising) {
		this.exercising = exercising;
	}

	@Override
	public void advanceAnimationFrame() {
		if (this.meditating || this.exercising || this.semiDead) {
			this.animationFrame++;
		} else {
			this.animationFrame = 0;
		}
		if (this.meditating) {
			this.animationFrame = this.animationFrame > 49 ? 0 : this.animationFrame;
		} else if (this.exercising) {
			this.animationFrame = this.animationFrame > 119 ? 0 : this.animationFrame;
		}
	}

	@Override
	public boolean isSemiDead() {
		return this.semiDead;
	}

	@Override
	public void setSemiDead(boolean semiDead) {
		this.semiDead = semiDead;
	}

	@Override
	public CompoundTag serialize() {
		var tag = new CompoundTag();
		tag.putBoolean("meditating", this.meditating);
		tag.putBoolean("exercising", this.exercising);
		tag.putBoolean("semiDead", this.semiDead);
		tag.putBoolean("swordFlight", this.swordFlight);
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		if (tag.contains("meditating"))
			this.meditating = tag.getBoolean("meditating");
		if (tag.contains("exercising"))
			this.exercising = tag.getBoolean("exercising");
		if (tag.contains("semiDead"))
			this.semiDead = tag.getBoolean("semiDead");
		if (tag.contains("swordFlight"))
			this.swordFlight = tag.getBoolean("swordFlight");
		this.animationFrame = 0;
	}
}
