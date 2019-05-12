package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.command.CommandBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(Items.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for(Item item : Items.ITEMS) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
	}

}
