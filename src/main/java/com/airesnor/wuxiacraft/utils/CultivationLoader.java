package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CultivationLoader {

	public static void loadLevelsFromConfig() {
		File levelsFile = new File(Loader.instance().getConfigDir(), "wuxiacraft/cultivationLevels.json");
		List<CultivationLevel> loadedLevels = new ArrayList<>(CultivationLevel.DEFAULTS);
		Gson gson = new Gson();
		try {
			Reader reader = new FileReader(levelsFile);
			CultivationLevelFileFormat read = gson.fromJson(reader, CultivationLevelFileFormat.class);
			loadedLevels.clear();
			for (CultivationLevelFileFormat.CultivationLevelFormat level : read.levels) {
				loadedLevels.add(new CultivationLevel(level.levelName,
						level.nextLevelName,
						level.displayName,
						level.subLevels,
						level.maxFoundationStat,
						level.baseProgress,
						(float) level.baseSpeed,
						(float) level.baseStrength,
						level.energyAsFood,
						level.needNoFood,
						level.canFly,
						level.freeFlight,
						level.teleportation,
						level.callsTribulation,
						level.tribulationEachSubLevel));
			}
		} catch (FileNotFoundException e) {
			WuxiaCraft.logger.error("Couldn't find the cultivation levels file. creating a new one.");
		} finally {
			try {
				if (levelsFile.getParentFile() != null) {
					if (levelsFile.getParentFile().mkdirs()) {
						WuxiaCraft.logger.info("Creating even the folder, BTW");
					}
				}
				if (!levelsFile.exists()) {
					if (levelsFile.createNewFile()) {
						WuxiaCraft.logger.info("Creating the file");
					}
				}
				FileWriter writer = new FileWriter(levelsFile);
				CultivationLevelFileFormat toWrite = new CultivationLevelFileFormat();
				toWrite.levels = new ArrayList<>();
				for (CultivationLevel level : loadedLevels) {
					CultivationLevelFileFormat.CultivationLevelFormat levelToWrite = new CultivationLevelFileFormat.CultivationLevelFormat();
					levelToWrite.levelName = level.levelName;
					levelToWrite.nextLevelName = level.nextLevelName;
					levelToWrite.displayName = level.displayName;
					levelToWrite.subLevels = level.subLevels;
					levelToWrite.maxFoundationStat = level.foundationMaxStat;
					levelToWrite.baseProgress = level.baseProgress;
					levelToWrite.baseSpeed = level.baseSpeedModifier;
					levelToWrite.baseStrength = level.baseStrengthModifier;
					levelToWrite.energyAsFood = level.energyAsFood;
					levelToWrite.needNoFood = level.needNoFood;
					levelToWrite.canFly = level.canFly;
					levelToWrite.freeFlight = level.freeFlight;
					levelToWrite.teleportation = level.teleportation;
					levelToWrite.callsTribulation = level.callsTribulation;
					levelToWrite.tribulationEachSubLevel = level.tribulationEachSubLevel;
					toWrite.levels.add(levelToWrite);
				}
				gson = new GsonBuilder().setPrettyPrinting().create();
				String output = gson.toJson(toWrite);
				writer.write(output);
				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			for (CultivationLevel loadedLevel : loadedLevels) {
				CultivationLevel.REGISTERED_LEVELS.put(loadedLevel.levelName, loadedLevel);
			}
			CultivationLevel.REGISTERED_BASE_LEVEL = loadedLevels.get(0); // gets the first registered level, i hope
			CultivationLevel.LOADED_LEVELS.clear();
			CultivationLevel.LOADED_LEVELS.putAll(CultivationLevel.REGISTERED_LEVELS);
			CultivationLevel.BASE_LEVEL = CultivationLevel.REGISTERED_BASE_LEVEL;
		}
	}

	public static class CultivationLevelFileFormat {
		public static class CultivationLevelFormat {
			public String levelName = "base_level";
			public String nextLevelName = "base_level";
			public String displayName= "Base Level";
			public int subLevels = 5;
			public long maxFoundationStat = 10;
			public double baseProgress = 1000;
			public double baseStrength = 1;
			public double baseSpeed = 1;
			public boolean energyAsFood = false;
			public boolean needNoFood = false;
			public boolean canFly = false;
			public boolean freeFlight = false;
			public boolean teleportation = false;
			public boolean callsTribulation = true;
			public boolean tribulationEachSubLevel = true;
		}

		public List<CultivationLevelFormat> levels;
	}

}
