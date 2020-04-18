package com.airesnor.wuxiacraft.config;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
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

		List<String> propOrder = new ArrayList<>();
		propOrder.add(propHandicap.getName());
		propOrder.add(propMaxSpeed.getName());
		propOrder.add(propStepAssist.getName());
		propOrder.add(propBreakSpeed.getName());
		propOrder.add(propJumpLimit.getName());
		config.setCategoryPropertyOrder(CATEGORY_GAMEPLAY, propOrder);

		if (readFieldsFromConfig) {
			speedHandicap = propHandicap.getInt();
			maxSpeed = (float) propMaxSpeed.getDouble();
			disableStepAssist = propStepAssist.getBoolean();
			blockBreakLimit = (float) propBreakSpeed.getDouble();
			jumpLimit = (float) propJumpLimit.getDouble();
		}

		propHandicap.set(speedHandicap);
		propMaxSpeed.set(maxSpeed);
		propStepAssist.set(disableStepAssist);
		propBreakSpeed.set(blockBreakLimit);
		propJumpLimit.set(jumpLimit);

		if (config.hasChanged())
			config.save();
	}

	public static class ConfigEventHandler {
		@SubscribeEvent
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WuxiaCraft.MOD_ID)) {
				syncFromGui();
				WuxiaCraft.logger.info("Sending a config update to server");
				syncCultivationFromConfigToClient();
				NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(speedHandicap, maxSpeed, blockBreakLimit, jumpLimit, Minecraft.getMinecraft().player.getName()));
			}
		}
	}

	public static void syncCultivationFromConfigToClient() {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
				cultivation.setSpeedHandicap(WuxiaCraftConfig.speedHandicap);
				cultivation.setMaxSpeed(WuxiaCraftConfig.maxSpeed);
				cultivation.setHasteLimit(WuxiaCraftConfig.blockBreakLimit);
				cultivation.setJumpLimit(WuxiaCraftConfig.jumpLimit);
			}
		});
	}
}
