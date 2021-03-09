package wuxiacraft;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.command.CommandSource;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wuxiacraft.capabilities.CultivationFactory;
import wuxiacraft.capabilities.CultivationStorage;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.MeditateScreen;
import wuxiacraft.client.handler.ClientSideHandler;
import wuxiacraft.client.handler.InputHandler;
import wuxiacraft.client.handler.RenderHudHandler;
import wuxiacraft.command.CultivationCommand;
import wuxiacraft.command.SkillCommand;
import wuxiacraft.container.IntrospectionContainer;
import wuxiacraft.container.MeditationContainer;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.init.WuxiaElements;
import wuxiacraft.init.WuxiaItems;
import wuxiacraft.init.WuxiaTechniques;
import wuxiacraft.network.WuxiaPacketHandler;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("wuxiacraft")
@SuppressWarnings("unused")
public class WuxiaCraft {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "wuxiacraft";

	public WuxiaCraft() {
		// Register the setup method for mod loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for mod loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for mod loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for mod loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		WuxiaItems.register();
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some pre init code
		LOGGER.info("Wuxiacraft init");
		CultivationLevel.initializeLevels();
		WuxiaElements.initalize();

		LOGGER.info("Register messages");
		WuxiaPacketHandler.registerMessages();

		LOGGER.info("Register capabilities");
		CapabilityManager.INSTANCE.register(ICultivation.class, new CultivationStorage(), new CultivationFactory());
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
		MinecraftForge.EVENT_BUS.register(new RenderHudHandler());
		MinecraftForge.EVENT_BUS.register(new ClientSideHandler());
		MinecraftForge.EVENT_BUS.register(new InputHandler());
		InputHandler.registerKeyBindings();

		ScreenManager.registerFactory(MeditationContainer.registryType, MeditateScreen::new);
		ScreenManager.registerFactory(IntrospectionContainer.registryType, IntrospectionScreen::new);
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		//InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		// LOGGER.info("Got IMC {}", event.getIMCStream().map(m->m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		//LOGGER.info("HELLO from server starting");
	}

	@SubscribeEvent
	public void onRegisterCommandEvent(RegisterCommandsEvent event) {
		LOGGER.info("HELLO commands registering");
		CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
		CultivationCommand.register(commandDispatcher);
		SkillCommand.register(commandDispatcher);
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
			// register a new block here
			LOGGER.info("HELLO from Register Block");
		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
			MeditationContainer.registryType = IForgeContainerType.create(MeditationContainer::createContainer);
			MeditationContainer.registryType.setRegistryName("meditation_container");
			IntrospectionContainer.registryType = IForgeContainerType.create(IntrospectionContainer::create);
			IntrospectionContainer.registryType.setRegistryName("introspection_container");
			event.getRegistry().register(MeditationContainer.registryType);
			event.getRegistry().register(IntrospectionContainer.registryType);
		}

		@SubscribeEvent
		public static void onRegisterModel(ModelRegistryEvent event) {
			ModelLoader.addSpecialModel(new ResourceLocation(WuxiaCraft.MOD_ID, "item/scroll"));
		}

		@SubscribeEvent
		public static void onBakeModels(ModelBakeEvent event) {
			IBakedModel model = event.getModelRegistry().get(new ResourceLocation(WuxiaCraft.MOD_ID, "item/scroll"));
			for (Technique tech : WuxiaTechniques.TECHNIQUES) {
				event.getModelRegistry().put(new ModelResourceLocation(new ResourceLocation(WuxiaCraft.MOD_ID, tech.getName() + "_item"), "inventory"), model);
			}
		}
	}
}
