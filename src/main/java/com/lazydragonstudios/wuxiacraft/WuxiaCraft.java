package com.lazydragonstudios.wuxiacraft;

import com.lazydragonstudios.wuxiacraft.command.*;
import com.lazydragonstudios.wuxiacraft.crafting.RuneMakingRecipe;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.init.*;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import com.lazydragonstudios.wuxiacraft.util.TechniqueUtil;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.EnumArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lazydragonstudios.wuxiacraft.capabilities.CapabilityAttachingHandler;
import com.lazydragonstudios.wuxiacraft.capabilities.CapabilityRegistryHandler;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("wuxiacraft")
public class WuxiaCraft {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	//the mod id for reference in resource locations
	public static final String MOD_ID = "wuxiacraft";

	public WuxiaCraft() {
		// Register the setup method for modloading
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);
		// Register the enqueueIMC method for modloading
		modEventBus.addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		modEventBus.addListener(this::processIMC);

		modEventBus.register(CapabilityRegistryHandler.class);

		MinecraftForge.EVENT_BUS.register(CapabilityAttachingHandler.class);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		WuxiaElements.ELEMENTS.register(modEventBus);
		WuxiaBlocks.BLOCKS.register(modEventBus);
		WuxiaItems.ITEMS.register(modEventBus);
		WuxiaMenuTypes.MENU_TYPES.register(modEventBus);
		WuxiaBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		WuxiaMobEffects.EFFECTS.register(modEventBus);
		WuxiaPoiTypes.POI_TYPES.register(modEventBus);
		WuxiaProfessions.PROFESSIONS.register(modEventBus);
		WuxiaParticleTypes.PARTICLE_TYPES.register(modEventBus);
		WuxiaRecipeTypes.RECIPE_TYPE_SERIALIZERS.register(modEventBus);
		WuxiaRealms.REALM_REGISTER.register(modEventBus);
		WuxiaRealms.STAGE_REGISTER.register(modEventBus);
		WuxiaEntities.ENTITY_TYPE_REGISTER.register(modEventBus);
		WuxiaTechniqueAspects.ASPECTS.register(modEventBus);
		WuxiaSkillAspects.ASPECTS.register(modEventBus);

		WuxiaConfigs.registerConfigs();
	}


	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("Registering messages. Check your transmission talisman!");
		WuxiaPacketHandler.registerMessages();

		WuxiaDefaultTechniqueManuals.init();
		TechniqueUtil.initDevouringData();
		TechniqueUtil.initChancedAspectsBlocks();
		registerArgumentTypes();
		RuneMakingRecipe.recipeType = RecipeType.register("runemaking");
		TestRun.testStuff();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
//		InterModComms.sendTo("ftbquests", "init", () -> {
//			LOGGER.info("Initializing the things from WuxiaCraft in FTBQuests");
//			FTBQuestsIntegration.init();
//			return "Hello world";
//		});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m -> m.messageSupplier().get()).
				collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	public void registerArgumentTypes() {
		ArgumentTypes.register("wuxiacraft:aspect_type", AspectArgument.class, new EmptyArgumentSerializer<>(AspectArgument::id));
		ArgumentTypes.register("wuxiacraft:stage_type", StageArgument.class, new EmptyArgumentSerializer<>(StageArgument::id));
		ArgumentTypes.register("wuxiacraft:element_type", ElementArgument.class, new EmptyArgumentSerializer<>(ElementArgument::id));
	}

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		var dispatcher = event.getDispatcher();
		CultivationCommand.register(dispatcher);
		StatCommand.register(dispatcher);
		CreateDefaultManualCommand.register(dispatcher);
	}
}
