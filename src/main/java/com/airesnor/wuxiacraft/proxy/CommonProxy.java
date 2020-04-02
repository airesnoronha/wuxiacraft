package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.*;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.OreUtils;
import com.airesnor.wuxiacraft.world.WorldGen;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
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

		NetworkWrapper.INSTANCE.registerMessage(new CultivationMessageHandler(), CultivationMessage.class, 167016, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new CultTechMessageHandler(), CultTechMessage.class, 167017, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new RespondCultivationLevelMessageHandler(), RespondCultivationLevelMessage.class, 167018, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SkillCapMessageHandler(), SkillCapMessage.class, 167019, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpawnParticleMessageHandler(), SpawnParticleMessage.class, 167020, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 167021, Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(WuxiaCraft.instance, new GuiHandler());

		Techniques.init();
		Element.init();
		Skills.init();
	}

	public void preInit() {
		try {
			Field mf = Field.class.getDeclaredField("modifiers");
			mf.setAccessible(true);

			Field f = ReflectionHelper.findField(SharedMonsterAttributes.class, "MAX_HEALTH", "field_111267_a");
			f.setAccessible(true);

			mf.set(f, f.getModifiers() & ~Modifier.FINAL);
			f.set(null, (new RangedAttribute(null, "generic.maxHealth", 20.0D, Float.MIN_VALUE, 10000000.0D)).setDescription("Max Health").setShouldWatch(true));

			mf.set(f, f.getModifiers() & Modifier.FINAL);

			WuxiaCraft.logger.info("Overriding max health succeeded.");
		} catch (Exception e) {
			WuxiaCraft.logger.error("Error overriding max health: " + e.getMessage());
			e.printStackTrace();
		}
		WuxiaCraftConfig.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
	}

	public void registerScrollModel(Item item, int meta, String id) {
	}

	public void registerCustomModelLocation(Item item, int meta, String id, String location) {
	}
}
