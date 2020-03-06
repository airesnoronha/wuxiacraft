package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.Blocks;
import com.airesnor.wuxiacraft.blocks.Cauldron;
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
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.world.WorldGen;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	/**
	 * Does in the server so this method is empty because server doesn't need textures i guess
	 * @param item
	 * @param meta
	 * @param id
	 */
	public void registerItemRenderer(Item item, int meta, String id) { }

	public void init() {

		CapabilityManager.INSTANCE.register(ICultivation.class, new CultivationStorage(), new CultivationFactory());
		CapabilityManager.INSTANCE.register(ICultTech.class, new CultTechStorage(), new CultTechFactory());
		CapabilityManager.INSTANCE.register(ISkillCap.class, new SkillsStorage(), new SkillsFactory());

		NetworkWrapper.INSTANCE.registerMessage(new EnergyMessageHandler(), EnergyMessage.class, 167002, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new ProgressMessageHandler(), ProgressMessage.class, 167003, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 167005, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new RequestCultGuiMessageHandler(), RequestCultGuiMessage.class, 167006, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new RemoveTechniqueMessageHandler(), RemoveTechniqueMessage.class, 167008, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new CastSkillMessageHandler(), CastSkillMessage.class, 167009, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SkillCapMessageHandler(), SkillCapMessage.class, 167011, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SelectSkillMessageHandler(), SelectSkillMessage.class, 167013, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new AskCultivationLevelMessageHandler(), AskCultivationLevelMessage.class, 167016, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new RespondCultivationLevelMessageHandler(), RespondCultivationLevelMessage.class, 167017, Side.SERVER);

		NetworkWrapper.INSTANCE.registerMessage(new CultivationMessageHandler(), CultivationMessage.class, 167001, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 167004, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SkillCapMessageHandler(), SkillCapMessage.class, 167010, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new CultTechMessageHandler(), CultTechMessage.class, 167007, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpawnParticleMessageHandler(), SpawnParticleMessage.class, 167012, Side.CLIENT);;
		NetworkWrapper.INSTANCE.registerMessage(new RespondCultivationLevelMessageHandler(), RespondCultivationLevelMessage.class, 167015, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new AskCultivationLevelMessageHandler(), AskCultivationLevelMessage.class, 167014, Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(WuxiaCraft.instance, new GuiHandler());

		Techniques.init();
		Element.init();
		Skills.init();
	}

	public void preInit() {
		WuxiaCraftConfig.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
	}

	public void registerScrollModel(Item item, int meta, String id) { }
}
