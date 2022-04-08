package com.lazydragonstudios.wuxiacraft.capabilities;

import net.minecraft.nbt.CompoundTag;

public interface IClientAnimationState {

	/**
	 * @return whether the entity is actually meditating for rendering the lotus position
	 */
	boolean isMeditating();

	/**
	 * whether the entity is exercising, so we can get the exercising animation
	 *
	 * @return
	 */
	boolean isExercising();

	boolean isSemiDead();

	boolean isSwordFlight();

	void setSwordFlight(boolean swordFlight);

	/**
	 * A track to the frames of the animation
	 *
	 * @return
	 */
	int getAnimationFrame();

	/**
	 * This will reset the animation frame counter to restart the animation
	 */
	void resetAnimation();

	void setMeditating(boolean meditating);

	void setExercising(boolean exercising);

	void setSemiDead(boolean semiDead);

	void advanceAnimationFrame();

	/**
	 * This is basically to send to the remote clients about the animation state of someone
	 *
	 * @return the animation state
	 */
	CompoundTag serialize();

	/**
	 * This is basically to understand changes in the animation state of some remote player
	 *
	 * @param tag the animation state
	 */
	void deserialize(CompoundTag tag);

}
