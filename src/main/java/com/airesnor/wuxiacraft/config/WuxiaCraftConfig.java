package com.airesnor.wuxiacraft.config;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SpeedHandicapMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WuxiaCraftConfig {

	public static Configuration config = null;

	public static final String CATEGORY_GAMEPLAY = "gameplay";
	public static int speedHandicap;
	public static boolean disableStepAssist;
	public static float maxSpeed;
	public static float blockBreakLimit;
	public static float jumpLimit;
	public static float stepAssistLimit;

	public static final String CATEGORY_MISCELLANEOUS = "miscellaneous";
	public static boolean EXTREME_QI_BIOME_SPAWN;

	public static final String CATEGORY_DIMENSION = "dimensions";
	public static int DIMENSION_MINING;
	public static int DIMENSION_FIRE;
	public static int DIMENSION_EARTH;
	public static int DIMENSION_METAL;
	public static int DIMENSION_WATER;
	public static int DIMENSION_WOOD;

	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "WuxiaCraft.cfg");
		config = new Configuration(configFile);
		syncFromFiles();
	}

	public static Configuration getConfig() {
		return config;
	}

	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	public static void syncFromFiles() {
		syncConfig(true, true);

	}

	public static void syncFromGui() {
		syncConfig(false, true);
	}

	public static void syncFromFields() {
		syncConfig(false, false);
	}

	private static void syncConfig(boolean loadFromConfigFile, boolean readFieldsFromConfig) {
		if (loadFromConfigFile)
			config.load();

		config.setCategoryComment(CATEGORY_GAMEPLAY, "Set Gameplay aspects");
		config.setCategoryComment(CATEGORY_DIMENSION, "Set Dimension IDs");
		config.setCategoryComment(CATEGORY_MISCELLANEOUS, "Set Miscellaneous Items");

		//Gameplay
		Property propHandicap = config.get(CATEGORY_GAMEPLAY, "speed_handicap", 100);
		propHandicap.setLanguageKey("gui.config.gameplay.speed_handicap.name");
		propHandicap.setComment("The relative top speed, after all, we're only humans. 0% = vanilla move speed - 100% = maximum available");
		propHandicap.setMaxValue(100);
		propHandicap.setMinValue(0);

		Property propMaxSpeed = config.get(CATEGORY_GAMEPLAY, "max_speed", 5.0f);
		propMaxSpeed.setLanguageKey("gui.config.gameplay.max_speed.name");
		propMaxSpeed.setComment("Max speed for playing, this will allow you to never go beyond this speed");
		propMaxSpeed.setDefaultValue(5.0f);

		Property propStepAssist = config.get(CATEGORY_GAMEPLAY, "step_assist", true);
		propStepAssist.setLanguageKey("gui.config.gameplay.step_assist.name");
		propStepAssist.setComment("If you want to enable step assist gained from cultivation levels");
		propStepAssist.setDefaultValue(true);

		Property propBreakSpeed = config.get(CATEGORY_GAMEPLAY, "haste_limit", 5f);
		propBreakSpeed.setLanguageKey("gui.config.gameplay.haste_limit.name");
		propBreakSpeed.setComment("Set a multiplier to base breaking speed that will be the it's limit gained from cultivation level");
		propBreakSpeed.setDefaultValue(5.0f);

		Property propJumpLimit = config.get(CATEGORY_GAMEPLAY, "jump_limit", 5f);
		propJumpLimit.setLanguageKey("gui.config.gameplay.jump_limit.name");
		propJumpLimit.setComment("Set a multiplier to base jump height that will be the it's limit gained from cultivation level");
		propJumpLimit.setDefaultValue(5.0f);

		Property propStepAssistLimit = config.get(CATEGORY_GAMEPLAY, "step_assist_limit", 3f);
		propJumpLimit.setLanguageKey("gui.config.gameplay.step_assist_limit.name");
		propJumpLimit.setComment("Set many blocks step assist permitted");
		propJumpLimit.setDefaultValue(3.0f);

		//Dimensions
		Property propDimensionMining = config.get(CATEGORY_DIMENSION, "dimension_mining", 200);
		propDimensionMining.setComment("Set the ID for the Mining Dimension");
		propDimensionMining.setDefaultValue(200);

		Property propDimensionFire = config.get(CATEGORY_DIMENSION, "dimension_fire", 202);
		propDimensionFire.setComment("Set the ID for the Fire Dimension");
		propDimensionFire.setDefaultValue(202);

		Property propDimensionEarth = config.get(CATEGORY_DIMENSION, "dimension_earth", 203);
		propDimensionEarth.setComment("Set the ID for the Earth Dimension");
		propDimensionEarth.setDefaultValue(203);

		Property propDimensionMetal = config.get(CATEGORY_DIMENSION, "dimension_metal", 204);
		propDimensionMetal.setComment("Set the ID for the Metal Dimension");
		propDimensionMetal.setDefaultValue(204);

		Property propDimensionWater = config.get(CATEGORY_DIMENSION, "dimension_water", 205);
		propDimensionWater.setComment("Set the ID for the Water Dimension");
		propDimensionWater.setDefaultValue(205);

		Property propDimensionWood = config.get(CATEGORY_DIMENSION, "dimension_wood", 206);
		propDimensionWood.setComment("Set the ID for the Wood Dimension");
		propDimensionWood.setDefaultValue(206);

		//Miscellaneous
		Property propExtremeQiBiomeSpawn = config.get(CATEGORY_MISCELLANEOUS, "extreme_qi_biome_spawn", true);
		propExtremeQiBiomeSpawn.setComment("Set whether the Extreme Qi biome will spawn in overworld");
		propExtremeQiBiomeSpawn.set(true);

		List<String> propOrder = new ArrayList<>();
		propOrder.add(propHandicap.getName());
		propOrder.add(propMaxSpeed.getName());
		propOrder.add(propStepAssist.getName());
		propOrder.add(propBreakSpeed.getName());
		propOrder.add(propJumpLimit.getName());
		propOrder.add(propStepAssistLimit.getName());
		config.setCategoryPropertyOrder(CATEGORY_GAMEPLAY, propOrder);

		propOrder.add(propDimensionMining.getName());
		propOrder.add(propDimensionFire.getName());
		propOrder.add(propDimensionEarth.getName());
		propOrder.add(propDimensionMetal.getName());
		propOrder.add(propDimensionWater.getName());
		propOrder.add(propDimensionWood.getName());
		config.setCategoryPropertyOrder(CATEGORY_DIMENSION, propOrder);

		propOrder.add(propExtremeQiBiomeSpawn.getName());
		config.setCategoryPropertyOrder(CATEGORY_MISCELLANEOUS, propOrder);

		if (readFieldsFromConfig) {
			speedHandicap = propHandicap.getInt();
			maxSpeed = (float) propMaxSpeed.getDouble();
			disableStepAssist = propStepAssist.getBoolean();
			blockBreakLimit = (float) propBreakSpeed.getDouble();
			jumpLimit = (float) propJumpLimit.getDouble();
			stepAssistLimit = (float) propStepAssistLimit.getDouble();
			DIMENSION_MINING = propDimensionMining.getInt();
			DIMENSION_FIRE = propDimensionFire.getInt();
			DIMENSION_EARTH = propDimensionEarth.getInt();
			DIMENSION_METAL = propDimensionMetal.getInt();
			DIMENSION_WATER = propDimensionWater.getInt();
			DIMENSION_WOOD = propDimensionWood.getInt();
			EXTREME_QI_BIOME_SPAWN = propExtremeQiBiomeSpawn.getBoolean();
		}

		propHandicap.set(speedHandicap);
		propMaxSpeed.set(maxSpeed);
		propStepAssist.set(disableStepAssist);
		propBreakSpeed.set(blockBreakLimit);
		propJumpLimit.set(jumpLimit);
		propStepAssistLimit.set(stepAssistLimit);
		propDimensionMining.set(DIMENSION_MINING);
		propDimensionFire.set(DIMENSION_FIRE);
		propDimensionEarth.set(DIMENSION_EARTH);
		propDimensionMetal.set(DIMENSION_METAL);
		propDimensionWater.set(DIMENSION_WATER);
		propDimensionWood.set(DIMENSION_WOOD);
		propExtremeQiBiomeSpawn.set(EXTREME_QI_BIOME_SPAWN);

		if (config.hasChanged())
			config.save();
	}

	public static class ConfigEventHandler {
		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WuxiaCraft.MOD_ID)) {
				syncFromGui();
				WuxiaCraft.logger.info("Sending a config update to server");
				syncCultivationFromConfigToClient();
				NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(speedHandicap, maxSpeed, blockBreakLimit, jumpLimit, stepAssistLimit, Minecraft.getMinecraft().player.getUniqueID()));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void syncCultivationFromConfigToClient() {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
			cultivation.setSpeedHandicap(WuxiaCraftConfig.speedHandicap);
			cultivation.setMaxSpeed(WuxiaCraftConfig.maxSpeed);
			cultivation.setHasteLimit(WuxiaCraftConfig.blockBreakLimit);
			cultivation.setJumpLimit(WuxiaCraftConfig.jumpLimit);
			cultivation.setStepAssistLimit(WuxiaCraftConfig.stepAssistLimit);
		});
	}
}
