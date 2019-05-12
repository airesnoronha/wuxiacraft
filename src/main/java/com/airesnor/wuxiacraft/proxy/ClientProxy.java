package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

	public static KeyBinding[] keyBindings;

	/**
	 * Does forge stuff for registering textures
	 * @param item item to receive the texture
	 * @param meta perhaps the item texture can be based on its metadata
	 * @param id string name of the item
	 */
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	@Override
	public void preInit() {
		super.preInit();
		WuxiaCraftConfig.clientPreInit();
	}

	@Override
	public void init() {
		super.init();
		keyBindings = new KeyBinding[2];
		keyBindings[0] = new KeyBinding("key.wuxiacraft.speed_up", Keyboard.KEY_EQUALS, "key.wuxiacraft.category");
		keyBindings[1] = new KeyBinding("key.wuxiacraft.speed_down", Keyboard.KEY_MINUS,"key.wuxiacraft.category");

		for(int i = 0; i < 2; i ++) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}
}
