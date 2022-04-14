package com.lazydragonstudios.wuxiacraft.world;

import com.lazydragonstudios.wuxiacraft.init.WuxiaBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OreGeneration {

	public static Holder<PlacedFeature> SPIRIT_GEN_1;
	public static Holder<PlacedFeature> SPIRIT_GEN_2;
	public static Holder<PlacedFeature> SPIRIT_GEN_3;

	public static void registerOres() {
		OreConfiguration spirit_vein_1 = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, WuxiaBlocks.SPIRIT_STONE_VEIN_1.get().defaultBlockState(), 6);
		OreConfiguration spirit_vein_2 = new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, WuxiaBlocks.SPIRIT_STONE_VEIN_2.get().defaultBlockState(), 4);
		OreConfiguration spirit_vein_3 = new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, WuxiaBlocks.SPIRIT_STONE_VEIN_3.get().defaultBlockState(), 2);
		SPIRIT_GEN_1 = registerPlacedOreFeature("overworld_spirit_vein_1",
				new ConfiguredFeature<>(Feature.ORE, spirit_vein_1),
				CountPlacement.of(20),
				InSquarePlacement.spread(),
				BiomeFilter.biome(),
				HeightRangePlacement.triangle(VerticalAnchor.absolute(10),
						VerticalAnchor.absolute(30))
		);
		SPIRIT_GEN_2 = registerPlacedOreFeature("overworld_spirit_vein_2",
				new ConfiguredFeature<>(Feature.ORE, spirit_vein_2),
				CountPlacement.of(10),
				InSquarePlacement.spread(),
				BiomeFilter.biome(),
				HeightRangePlacement.triangle(VerticalAnchor.absolute(-50),
						VerticalAnchor.absolute(-10))
		);
		SPIRIT_GEN_3 = registerPlacedOreFeature("overworld_spirit_vein_3",
				new ConfiguredFeature<>(Feature.ORE, spirit_vein_3),
				CountPlacement.of(5),
				InSquarePlacement.spread(),
				BiomeFilter.biome(),
				HeightRangePlacement.triangle(VerticalAnchor.absolute(-60),
						VerticalAnchor.absolute(-40))
		);
	}

	private static <C extends FeatureConfiguration, F extends Feature<C>> Holder<PlacedFeature> registerPlacedOreFeature(String registryName, ConfiguredFeature<C, F> feature, PlacementModifier... placementModifiers) {
		return PlacementUtils.register(registryName, Holder.direct(feature), placementModifiers);
	}

	@SubscribeEvent
	public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
		if (event.getCategory() == Biome.BiomeCategory.THEEND || event.getCategory() == Biome.BiomeCategory.NETHER) return;
		event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SPIRIT_GEN_1);
		event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SPIRIT_GEN_2);
		event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, SPIRIT_GEN_3);
	}

}
