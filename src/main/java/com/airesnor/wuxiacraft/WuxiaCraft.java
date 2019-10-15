package com.airesnor.wuxiacraft;

import com.airesnor.wuxiacraft.commands.*;
import com.airesnor.wuxiacraft.proxy.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = WuxiaCraft.MODID, name = WuxiaCraft.NAME, version = WuxiaCraft.VERSION, guiFactory = WuxiaCraft.CONFIG_GUI_FACTORY)
public class WuxiaCraft {
	public static final String MODID = "wuxiacraft";
	public static final String NAME = "Wuxia Craft";
	public static final String VERSION = "@VERSION@";
	public static final String CONFIG_GUI_FACTORY = "com.airesnor.wuxiacraft.config.WuxiaCraftConfigFactory";

	public static Logger logger;

	@Mod.Instance(WuxiaCraft.MODID)
	public static WuxiaCraft instance;

	@SidedProxy(clientSide = "com.airesnor.wuxiacraft.proxy.ClientProxy", serverSide = "com.airesnor.wuxiacraft.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// some example code
		//logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		proxy.init();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CultivationCommand());
		event.registerServerCommand(new CultTechsCommand());
		event.registerServerCommand(new AdvCultLevel());
		event.registerServerCommand(new ResetCultCommand());
		event.registerServerCommand(new SkillsCommand());
	}
}
