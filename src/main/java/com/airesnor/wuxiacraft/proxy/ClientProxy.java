package com.airesnor.wuxiacraft.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

	/**
	 * Does forge stuff for registering textures
	 * @param item item to receive the texture
	 * @param meta perhaps the item texture can be based on its metadata
	 * @param id string name of the item
	 */
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

}
