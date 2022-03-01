package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.*;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class WuxiaTechniqueAspects {

	public static DeferredRegister<TechniqueAspect> ASPECTS = DeferredRegister.create(TechniqueAspect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("aspect_start", StartAspect::new);

	/**
	 * this is an empty aspect should, should not even be mentioned
	 * but is there to serve as default for when the grid is empty and avoid null pointers
	 * plus it says it won't connect to anyone
	 */
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("aspect_empty",
			() -> new TechniqueAspect() {
				@Override
				public boolean canConnect(TechniqueAspect aspect) {
					return false;
				}
			}
	);

	//////////////////////////////////////////
	//           Gathering Nodes            //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> BODY_GATHERING = ASPECTS.register("body_gathering",
			() -> new SystemGather(System.BODY)
	);

	public static RegistryObject<TechniqueAspect> DIVINE_GATHERING = ASPECTS.register("divine_gathering",
			() -> new SystemGather(System.DIVINE)
	);

	public static RegistryObject<TechniqueAspect> ESSENCE_GATHERING = ASPECTS.register("essence_gathering",
			() -> new SystemGather(System.ESSENCE)
	);

	//////////////////////////////////////////
	//           Fire Generation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> EMBER_ASPECT = ASPECTS.register("ember",
			() -> new ElementalGenerator(1d, WuxiaElements.FIRE.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("sparkle", new BigDecimal("5000")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("fire_intent", new BigDecimal("15000")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("remaining_fire", new BigDecimal("35000")))
	);

	//////////////////////////////////////////
	//       Fire Transformation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SCORCH = ASPECTS.register("scorch",
			() -> new ElementSystemConverter(3d, WuxiaElements.FIRE.getId(), System.BODY)
	);

	//////////////////////////////////////////
	//       Wooden Generation ones         //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SEED = ASPECTS.register("seed",
			() -> new ElementalGenerator(1d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> MOSS = ASPECTS.register("moss",
			() -> new ElementalGenerator(3d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> SPROUT = ASPECTS.register("sprout",
			() -> new ElementalGenerator(9d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> HERB = ASPECTS.register("herb",
			() -> new ElementalGenerator(27d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> BUSH = ASPECTS.register("bush",
			() -> new ElementalGenerator(81d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> SAPLING = ASPECTS.register("sapling",
			() -> new ElementalGenerator(243d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> TREE = ASPECTS.register("tree",
			() -> new ElementalGenerator(729d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> ANCIENT_TREE = ASPECTS.register("ancient_tree",
			() -> new ElementalGenerator(2187d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> WORLD_TREE = ASPECTS.register("world_tree",
			() -> new ElementalGenerator(6561d, WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Wood Transformation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> FLOWER = ASPECTS.register("flower",
			() -> new ElementalGenerator(1d, WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Lightning Generation ones      //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SPARK = ASPECTS.register("spark",
			() -> new ElementalGenerator(1d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> CIRCUIT = ASPECTS.register("circuit",
			() -> new ElementalGenerator(3d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> THUNDERING = ASPECTS.register("thundering",
			() -> new ElementalGenerator(9d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> EARTHEN_THUNDER = ASPECTS.register("earthen_thunder",
			() -> new ElementalGenerator(27d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_LIGHTNING = ASPECTS.register("heavenly_lightning",
			() -> new ElementalGenerator(81d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_STORM = ASPECTS.register("immortal_storm",
			() -> new ElementalGenerator(243d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> ENDLESS_LIGHTNING = ASPECTS.register("endless_lightning",
			() -> new ElementalGenerator(729d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> EMBODIMENT_OF_LIGHTNING = ASPECTS.register("embodiment_of_lightning",
			() -> new ElementalGenerator(2187d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> GOD_OF_LIGHTNING = ASPECTS.register("god_of_lightning",
			() -> new ElementalGenerator(6561d, WuxiaElements.LIGHTNING.getId())
	);

	//////////////////////////////////////////
	//       Wooden Special ones            //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> LICHEN = ASPECTS.register("lichen",
			() -> new ElementToElementConverter(3d, 1.5d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> STEM = ASPECTS.register("stem",
			() -> new ElementToElementConverter(5d, 1.7d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> CHARCOAL = ASPECTS.register("charcoal",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.WOOD.getId(), WuxiaElements.FIRE.getId())
	);

	//////////////////////////////////////////
	//       Earth Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> ROOT = ASPECTS.register("root",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.FIRE.getId(), WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Space Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SPACE_TEARING = ASPECTS.register("space_tearing",
			() -> new ElementToSkillConsumer(WuxiaElements.SPACE.getId(), 10d, WuxiaSkillAspects.SPACE_TEAR_ASPECT.getId())
	);

	//////////////////////////////////////////
	//       Light Generation ones          //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> STARRY_BATH = ASPECTS.register("starry_bath",
			() -> new ConditionalElementalGenerator(3d, WuxiaElements.LIGHT.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					var timeOfDay = event.getPlayer().level.getDayTime();
					var techniqueData = Cultivation.get(event.getPlayer()).getSystemData(event.getSystem()).techniqueData;
					var grid = techniqueData.grid;
					int starryBathCount = 0;
					for (var aspect : grid.getGrid().values()) {
						if (aspect == STARRY_BATH.getId()) {
							starryBathCount++;
						}
					}
					var amount = event.getAmount();
					var modifier = new BigDecimal("1");
					if (timeOfDay > 15000 && timeOfDay < 18000) {
						// modifier = modifier +  starryBathCount * 0.5
						modifier = modifier.add(new BigDecimal(starryBathCount).multiply(new BigDecimal("0.5")));
					} else if (timeOfDay > 12000) {
						// modifier = modifier + starryBathCount * 0.2
						modifier = modifier.add(new BigDecimal(starryBathCount).multiply(new BigDecimal("0.2")));
					}
					event.setAmount(amount);
				}
			});

	//////////////////////////////////////////
	//       Light Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> LEAF = ASPECTS.register("leaf",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.LIGHT.getId(), WuxiaElements.WOOD.getId())
	);

}
