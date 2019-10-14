package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.items.ItemScroll;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

	public static final int KEY_SPEED_UP = 0;
	public static final int KEY_SPEED_DOWN = 1;
	public static final int KEY_CULT_GUI = 2;
	public static final int KEY_ACIVATE_SKILL = 3;

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
	public void registerScrollModel(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("wuxiacraft:scroll", id));
	}

	@Override
	public void preInit() {
		super.preInit();
		WuxiaCraftConfig.clientPreInit();
	}

	@Override
	public void init() {
		super.init();
		keyBindings = new KeyBinding[4];
		keyBindings[KEY_SPEED_UP] = new KeyBinding("key.wuxiacraft.speed_up", Keyboard.KEY_EQUALS, "key.wuxiacraft.category");
		keyBindings[KEY_SPEED_DOWN] = new KeyBinding("key.wuxiacraft.speed_down", Keyboard.KEY_MINUS,"key.wuxiacraft.category");
		keyBindings[KEY_CULT_GUI] = new KeyBinding("key.wuxiacraft.cult_gui", Keyboard.KEY_K,"key.wuxiacraft.category");
		keyBindings[KEY_ACIVATE_SKILL] = new KeyBinding("key.wuxiacraft.activate_skill", Keyboard.KEY_F, "key.wuxiacraft.category");
		for(int i = 0; i < 4; i ++) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}
}
