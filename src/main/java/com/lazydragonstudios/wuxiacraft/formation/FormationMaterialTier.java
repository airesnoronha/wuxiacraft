package com.lazydragonstudios.wuxiacraft.formation;

import net.minecraft.world.level.material.Material;

import java.math.BigDecimal;

public enum FormationMaterialTier {

	OAK(new BigDecimal("1"), 2f, Material.WOOD),
	BIRCH(new BigDecimal("1"), 2f, Material.WOOD),
	SPRUCE(new BigDecimal("1"), 2f, Material.WOOD),
	JUNGLE(new BigDecimal("1"), 2f, Material.WOOD),
	ACACIA(new BigDecimal("1"), 2f, Material.WOOD),
	DARK_OAK(new BigDecimal("1"), 2f, Material.WOOD),
	STONE(new BigDecimal("3"), 3f, Material.STONE),
	COPPER(new BigDecimal("5"), 4f, Material.METAL),
	IRON(new BigDecimal("6"), 4f, Material.METAL),
	LAPIS(new BigDecimal("8"), 3f, Material.STONE),
	GOLD(new BigDecimal("10"), 4f, Material.METAL),
	DIAMOND(new BigDecimal("14"), 6f, Material.METAL),
	EMERALD(new BigDecimal("16"), 6f, Material.STONE),
	NETHERITE(new BigDecimal("20"), 8f, Material.STONE);

	public final BigDecimal materialModifier;

	public final float blockStrength;

	public final Material blockMaterial;

	FormationMaterialTier(BigDecimal materialModifier, float blockStrength, Material blockMaterial) {
		this.materialModifier = materialModifier;
		this.blockStrength = blockStrength;
		this.blockMaterial = blockMaterial;
	}
}
