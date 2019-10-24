package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.gui.SkillsGui;
import com.airesnor.wuxiacraft.items.ItemScroll;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

	public static final int KEY_SPEED_UP = 0;
	public static final int KEY_SPEED_DOWN = 1;
	public static final int KEY_CULT_GUI = 2;
	public static final int KEY_ACIVATE_SKILL = 3;
	public static final int KEY_SKILLS_GUI = 4;
	public static final int KEY_SELECT_UP = 5;
	public static final int KEY_SELECT_DOWN = 6;
	public static final int KEY_SELECT_1 = 7;
	public static final int KEY_SELECT_2 = 8;
	public static final int KEY_SELECT_3 = 9;
	public static final int KEY_SELECT_4 = 10;
	public static final int KEY_SELECT_5 = 11;
	public static final int KEY_SELECT_6 = 12;
	public static final int KEY_SELECT_7 = 13;
	public static final int KEY_SELECT_8 = 14;
	public static final int KEY_SELECT_9 = 15;
	public static final int KEY_SELECT_0 = 16;

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
		SkillsGui.init();
		keyBindings = new KeyBinding[17];
		keyBindings[KEY_SPEED_UP] = new KeyBinding("key.wuxiacraft.speed_up", Keyboard.KEY_EQUALS, "key.wuxiacraft.category");
		keyBindings[KEY_SPEED_DOWN] = new KeyBinding("key.wuxiacraft.speed_down", Keyboard.KEY_MINUS,"key.wuxiacraft.category");
		keyBindings[KEY_CULT_GUI] = new KeyBinding("key.wuxiacraft.cult_gui", Keyboard.KEY_K,"key.wuxiacraft.category");
		keyBindings[KEY_ACIVATE_SKILL] = new KeyBinding("key.wuxiacraft.activate_skill", Keyboard.KEY_F, "key.wuxiacraft.category");
		keyBindings[KEY_SKILLS_GUI] = new KeyBinding("key.wuxiacraft.skills_gui", Keyboard.KEY_L, "key.wuxiacraft.category");
		keyBindings[KEY_SELECT_UP] = new KeyBinding("key.wuxiacraft.select_up", Keyboard.KEY_V, "key.wuxiacraft.category");
		keyBindings[KEY_SELECT_DOWN] = new KeyBinding("key.wuxiacraft.select_down", Keyboard.KEY_C, "key.wuxiacraft.category");
		for(int i = 0; i < 10; i++) {
			int j = i == 9 ? 0 : (i+1);
			keyBindings[KEY_SELECT_1+i] = new KeyBinding("key.wuxiacraft.select_"+j, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, Keyboard.KEY_1+ i, "key.wuxiacraft.category");
		}
		for(int i = 0; i < keyBindings.length; i ++) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}
}