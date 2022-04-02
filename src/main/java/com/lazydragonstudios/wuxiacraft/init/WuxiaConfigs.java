package com.lazydragonstudios.wuxiacraft.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class WuxiaConfigs {

    //Server Config Values

    //Common Config Values
    public static ForgeConfigSpec.LongValue INITIAL_LIVES;
    public static ForgeConfigSpec.LongValue MAX_LIVES;
    public static ForgeConfigSpec.DoubleValue CULTIVATION_SPEED_MULTIPLIER;
    public static ForgeConfigSpec.IntValue SEMI_DEAD_TIMER;

    //Client Config Values


    public static void registerConfigs() {
        registerServerConfig();
        registerCommonConfig();
        registerClientConfig();
    }

    private static void registerServerConfig() {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    private static void registerCommonConfig() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Common Config for WuxiaCraft").push("cultivation");

        INITIAL_LIVES = COMMON_BUILDER
                .comment("How many lives do players initially have.")
                .defineInRange("initialLives", 3, 1, Long.MAX_VALUE);
        MAX_LIVES = COMMON_BUILDER
                .comment("The max lives a player will manage to obtain or have.")
                .defineInRange("maxLives", 3, 1, Long.MAX_VALUE);
        CULTIVATION_SPEED_MULTIPLIER = COMMON_BUILDER
                .comment("The multiplier for the cultivation speed.")
                .defineInRange("cultivationSpeedMultiplier", 1.0d, 0, Double.MAX_VALUE);
        SEMI_DEAD_TIMER = COMMON_BUILDER
                .comment("The time in seconds the player will stay in the semi dead state upon death.")
                .defineInRange("semiDeadTimer", 300, 0, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

    private static void registerClientConfig() {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }
}
