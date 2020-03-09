package com.airesnor.wuxiacraft.items;

public class ItemFan extends ItemBase {

	private float fanStrength;
	private float maxFanStrength;

	public ItemFan(String name) {
		super(name);
		this.fanStrength = 1f;
		this.maxFanStrength = 10f;
		setMaxStackSize(1);
	}

	public ItemFan setStrength(float strength) {
		this.fanStrength = strength;
		return this;
	}

	public ItemFan setMaxStrength(float strength) {
		this.maxFanStrength = strength;
		return this;
	}

	public float getFanStrength() {
		return fanStrength;
	}

	public float getMaxFanStrength() {
		return maxFanStrength;
	}
}
