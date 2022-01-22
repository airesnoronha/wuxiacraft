package wuxiacraft.init;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryBuilder;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.CultivationRealm;
import wuxiacraft.cultivation.CultivationStage;
import wuxiacraft.cultivation.Element;
import wuxiacraft.cultivation.Technique;
import wuxiacraft.cultivation.technique.TechniqueAspect;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
		// register a new block here
		//WuxiaCraft.LOGGER.info("HELLO from Register Block");
	}

	@SubscribeEvent
	public static void onCreateNewRegistries(final RegistryEvent.NewRegistry event) {
		WuxiaCraft.LOGGER.info("Creating registries");

		WuxiaCraft.LOGGER.debug("Creating the registry for the Cultivation Realms");
		WuxiaRegistries.CULTIVATION_REALMS = new RegistryBuilder<CultivationRealm>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_realms"))
				.setType(CultivationRealm.class)
				.create();

		WuxiaCraft.LOGGER.debug("Creating the registry for the Cultivation Stages");
		WuxiaRegistries.CULTIVATION_STAGES = new RegistryBuilder<CultivationStage>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_stages"))
				.setType(CultivationStage.class)
				.create();

		WuxiaCraft.LOGGER.debug("Creating the registry for the Techniques");
		WuxiaRegistries.TECHNIQUES = new RegistryBuilder<Technique>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "techniques"))
				.setType(Technique.class)
				.create();

		WuxiaRegistries.ELEMENTS = new RegistryBuilder<Element>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "elements"))
				.setType(Element.class)
				.create();

		WuxiaRegistries.TECHNIQUE_ASPECT = new RegistryBuilder<TechniqueAspect>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "technique_aspects"))
				.setType(TechniqueAspect.class)
				.create();
	}
}