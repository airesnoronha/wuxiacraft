package com.airesnor.wuxiacraft.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class Materials {

	public static final Material CAULDRON = new Material(MapColor.IRON) {
		@Override
		public boolean blocksMovement() {
			return false;
		}
	};

	public static final Material RUNE = new Material(MapColor.AIR) {
		@Override
		public boolean blocksLight() {
			return false;
		}

		@Override
		public boolean blocksMovement() {
			return false;
		}
	};

}
