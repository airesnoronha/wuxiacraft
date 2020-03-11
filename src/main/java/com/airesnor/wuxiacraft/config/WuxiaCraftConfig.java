package com.airesnor.wuxiacraft.config;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.SpeedHandicapMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WuxiaCraftConfig {

	public static Configuration config = null;

	public static final String CATEGORY_GAMEPLAY = "gameplay";

	public static int speedHandicap;

	public static boolean disableStepAssist;

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

		Property propHandicap = config.get(CATEGORY_GAMEPLAY, "speed_handicap", 100);
		propHandicap.setLanguageKey("gui.config.gameplay.speed_handicap.name");
		propHandicap.setComment("The relative top speed, after all, we're only humans. 0% = vanilla move speed - 100% = maximum available");
		propHandicap.setMaxValue(100);
		propHandicap.setMinValue(0);

		Property propStepAssist = config.get(CATEGORY_GAMEPLAY, "step_assist", true);
		propStepAssist.setLanguageKey("gui.config.gameplay.step_assist.name");
		propStepAssist.setComment("If you want to enable step assist gained from cultivation levels");
		propStepAssist.setDefaultValue(true);

		List<String> propOrder = new ArrayList<>();
		propOrder.add(propHandicap.getName());
		propOrder.add(propStepAssist.getName());
		config.setCategoryPropertyOrder(CATEGORY_GAMEPLAY, propOrder);

		if (readFieldsFromConfig) {
			speedHandicap = propHandicap.getInt();
			disableStepAssist = propStepAssist.getBoolean();
		}

		propHandicap.set(speedHandicap);
		propStepAssist.set(disableStepAssist);

		if (config.hasChanged())
			config.save();
	}

	public static class ConfigEventHandler {
		@SubscribeEvent
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WuxiaCraft.MODID)) {
				syncFromGui();
				WuxiaCraft.logger.info("fired event changed");
				NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(speedHandicap));
			}
		}
	}
}
