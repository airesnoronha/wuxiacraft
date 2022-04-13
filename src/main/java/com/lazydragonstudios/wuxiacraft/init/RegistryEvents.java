package com.lazydragonstudios.wuxiacraft.init;


import com.lazydragonstudios.wuxiacraft.container.InscriberMenu;
import com.lazydragonstudios.wuxiacraft.container.IntrospectionMenu;
import com.lazydragonstudios.wuxiacraft.container.RunemakingMenu;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationRealm;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationStage;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.Element;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;

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
	public static void onCreateNewRegistries(final NewRegistryEvent event) {
		WuxiaCraft.LOGGER.info("Creating registries");

		WuxiaCraft.LOGGER.debug("Creating the registry for the Cultivation Realms");
		WuxiaRegistries.CULTIVATION_REALMS = event.create(new RegistryBuilder<CultivationRealm>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_realms"))
				.setType(CultivationRealm.class));

		WuxiaCraft.LOGGER.debug("Creating the registry for the Cultivation Stages");
		WuxiaRegistries.CULTIVATION_STAGES = event.create(new RegistryBuilder<CultivationStage>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "cultivation_stages"))
				.setType(CultivationStage.class));

		WuxiaCraft.LOGGER.debug("Creating the registry for the Elements");
		WuxiaRegistries.ELEMENTS = event.create(new RegistryBuilder<Element>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "elements"))
				.setType(Element.class));

		WuxiaCraft.LOGGER.debug("Creating the registry for the Technique Aspects");
		WuxiaRegistries.TECHNIQUE_ASPECT = event.create(new RegistryBuilder<TechniqueAspect>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "technique_aspects"))
				.setType(TechniqueAspect.class));

		WuxiaCraft.LOGGER.debug("Creating the registry for the Skills Aspects");
		WuxiaRegistries.SKILL_ASPECT = event.create(new RegistryBuilder<SkillAspectType>()
				.setName(new ResourceLocation(WuxiaCraft.MOD_ID, "skill_aspects"))
				.setType(SkillAspectType.class));
	}

}