package wuxiacraft;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wuxiacraft.capabilities.CapabilityAttachingHandler;
import wuxiacraft.capabilities.CapabilityRegistryHandler;
import wuxiacraft.capabilities.cultivation.CultivationProvider;
import wuxiacraft.init.WuxiaRealms;

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
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

		FMLJavaModLoadingContext.get().getModEventBus().register(CapabilityRegistryHandler.class);

		MinecraftForge.EVENT_BUS.register(CapabilityAttachingHandler.class);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		WuxiaRealms.REALM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
		WuxiaRealms.STAGE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}


	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("HELLO FROM PREINIT");
		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
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
}
