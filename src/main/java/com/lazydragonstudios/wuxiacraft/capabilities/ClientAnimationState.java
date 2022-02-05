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
		this.animationFrame++;
	}

	@Override
	public CompoundTag serialize() {
		var tag = new CompoundTag();
		tag.putBoolean("meditating", this.meditating);
		tag.putBoolean("exercising", this.exercising);
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		if (tag.contains("meditating"))
			meditating = tag.getBoolean("meditating");
		if (tag.contains("exercising"))
			exercising = tag.getBoolean("exercising");
		this.animationFrame = 0;
	}
}
