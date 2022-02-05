package com.lazydragonstudios.wuxiacraft;

import com.lazydragonstudios.wuxiacraft.client.RenderHUDEventHandler;
import com.lazydragonstudios.wuxiacraft.client.overlays.DebugOverlay;
import com.lazydragonstudios.wuxiacraft.client.overlays.HealthOverlay;
import com.lazydragonstudios.wuxiacraft.command.CultivationCommand;
import com.lazydragonstudios.wuxiacraft.container.IntrospectionMenu;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaTechniqueAspects;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lazydragonstudios.wuxiacraft.capabilities.CapabilityAttachingHandler;
import com.lazydragonstudios.wuxiacraft.capabilities.CapabilityRegistryHandler;
import com.lazydragonstudios.wuxiacraft.client.InputHandler;
import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import com.lazydragonstudios.wuxiacraft.client.overlays.EnergiesOverlay;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRealms;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("wuxiacraft")
public class WuxiaCraft {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	//the mod id for reference in resource locations
	public static final String MOD_ID = "wuxiacraft";

	public WuxiaCraft() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the clientSetup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

		FMLJavaModLoadingContext.get().getModEventBus().register(CapabilityRegistryHandler.class);

		MinecraftForge.EVENT_BUS.register(CapabilityAttachingHandler.class);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		WuxiaRealms.REALM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		WuxiaRealms.STAGE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		WuxiaElements.ELEMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
		WuxiaTechniqueAspects.ASPECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		InputHandler.registerKeyBindings();

		MinecraftForge.EVENT_BUS.register(RenderHUDEventHandler.class);
		MinecraftForge.EVENT_BUS.register(InputHandler.class);

		OverlayRegistry.registerOverlayTop("wuxiacraft_debug", new DebugOverlay());
		OverlayRegistry.registerOverlayAbove(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, "wuxiacraft_health_bar", new HealthOverlay());
		OverlayRegistry.registerOverlayBelow(ForgeIngameGui.CHAT_PANEL_ELEMENT, "wuxiacraft_energies", new EnergiesOverlay());

		MenuScreens.register(IntrospectionMenu.registryType, IntrospectionScreen::new);

	}


	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("Registering messages. Check your transmission talisman!");
		WuxiaPacketHandler.registerMessages();

		TestRun.testStuff();

	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		//InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		//LOGGER.info("Got IMC {}", event.getIMCStream().
		//       map(m->m.messageSupplier().get()).
		//      collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		var dispatcher = event.getDispatcher();
		CultivationCommand.register(dispatcher);
	}
}
