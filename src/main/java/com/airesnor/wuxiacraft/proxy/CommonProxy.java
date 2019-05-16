package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CapabilitiesHandler;
import com.airesnor.wuxiacraft.capabilities.CultivationFactory;
import com.airesnor.wuxiacraft.capabilities.CultivationStorage;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.world.WorldGen;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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

		NetworkWrapper.INSTANCE.registerMessage(new CultivationMessageHandler(), CultivationMessage.class, 167001, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new EnergyMessageHandler(), EnergyMessage.class, 167002, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new ProgressMessageHandler(), ProgressMessage.class, 167003, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 167004, Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(new SpeedHandicapMessageHandler(), SpeedHandicapMessage.class, 167005, Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(new RequestCultGuiMessageHandler(), RequestCultGuiMessage.class, 167006, Side.SERVER);

		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new RendererHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(WuxiaCraft.instance, new GuiHandler());
	}

	public void preInit() {
		WuxiaCraftConfig.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
	}

}
