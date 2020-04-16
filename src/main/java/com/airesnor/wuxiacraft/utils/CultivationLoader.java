package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CultivationLoader {

	public static void loadLevelsFromConfig() {
		File levelsFile = new File(Loader.instance().getConfigDir(), "wuxiacraft/cultivationLevels.json");
		List<CultivationLevel> loadedLevels = new ArrayList<>();
		Gson gson = new Gson();
		try {
			Reader reader = new FileReader(levelsFile);
			CultivationLevelFileFormat read = gson.fromJson(reader, CultivationLevelFileFormat.class);
			for (CultivationLevelFileFormat.CultivationLevelFormat level : read.levels) {
				loadedLevels.add(new CultivationLevel(level.levelName,
						level.nextLevelName,
						level.subLevels,
						level.baseProgress,
						(float) level.baseSpeed,
						(float) level.baseStrength,
						level.energyAsFood,
						level.needNoFood,
						level.canFly,
						level.freeFlight,
						level.teleportation));
			}
		} catch (FileNotFoundException e) {
			WuxiaCraft.logger.error("Couldn't find the cultivation levels file. creating a new one.");
			loadedLevels = CultivationLevel.DEFAULTS;
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
					levelToWrite.subLevels = level.subLevels;
					levelToWrite.baseProgress = level.baseProgress;
					levelToWrite.baseSpeed = level.baseSpeedModifier;
					levelToWrite.baseStrength = level.baseStrengthModifier;
					levelToWrite.energyAsFood = level.energyAsFood;
					levelToWrite.needNoFood = level.needNoFood;
					levelToWrite.canFly = level.canFly;
					levelToWrite.freeFlight = level.freeFlight;
					levelToWrite.teleportation = level.teleportation;
					toWrite.levels.add(levelToWrite);
				}
				gson = new GsonBuilder().setPrettyPrinting().create();
				String output = gson.toJson(toWrite);
				writer.write(output);
				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			for (CultivationLevel loadedLevel : loadedLevels) {
				CultivationLevel.REGISTERED_LEVELS.put(loadedLevel.levelName, loadedLevel);
			}
			CultivationLevel.BASE_LEVEL = loadedLevels.get(0); // gets the first registered level, i hope
		}
	}

	public static class CultivationLevelFileFormat {
		public static class CultivationLevelFormat {
			public String levelName;
			public String nextLevelName;
			public int subLevels;
			public double baseProgress;
			public double baseStrength;
			public double baseSpeed;
			public boolean energyAsFood;
			public boolean needNoFood;
			public boolean canFly;
			public boolean freeFlight;
			public boolean teleportation;
		}

		public List<CultivationLevelFormat> levels;
	}

}
