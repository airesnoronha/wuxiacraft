package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.WuxiaBlocks;
import com.airesnor.wuxiacraft.capabilities.*;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.ISealing;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import com.airesnor.wuxiacraft.dimensions.WuxiaDimensions;
import com.airesnor.wuxiacraft.dimensions.biomes.WuxiaBiomes;
import com.airesnor.wuxiacraft.dimensions.worldtypes.WorldTypeRegister;
import com.airesnor.wuxiacraft.formation.FormationEventHandler;
import com.airesnor.wuxiacraft.utils.FormationUtils;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import com.airesnor.wuxiacraft.handlers.RegistryHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.CultivationLoader;
import com.airesnor.wuxiacraft.utils.OreUtils;
import com.airesnor.wuxiacraft.world.WorldGen;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CommonProxy {

	/**
	 * Does in the server so this method is empty because server doesn't need textures i guess
	 *
	 * @param item The item to be rendered
	 * @param meta The item meta to be rendered
	 * @param id the block state variation
	 */
	public void registerItemRenderer(Item item, int meta, String id) {
	}

	public void init() {

		OreUtils.loadOresToFind();

		CapabilityManager.INSTANCE.register(ICultivation.class, new CultivationStorage(), new CultivationFactory());
		CapabilityManager.INSTANCE.register(ICultTech.class, new CultTechStorage(), new CultTechFactory());
		CapabilityManager.INSTANCE.register(ISkillCap.class, new SkillsStorage(), new SkillsFactory());
		CapabilityManager.INSTANCE.register(IFoundation.class, new FoundationStorage(), new FoundationFactory());
		CapabilityManager.INSTANCE.register(ISealing.class, new SealingStorage(), new SealingFactory());

		NetworkWrapper.INSTANCE.registerMessage(new ActivatePartialSkillMessageHandler(), ActivatePartialSkillMessage.class, 167001, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new ActivateSkillMessageHandler(), ActivateSkillMessage.class, 167002, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new AddRecipeItemMessageHandler(), AddRecipeItemMessage.class, 167003, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new AskCultivationLevelMessageHandler(), AskCultivationLevelMessage.class, 167004, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new CastSkillMessageHandler(), CastSkillMessage.class, 167005, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new EnergyMessageHandler(), EnergyMessage.class, 167006, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new ProgressMessageHandler(), ProgressMessage.class, 167007, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new RemoveTechniqueMessageHandler(), RemoveTechniqueMessage.class, 167008, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new RequestCultGuiMessageHandler(), RequestCultGuiMessage.class, 167009, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SelectSkillMessageHandler(), SelectSkillMessage.class, 167010, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new ShrinkEntityItemMessageHandler(), ShrinkEntityItemMessage.class, 167011, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SkillCapMessageHandler(), SkillCapMessage.class, 167012, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SpawnParticleMessageHandler(), SpawnParticleMessage.class, 167013, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 167014, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SuppressCultivationMessageHandler(), SuppressCultivationMessage.class, 167015, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new AddProgressToFoundationAttributeMessageHandler(), AddProgressToFoundationAttributeMessage.class, 167016, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SelectFoundationAttributeMessageHandler(), SelectFoundationAttributeMessage.class, 167017, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new CapabilityRequestMessageHandler(), CapabilityRequestMessage.class, 167018, Side.SERVER);

		NetworkWrapper.INSTANCE.registerMessage(new CultivationMessageHandler(), CultivationMessage.class, 168001, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new CultTechMessageHandler(), CultTechMessage.class, 168002, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new RespondCultivationLevelMessageHandler(), RespondCultivationLevelMessage.class, 168003, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SkillCapMessageHandler(), SkillCapMessage.class, 168004, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpawnParticleMessageHandler(), SpawnParticleMessage.class, 168005, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 168006, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new UnifiedCapabilitySyncMessageHandler(), UnifiedCapabilitySyncMessage.class, 168007, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new CultivationLevelsMessageHandler(), CultivationLevelsMessage.class, 168008, Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new FormationEventHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(WuxiaCraft.instance, new GuiHandler());

		Techniques.init();
		Element.init();
		Skills.init();
		FormationUtils.init();

		RegistryHandler.registerSmeltingRecipes();

		//WorldType TEST = new WorldTypeTest("Test");
		//WorldType WUXIATEST = new WorldTypeWuxiaTest("WuxiaTest");
		WorldTypeRegister.registerWorldTypes();
	}

	public void preInit() {
		CultivationLoader.loadLevelsFromConfig();

		reflectOnField(SharedMonsterAttributes.class, new String[]{"MAX_HEALTH", "field_111267_a"}, (new RangedAttribute(null, "generic.maxHealth", 20.0D, Float.MIN_VALUE, 1000000000000000.0D)).setDescription("Max Health").setShouldWatch(true));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"ARMOR", "field_188791_g"}, (new RangedAttribute(null, "generic.armor", 0.0D, 0.0D, 100000000000000.0D)).setShouldWatch(true));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"MOVEMENT_SPEED", "field_111263_d"}, (new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071D, 0.0D, 4096.0D)).setDescription("Movement Speed").setShouldWatch(true));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"ATTACK_DAMAGE", "field_111264_e"}, new RangedAttribute(null, "generic.attackDamage", 2.0D, 0.0D, 100000000000000.0D));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"ATTACK_SPEED", "field_188790_f"}, (new RangedAttribute(null, "generic.attackSpeed", 4.0D, 0.0D, 100000000000000.0D)).setShouldWatch(true));

		WuxiaCraftConfig.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		registerRuneBlocks();
		WuxiaBiomes.registerBiomes();
		WuxiaDimensions.registerDimensions();
	}

	private void registerRuneBlocks() {
		WuxiaBlocks.initBloodRunes();
		//ForgeRegistries.BLOCKS.registerAll(Blocks.BLOOD_RUNES.values().toArray(new Block[0]));
	}

	public void registerScrollModel(Item item, int meta, String id) {
	}

	public void registerCustomModelLocation(Item item, int meta, String id, String location) {
	}

	public void reflectOnField(Class<?> clazz, String [] names, Object newValue) {
		try {
			Field mf = Field.class.getDeclaredField("modifiers");
			mf.setAccessible(true);

			Field f = ReflectionHelper.findField(clazz, names);
			f.setAccessible(true);

			mf.set(f, f.getModifiers() & ~Modifier.FINAL);
			f.set(null, newValue);


			mf.set(f, f.getModifiers() & Modifier.FINAL);

			WuxiaCraft.logger.info("Overriding field "+names[0]+" succeeded.");
		} catch (Exception e) {
			WuxiaCraft.logger.error("Error overriding field '"+names[0]+"': " + e.getMessage());
			e.printStackTrace();
		}
	}
}
