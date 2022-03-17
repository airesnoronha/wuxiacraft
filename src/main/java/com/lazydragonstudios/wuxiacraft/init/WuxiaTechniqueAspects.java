package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.*;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import com.lazydragonstudios.wuxiacraft.util.TechniqueUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class WuxiaTechniqueAspects {

	public static DeferredRegister<TechniqueAspect> ASPECTS = DeferredRegister.create(TechniqueAspect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("start", StartAspect::new);

	/**
	 * this is an empty aspect should, should not even be mentioned
	 * but is there to serve as default for when the grid is empty and avoid null pointers
	 * plus it says it won't connect to anyone
	 */
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("empty",
			() -> new TechniqueAspect() {
				@Override
				public boolean canConnect(TechniqueAspect aspect) {
					return false;
				}
			}
	);

	public static RegistryObject<TechniqueAspect> UNKNOWN = ASPECTS.register("unknown",
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

	public static RegistryObject<TechniqueAspect> CINDER = ASPECTS.register("cinder",
			() -> new ElementalGenerator(1d, WuxiaElements.FIRE.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("cinder_basic", new BigDecimal("100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("cinder_intermediate", new BigDecimal("250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("cinder_advanced", new BigDecimal("500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("cinder_expert", new BigDecimal("1000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> EMBER = ASPECTS.register("ember",
			() -> new ElementalGenerator(3d, WuxiaElements.FIRE.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("ember_basic", new BigDecimal("300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ember_intermediate", new BigDecimal("750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ember_advanced", new BigDecimal("1500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ember_expert", new BigDecimal("3000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "blazing"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> BLAZE = ASPECTS.register("blazing",
			() -> new ElementalGenerator(9d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "cinder")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("blazing_basic", new BigDecimal("900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("blazing_intermediate", new BigDecimal("2250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("blazing_advanced", new BigDecimal("4500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("blazing_expert", new BigDecimal("9000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "earth_scorching"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> EARTH_SCORCHING = ASPECTS.register("earth_scorching",
			() -> new ElementalGenerator(27d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earth_scorching_basic", new BigDecimal("2700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earth_scorching_intermediate", new BigDecimal("6750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earth_scorching_advanced", new BigDecimal("13500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earth_scorching_expert", new BigDecimal("27000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heaven_burning_fire"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> HEAVEN_BURNING_FIRE = ASPECTS.register("heaven_burning_fire",
			() -> new ElementalGenerator(81d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "blazing")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heaven_burning_fire_basic", new BigDecimal("8100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heaven_burning_fire_intermediate", new BigDecimal("20250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heaven_burning_fire_advanced", new BigDecimal("40500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heaven_burning_fire_expert", new BigDecimal("81000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "everlasting_flame"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> EVERLASTING_FLAME = ASPECTS.register("everlasting_flame",
			() -> new ElementalGenerator(243d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "earth_scorching")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("everlasting_flame_basic", new BigDecimal("24300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("everlasting_flame_intermediate", new BigDecimal("60750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("everlasting_flame_advanced", new BigDecimal("121500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("everlasting_flame_expert", new BigDecimal("243000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "solar_flame"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> SOLAR_FLAME = ASPECTS.register("solar_flame",
			() -> new ElementalGenerator(729d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heaven_burning_fire")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("solar_flame_basic", new BigDecimal("72900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("solar_flame_intermediate", new BigDecimal("182250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("solar_flame_advanced", new BigDecimal("364500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("solar_flame_expert", new BigDecimal("729000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "immortal_flame"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_FLAME = ASPECTS.register("immortal_flame",
			() -> new ElementalGenerator(2187d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "everlasting_flame")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_flame_basic", new BigDecimal("218700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_flame_intermediate", new BigDecimal("564750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_flame_advanced", new BigDecimal("1093500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_flame_expert", new BigDecimal("2187000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "flame_emperor"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> FLAME_EMPEROR = ASPECTS.register("flame_emperor",
			() -> new ElementalGenerator(6561d, WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "immortal_flame")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_basic", new BigDecimal("656100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_intermediate", new BigDecimal("1640250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_advanced", new BigDecimal("3280500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_expert", new BigDecimal("6561000"), new BigDecimal("0.4")))
	);

	//////////////////////////////////////////
	//       Fire Transformation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SCORCH = ASPECTS.register("scorch",
			() -> new ElementSystemConverter(3d, WuxiaElements.FIRE.getId(), System.BODY)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "cinder"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("scorch_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("scorch_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("scorch_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("scorch_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> MAGIC_BURNING = ASPECTS.register("magic_burning",
			() -> new ElementSystemConverter(3d, WuxiaElements.FIRE.getId(), System.ESSENCE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "cinder"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magic_burning_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magic_burning_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magic_burning_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magic_burning_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> MIND_FLARE = ASPECTS.register("mind_flare",
			() -> new ElementSystemConverter(3d, WuxiaElements.FIRE.getId(), System.DIVINE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "cinder"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("mind_flare_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("mind_flare_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("mind_flare_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("mind_flare_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	//////////////////////////////////////////
	//           Water Generation ones      //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> DROP = ASPECTS.register("drop",
			() -> new ElementalGenerator(1d, WuxiaElements.WATER.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("drop_basic", new BigDecimal("100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("drop_intermediate", new BigDecimal("250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("drop_advanced", new BigDecimal("500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("drop_expert", new BigDecimal("1000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "flow"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> FLOW = ASPECTS.register("flow",
			() -> new ElementalGenerator(3d, WuxiaElements.WATER.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("flow_basic", new BigDecimal("300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flow_intermediate", new BigDecimal("750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flow_advanced", new BigDecimal("1500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flow_expert", new BigDecimal("3000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "waterfall"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> WATERFALL = ASPECTS.register("waterfall",
			() -> new ElementalGenerator(9d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "drop")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waterfall_basic", new BigDecimal("900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waterfall_intermediate", new BigDecimal("2250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waterfall_advanced", new BigDecimal("4500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waterfall_expert", new BigDecimal("9000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ocean_heart"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> OCEAN_HEART = ASPECTS.register("ocean_heart",
			() -> new ElementalGenerator(27d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "flow")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_heart_basic", new BigDecimal("2700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_heart_intermediate", new BigDecimal("6750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_heart_advanced", new BigDecimal("13500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_heart_expert", new BigDecimal("27000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heavenly_water"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_WATER = ASPECTS.register("heavenly_water",
			() -> new ElementalGenerator(81d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "waterfall")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_water_basic", new BigDecimal("8100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_water_intermediate", new BigDecimal("20250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_water_advanced", new BigDecimal("40500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_water_expert", new BigDecimal("81000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ocean_tide"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> OCEAN_TIDE = ASPECTS.register("ocean_tide",
			() -> new ElementalGenerator(243d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ocean_heart")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_tide_basic", new BigDecimal("24300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_tide_intermediate", new BigDecimal("60750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_tide_advanced", new BigDecimal("121500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ocean_tide_expert", new BigDecimal("243000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "lunar_water"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> LUNAR_WATER = ASPECTS.register("lunar_water",
			() -> new ElementalGenerator(729d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heavenly_water")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("lunar_water_basic", new BigDecimal("72900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("lunar_water_intermediate", new BigDecimal("182250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("lunar_water_advanced", new BigDecimal("364500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("lunar_water_expert", new BigDecimal("729000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "immortal_water"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_WATER = ASPECTS.register("immortal_water",
			() -> new ElementalGenerator(2187d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ocean_tide")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_water_basic", new BigDecimal("218700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_water_intermediate", new BigDecimal("564750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_water_advanced", new BigDecimal("1093500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_water_expert", new BigDecimal("2187000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "god_of_water"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> GOD_OF_WATER = ASPECTS.register("god_of_water",
			() -> new ElementalGenerator(6561d, WuxiaElements.WATER.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "lunar_water")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_water_basic", new BigDecimal("656100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_water_intermediate", new BigDecimal("1640250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_water_advanced", new BigDecimal("3280500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_water_expert", new BigDecimal("6561000"), new BigDecimal("0.4")))
	);

	//////////////////////////////////////////
	//       Water Transformation ones      //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> SPLASH = ASPECTS.register("splash",
			() -> new ElementSystemConverter(3d, WuxiaElements.WATER.getId(), System.BODY)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "drop"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "flow")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("splash_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("splash_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("splash_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("splash_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> STREAM = ASPECTS.register("stream",
			() -> new ElementSystemConverter(3d, WuxiaElements.WATER.getId(), System.ESSENCE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "drop"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("stream_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("stream_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("stream_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("stream_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> WAVING = ASPECTS.register("waving",
			() -> new ElementSystemConverter(3d, WuxiaElements.WATER.getId(), System.DIVINE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "drop"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "flow")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waving_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waving_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waving_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("waving_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	//////////////////////////////////////////
	//       Water Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SNOW = ASPECTS.register("snow",
			() -> new ConditionalElementalGenerator(3d, WuxiaElements.WATER.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					if (event.getPlayer().getTicksFrozen() > 0) {
						event.setAmount(event.getAmount().multiply(new BigDecimal("1.2")));
					}
				}
			}
					.addCheckpoint(new TechniqueAspect.Checkpoint("freeze_basic", new BigDecimal("300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("freeze_intermediate", new BigDecimal("750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("freeze_advanced", new BigDecimal("1500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("freeze_expert", new BigDecimal("3000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "waterfall"), cultivation)))
	);

	//////////////////////////////////////////
	//       Wood Generation ones           //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SEED = ASPECTS.register("seed",
			() -> new ElementalGenerator(1d, WuxiaElements.WOOD.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("seed_basic", new BigDecimal("100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("seed_intermediate", new BigDecimal("250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("seed_advanced", new BigDecimal("500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("seed_expert", new BigDecimal("1000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "moss"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> MOSS = ASPECTS.register("moss",
			() -> new ElementalGenerator(3d, WuxiaElements.WOOD.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("moss_basic", new BigDecimal("300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("moss_intermediate", new BigDecimal("750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("moss_advanced", new BigDecimal("1500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("moss_expert", new BigDecimal("3000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "sprout"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> SPROUT = ASPECTS.register("sprout",
			() -> new ElementalGenerator(9d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "seed")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sprout_basic", new BigDecimal("900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sprout_intermediate", new BigDecimal("2250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sprout_advanced", new BigDecimal("4500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sprout_expert", new BigDecimal("9000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "herb"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> HERB = ASPECTS.register("herb",
			() -> new ElementalGenerator(27d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "moss")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("herb_basic", new BigDecimal("2700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("herb_intermediate", new BigDecimal("6750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("herb_advanced", new BigDecimal("13500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("herb_expert", new BigDecimal("27000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "bush"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> BUSH = ASPECTS.register("bush",
			() -> new ElementalGenerator(81d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "sprout")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bush_basic", new BigDecimal("8100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bush_intermediate", new BigDecimal("20250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bush_advanced", new BigDecimal("40500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bush_expert", new BigDecimal("81000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "sapling"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> SAPLING = ASPECTS.register("sapling",
			() -> new ElementalGenerator(243d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "herb")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sapling_basic", new BigDecimal("24300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sapling_intermediate", new BigDecimal("60750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sapling_advanced", new BigDecimal("121500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sapling_expert", new BigDecimal("243000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "tree"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> TREE = ASPECTS.register("tree",
			() -> new ElementalGenerator(729d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "bush")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("tree_basic", new BigDecimal("72900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("tree_intermediate", new BigDecimal("182250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("tree_advanced", new BigDecimal("364500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("tree_expert", new BigDecimal("729000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ancient_tree"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> ANCIENT_TREE = ASPECTS.register("ancient_tree",
			() -> new ElementalGenerator(2187d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "sapling")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_tree_basic", new BigDecimal("218700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_tree_intermediate", new BigDecimal("564750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_tree_advanced", new BigDecimal("1093500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_tree_expert", new BigDecimal("2187000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "world_tree"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> WORLD_TREE = ASPECTS.register("world_tree",
			() -> new ElementalGenerator(6561d, WuxiaElements.WOOD.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "tree")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("world_tree_basic", new BigDecimal("656100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("world_tree_intermediate", new BigDecimal("1640250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("world_tree_advanced", new BigDecimal("3280500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("world_tree_expert", new BigDecimal("6561000"), new BigDecimal("0.4")))
	);

	//////////////////////////////////////////
	//       Wood Transformation ones       //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> BARK = ASPECTS.register("bark",
			() -> new ElementSystemConverter(3d, WuxiaElements.WOOD.getId(), System.BODY)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "seed"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "moss")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bark_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bark_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bark_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("bark_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> BRANCHING = ASPECTS.register("branching",
			() -> new ElementSystemConverter(3d, WuxiaElements.WOOD.getId(), System.ESSENCE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "seed"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "moss")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("branching_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("branching_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("branching_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("branching_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> SWAYING = ASPECTS.register("swaying",
			() -> new ElementSystemConverter(3d, WuxiaElements.WOOD.getId(), System.DIVINE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "seed"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "moss")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("swaying_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("swaying_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("swaying_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("swaying_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	//////////////////////////////////////////
	//       Wooden Special ones            //
	//////////////////////////////////////////
	//TODO make this get stronger by adding a flower factor to it and convert this to special
	public static RegistryObject<TechniqueAspect> FLOWER = ASPECTS.register("flower",
			() -> new ElementalGenerator(1d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> LICHEN = ASPECTS.register("lichen",
			() -> new ElementToElementConverter(3d, 1.5d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> STEM = ASPECTS.register("stem",
			() -> new ElementToElementConverter(5d, 1.7d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> CHARCOAL = ASPECTS.register("charcoal",
			() -> new ElementToElementConverter(3d, 1.1d, WuxiaElements.WOOD.getId(), WuxiaElements.FIRE.getId())
					.setCanLearn(cultivation -> (cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "seed"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "moss")))
							&& (cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "cinder"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ember"))))
	);

	//////////////////////////////////////////
	//       Metal Transformation ones      //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> ORE = ASPECTS.register("ore",
			() -> new ElementalGenerator(1d, WuxiaElements.METAL.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("ore_basic", new BigDecimal("100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ore_intermediate", new BigDecimal("250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ore_advanced", new BigDecimal("500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ore_expert", new BigDecimal("1000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_nugget"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> METAL_NUGGET = ASPECTS.register("metal_nugget",
			() -> new ElementalGenerator(3d, WuxiaElements.METAL.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_nugget_basic", new BigDecimal("300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_nugget_intermediate", new BigDecimal("750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_nugget_advanced", new BigDecimal("1500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_nugget_expert", new BigDecimal("3000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_ingot"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> METAL_INGOT = ASPECTS.register("metal_ingot",
			() -> new ElementalGenerator(9d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ore")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_ingot_basic", new BigDecimal("900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_ingot_intermediate", new BigDecimal("2250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_ingot_advanced", new BigDecimal("4500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_ingot_expert", new BigDecimal("9000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_block"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> METAL_BLOCK = ASPECTS.register("metal_block",
			() -> new ElementalGenerator(27d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_nugget")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_block_basic", new BigDecimal("2700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_block_intermediate", new BigDecimal("6750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_block_advanced", new BigDecimal("13500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_block_expert", new BigDecimal("27000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "earthly_metal"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> EARTHLY_METAL = ASPECTS.register("earthly_metal",
			() -> new ElementalGenerator(81d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_ingot")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthly_metal_basic", new BigDecimal("8100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthly_metal_intermediate", new BigDecimal("20250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthly_metal_advanced", new BigDecimal("40500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthly_metal_expert", new BigDecimal("81000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heavenly_metal"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_METAL = ASPECTS.register("heavenly_metal",
			() -> new ElementalGenerator(243d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_block")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_metal_basic", new BigDecimal("24300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_metal_intermediate", new BigDecimal("60750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_metal_advanced", new BigDecimal("121500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_metal_expert", new BigDecimal("243000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "godly_metal"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> GODLY_METAL = ASPECTS.register("godly_metal",
			() -> new ElementalGenerator(729d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "earthly_metal")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("godly_metal_basic", new BigDecimal("72900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("godly_metal_intermediate", new BigDecimal("182250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("godly_metal_advanced", new BigDecimal("364500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("godly_metal_expert", new BigDecimal("729000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ancient_metal"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> ANCIENT_METAL = ASPECTS.register("ancient_metal",
			() -> new ElementalGenerator(2187d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heavenly_metal")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_metal_basic", new BigDecimal("218700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_metal_intermediate", new BigDecimal("564750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_metal_advanced", new BigDecimal("1093500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("ancient_metal_expert", new BigDecimal("2187000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "world_metal"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> WORLD_METAL = ASPECTS.register("world_metal",
			() -> new ElementalGenerator(6561d, WuxiaElements.METAL.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "godly_metal")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_basic", new BigDecimal("656100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_intermediate", new BigDecimal("1640250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_advanced", new BigDecimal("3280500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flame_emperor_expert", new BigDecimal("6561000"), new BigDecimal("0.4")))
	);

	//////////////////////////////////////////
	//       Metal Transformation ones      //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> METAL_SKIN = ASPECTS.register("metal_skin",
			() -> new ElementSystemConverter(3d, WuxiaElements.METAL.getId(), System.BODY)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ore"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_nugget")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_skin_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_skin_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_skin_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("metal_skin_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> SHARPNESS = ASPECTS.register("sharpness",
			() -> new ElementSystemConverter(3d, WuxiaElements.METAL.getId(), System.ESSENCE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ore"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_nugget")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sharpness_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sharpness_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sharpness_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sharpness_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> MAGNETIZATION = ASPECTS.register("magnetization",
			() -> new ElementSystemConverter(3d, WuxiaElements.METAL.getId(), System.DIVINE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "ore"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "metal_nugget")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magnetization_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magnetization_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magnetization_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("magnetization_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	//////////////////////////////////////////
	//       Lightning Generation ones      //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SPARK = ASPECTS.register("spark",
			() -> new ElementalGenerator(1d, WuxiaElements.LIGHTNING.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("spark_basic", new BigDecimal("100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("spark_intermediate", new BigDecimal("250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("spark_advanced", new BigDecimal("500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("spark_expert", new BigDecimal("1000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "circuit"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> CIRCUIT = ASPECTS.register("circuit",
			() -> new ElementalGenerator(3d, WuxiaElements.LIGHTNING.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("circuit_basic", new BigDecimal("300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("circuit_intermediate", new BigDecimal("750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("circuit_advanced", new BigDecimal("1500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("circuit_expert", new BigDecimal("3000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "thundering"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> THUNDERING = ASPECTS.register("thundering",
			() -> new ElementalGenerator(9d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "spark")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("thundering_basic", new BigDecimal("900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("thundering_intermediate", new BigDecimal("2250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("thundering_advanced", new BigDecimal("4500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("thundering_expert", new BigDecimal("9000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "earthen_thunder"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> EARTHEN_THUNDER = ASPECTS.register("earthen_thunder",
			() -> new ElementalGenerator(27d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "circuit")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthen_thunder_basic", new BigDecimal("2700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthen_thunder_intermediate", new BigDecimal("6750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthen_thunder_advanced", new BigDecimal("13500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("earthen_thunder_expert", new BigDecimal("27000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heavenly_lightning"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_LIGHTNING = ASPECTS.register("heavenly_lightning",
			() -> new ElementalGenerator(81d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "thundering")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_lightning_basic", new BigDecimal("8100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_lightning_intermediate", new BigDecimal("20250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_lightning_advanced", new BigDecimal("40500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("heavenly_lightning_expert", new BigDecimal("81000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "immortal_storm"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_STORM = ASPECTS.register("immortal_storm",
			() -> new ElementalGenerator(243d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "earthen_thunder")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_storm_basic", new BigDecimal("24300")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_storm_intermediate", new BigDecimal("60750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_storm_advanced", new BigDecimal("121500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("immortal_storm_expert", new BigDecimal("243000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "endless_lightning"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> ENDLESS_LIGHTNING = ASPECTS.register("endless_lightning",
			() -> new ElementalGenerator(729d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "heavenly_lightning")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("endless_lightning_basic", new BigDecimal("72900")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("endless_lightning_intermediate", new BigDecimal("182250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("endless_lightning_advanced", new BigDecimal("364500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("endless_lightning_expert", new BigDecimal("729000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "embodiment_of_lightning"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> EMBODIMENT_OF_LIGHTNING = ASPECTS.register("embodiment_of_lightning",
			() -> new ElementalGenerator(2187d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "immortal_storm")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("embodiment_of_lightning_basic", new BigDecimal("218700")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("embodiment_of_lightning_intermediate", new BigDecimal("564750"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("embodiment_of_lightning_advanced", new BigDecimal("1093500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("embodiment_of_lightning_expert", new BigDecimal("2187000"), new BigDecimal("0.4"),
							cultivation -> cultivation.getAspects().learnAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "god_of_lightning"), cultivation)))
	);

	public static RegistryObject<TechniqueAspect> GOD_OF_LIGHTNING = ASPECTS.register("god_of_lightning",
			() -> new ElementalGenerator(6561d, WuxiaElements.LIGHTNING.getId())
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "endless_lightning")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_lightning_basic", new BigDecimal("656100")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_lightning_intermediate", new BigDecimal("1640250"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_lightning_advanced", new BigDecimal("3280500"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("god_of_lightning_expert", new BigDecimal("6561000"), new BigDecimal("0.4")))
	);

	//////////////////////////////////////////
	//       Lightning Generation ones      //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> CONDUIT = ASPECTS.register("conduit",
			() -> new ElementSystemConverter(3d, WuxiaElements.LIGHTNING.getId(), System.BODY)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "spark"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "circuit")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("conduit_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("conduit_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("conduit_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("conduit_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> ARC = ASPECTS.register("arc",
			() -> new ElementSystemConverter(3d, WuxiaElements.LIGHTNING.getId(), System.ESSENCE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "spark"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "circuit")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("arc_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("arc_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("arc_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("arc_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	public static RegistryObject<TechniqueAspect> FLASH = ASPECTS.register("flash",
			() -> new ElementSystemConverter(3d, WuxiaElements.LIGHTNING.getId(), System.DIVINE)
					.setCanLearn(cultivation -> cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "spark"))
							|| cultivation.getAspects()
							.knowsAspect(new ResourceLocation(WuxiaCraft.MOD_ID, "circuit")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flash_basic", new BigDecimal("1000"), new BigDecimal("0.1")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flash_intermediate", new BigDecimal("5000"), new BigDecimal("0.2")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flash_advanced", new BigDecimal("15000"), new BigDecimal("0.4")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("flash_expert", new BigDecimal("35000"), new BigDecimal("0.6")))
	);

	//////////////////////////////////////////
	//       Earth Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> ROOT = ASPECTS.register("root",
			() -> new ElementToElementConverter(3d, 1.1d, WuxiaElements.FIRE.getId(), WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Space Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SPACE_TEARING = ASPECTS.register("space_tearing",
			() -> new ElementToSkillConsumer(WuxiaElements.SPACE.getId(), 10d, WuxiaSkillAspects.SPACE_TEAR.getId())
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
					event.setAmount(amount.multiply(modifier));
				}
			}
	);

	//////////////////////////////////////////
	//       Light Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> LEAF = ASPECTS.register("leaf",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.LIGHT.getId(), WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Neutral Generation ones        //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> QI_STRAND = ASPECTS.register("qi_strand",
			() -> new ElementalGenerator(0.7d, WuxiaElements.PHYSICAL.getId())
	);

	public static RegistryObject<TechniqueAspect> BLOOD_BURNING = ASPECTS.register("blood_burning",
			() -> new ConditionalElementalGenerator(2d, WuxiaElements.PHYSICAL.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					var cultivation = Cultivation.get(event.getPlayer());
					var health = cultivation.getStat(PlayerStat.HEALTH);
					health = health.subtract(event.getAmount().multiply(new BigDecimal("0.7")));
					event.setAmount(event.getAmount().multiply(new BigDecimal("1.4")));
					cultivation.setStat(PlayerStat.HEALTH, health);
				}
			}
	);

	public static RegistryObject<TechniqueAspect> DEVOURING = ASPECTS.register("devouring",
			() -> new ConditionalElementalGenerator(1d, WuxiaElements.PHYSICAL.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					var player = event.getPlayer();
					var cultivation = Cultivation.get(player);
					var systemData = cultivation.getSystemData(event.getSystem());
					var itemStack = player.getMainHandItem();
					var devourData = TechniqueUtil.getDevouringDataPerItem(itemStack.getItem());
					if (devourData.size() <= 0) return;
					var baseAmount = event.getAmount();
					for (var element : devourData.keySet()) {
						baseAmount = baseAmount.add(devourData.get(element));
						systemData.addStat(element, PlayerSystemElementalStat.FOUNDATION, baseAmount.multiply(new BigDecimal("0.1")));
						cultivation.addStat(element, PlayerElementalStat.COMPREHENSION, baseAmount.multiply(new BigDecimal("0.3")));
					}
					event.setAmount(baseAmount);
				}
			}
	);

	/*


metallic heart (metal) -> gather metal cultivation base when near metal blocks
  image = image.empty
Author = @[Dao of luck breeding] syn

beast comprehension -> tamed animals might give a raw soul cultivation base
  image = image.empty

Author = @[Dao of luck breeding] syn
Aires — 01/24/2022
enlightenment -> gets a small chance of getting enlightened that increases cult speed for a while
  image = image.empty
Aires — 01/26/2022
slaugther -> gets soul cultivation base from killing
  image = image.empty

Author = @Seteron
hungry for earth -> gets cultivation base by breaking blocks (chanced) and increases proficiency in breaking earth element blocks
  image = image.empty

Author = @[Ruler of Dragons] Wu Long
Aires — 01/28/2022
chronological return -> gets time cultivation base from continuously going back to the start of the session and repeatedly increasing the cultivation based gained from it. From outside (and to the player) you might just be cultivating at the same place. Cultivation base increases exponentially as long as you don't move and keeps cultivating
  image = image.empty

Author = @Everyone's Junior Sister
Aires — 02/27/2022
Aspect of Luck = Transforms raw cultivation base (the refined part) into luck.
  image = image.empty

Author = @[Dao of Forests] Mt Febian
Sound manipulation = Unlocks search abilities using sound
  image = image.empty

Author = @[Dao of Forests] Mt Febian
Heavenly Dao Bot
BOT
 — 02/28/2022
Rain Aspect
Converts a little bit of water cultivation base into water elemental pierce for attacks
Author
@[Dao of Forests] Mt Febian
Sigh Aspect
Uses body cultivation base to enhance sight
Author
@[Dao of Forests] Mt Febian
Poisoned Qi
Converts a little bit of cultivation base to make attacks poison on contact
Author
@[Dao of Forests] Mt Febian
Heavenly Dao Bot
BOT
 — 02/28/2022
Aspect of Stars
Uses stars energy to generate a little bit of raw body cultivation base, works better at night
Author
@Asura
Yin Yang Exchange
Generates raw cultivation base if there is a partner on the bed same bed while cultivating (Only works up to 1 partner)
Author
@[Dao of Retardism]Gavatron80
Yin Aspect Dual Cultivation
Yin counterpart of the Yin Yang Exchange, gives bonus to yin elements connected
Author
@Vermilion Bird
Yang Aspect Dual Cultivaiton
Yang counterpart of the Yin Yang Exchange, gives bonus to yang elements connected
Author
@Vermilion Bird
Aspect of chaos
Generate good amounts of cultivation base at the cost of foundation
Author
<@!338712089650790401>
Aspect of order
Generate good amounts of cultivation base if foundation is Strong
Author
<@!338712089650790401>
Primordial Chaos
Uses spatial energy from the outer chaos to generate cultivation base
Author
@Vermilion Bird
Demon Aspect
Uses raw cultivation base and turns it into demonic cultivation base
Author
@Vermilion Bird
Dragon Aspect
Uses body cultivation base and starts transforming body into dragon
Author
@Vermilion Bird
Phoenix Aspect
Uses body cultivation base and starts transforming body into Phoenix
Author
@Vermilion Bird
Tiger Aspect
Uses body cultivation base and starts transforming body into Tiger
Author
@Vermilion Bird
Tortoise Aspect
Uses body cultivation base and starts transforming body into Tortoise
Author
@Vermilion Bird
Qilin Aspect
Uses body cultivation base and starts transforming body into Qilin
Author
@Vermilion Bird
Fox Aspect
Uses body cultivation base and starts transforming body into Fox
Author
@Everyone's Junior Sister
Five elements conversion
Generates cultivation base from the most efficient way of the cycle of the five base elements
Author
<@!590116241319133204>
Alchemy Cultivation
Gain cultivation base from performing alchemy
Author
@Zigresho
Defiance
Gains demonic cultivation base from killing stronger foes
Author
@Zigresho
Constellation aspects
A plethora of aspects that can have a constellation linked that can even perhaps contain some law knowledge behind
Author
@Harvey
Enhancement
When paired with another generation aspect will enhance a that aspect
Author
@[Dao of Retardism]Gavatron80
Aspect of Steam
By using both fire and water cultivation base to practice in a form that they benefit each other in the form of steam
Author
<@!491663363676307466>
Aspect of Ice
By freezing water cultivation base, can be used to practice in a form that water will be frozen
Author
<@!491663363676307466>
Aspect of Tribulation
By comprehending tribulation energy, can use it to attack foes
Author
@[Dao of Retardism]Gavatron80
Vitality Aspect
Uses wooden cultivation base to add quick healing properties to the body, causing the a phenomenon that will seem like the practitioner has a great health
Author
@Asura
Healing
Uses wooden cultivation base to transform some of it into a powerful healing skill to aid allies
Author
@Asura
Vermilion Flames
By comprehending a super rare form of fire, the Vermilion Flames from the Vermilion Bird, allows you to generate that fire to cultivate with it
Author
@Vermilion Bird
Dryad Life Link
By comprehending a rare wooden law, you learn to link your life essence to a plant. As long as that plant exists, you'll exist, and the damage will be shared with that plant
Author
@Vermilion Bird
Blending Aspect
Allows to enhance stats gained by the elements in the body system by further blending with the elements in your body
Author
@[DSS Leader] MrEizy
Gravity Manipulation
By comprehending rare wave forms released by the earth element spatial form, the practitioner can alter the gravity in a certain region
Author
@Asura
Poising Coexistence
By absorbing poison, generates raw cultivation base
Author
@Ancient Devouring Beast
Existence Paradox Aspect
By comprehending a very rare part of some law, gives the practitioner the ability of temporarily fade into non existence
Author
<@!881552223971192844>
Life and Death Exchange
By comprehending a rare part of the wood law, allows to exchange the vitality of nearby living things to bring to life someone who almost died
Samsara Experience
By comprehending the reincarnation cycle of life and death, allows to pull someone from this cycle to continue living in this life
Author
@Vermilion Bird
Metallic Body Aspect
Consume random metals and increase body metallic nature
Author
@Zigresho
Crystallic Body Aspect
Consume random metals and increase body crystallic nature
Author
@Zigresho
Lightning Aspect
When struck by lightning gains a little of cultivation base
Author
@Zigresho
Lightning Circuit
By further understanding the lightning strikes, enables the practitioner to gain more cultivation base from Lightning Aspect (use both together)
Author
@Zigresho
Scholarly Aspect
Gains cultivation base from reading or writing books
Author
@Seteron
Bleeding edge
Enhance body cultivation base gain from being with low health
Author
@Seteron
Heavenly Dao Bot
BOT
 — 02/28/2022
Oneironautics
Gains cultivation base from cultivating inside of the dreams
Author
@Zigresho
Lay Down Roots
Passively lay down energy roots that allows for better energy recovery in the essence system, only works for essence system
Author
@Seteron
Boundless Sea
By using water comprehension on how water is in the nature, allows for the practitioner to use the same logic to store it's own energy and further increase max energy
Author
@Seteron
Heavenly Dao Bot
BOT
 — 02/28/2022
Aspect of training
A peculiar aspect that allows you to gain body cultivation base from physical activities the cultivator do
Author
@[Dao of Dimension] Blue Phoenix
Aspect of running
A peculiar aspect that allows the practitioner to have extra speed based on how much the practitioner have traversed on ground
Author
@[Dao of Dimension] Blue Phoenix
Undead Slayer
Gain stats for when nearby undead are detected and have increased cultivation base when undead detected undead are killed in the detection range
Author
@[Dao of Dimension] Blue Phoenix
Aspect of Clear Minded
Gains divine cultivation base from doing activities that make your mind clear in the mundane world, like fishing
Author
@Heavenly Fruit Deity IV
Heavenly Dao Bot
BOT
 — 02/28/2022
Aspect of Moss
Collects wooden energy from nature and transforms it into wooden cultivation base
Author
@[Dao of Forests] Mt Febian
Aspect of Lichen
When combined with other wood element generation aspect symbionts will further enhance the wooden energy, enhancing that aspect generated resources
Author
@[Dao of Forests] Mt Febian
Aspect of Stem
When combined with other wood element generation aspect will make the wood energy grow into stems, enhancing that aspect generated resources
Author
@[Dao of Forests] Mt Febian
Herbalist
Growing herbs will generate wooden cultivation base
Author
@papryk
Concept of Buddhism
Grants an increased soul cultivation base, but in return hurting living things will cause a loss of cultivation base and foundation
Author
@Seteron
Heavenly Dao Bot
BOT
 — 02/28/2022
Sword Affinity
Grants cultivation base from the comprehension of the sword
Author
@Zigresho
Heavenly Dao Bot
BOT
 — 03/02/2022
Cinder
Tier 1 Fire generation aspect = Gets fire energy from the environment and with it's cinders, transform that into cultivaiton base
Author
@Sapphire Dragon Emperor
Blaze
Tier 3 Fire generation aspect = Gets fire energy from the environment and make them blaze to transform that into cultivaiton base
Author
@Sapphire Dragon Emperor
	 */

}
