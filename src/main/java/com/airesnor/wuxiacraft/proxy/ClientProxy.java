package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.OBJBlockModelLoader;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.entities.mobs.GiantBee;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.mobs.renders.RenderGiantAnt;
import com.airesnor.wuxiacraft.entities.mobs.renders.RenderGiantBee;
import com.airesnor.wuxiacraft.entities.mobs.renders.RenderWanderingCultivator;
import com.airesnor.wuxiacraft.entities.skills.SwordBeamThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterBladeThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterNeedleThrowable;
import com.airesnor.wuxiacraft.entities.skills.models.RenderSwordBeam;
import com.airesnor.wuxiacraft.entities.skills.models.RenderWaterBlade;
import com.airesnor.wuxiacraft.entities.skills.models.RenderWaterNeedle;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTESR;
import com.airesnor.wuxiacraft.gui.SkillsGui;
import com.airesnor.wuxiacraft.handlers.PreClientEvents;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import com.airesnor.wuxiacraft.utils.OreUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

public class ClientProxy extends CommonProxy {

	public static final int KEY_SPEED_UP = 0;
	public static final int KEY_SPEED_DOWN = 1;
	public static final int KEY_CULT_GUI = 2;
	public static final int KEY_ACTIVATE_SKILL = 3;
	public static final int KEY_SKILLS_GUI = 4;
	public static final int KEY_SELECT_UP = 5;
	public static final int KEY_SELECT_DOWN = 6;
	public static final int KEY_SELECT_1 = 7;
	public static final int KEY_UNLEASH_SPEED = 17;

	public static KeyBinding[] keyBindings;

	/**
	 * Does forge stuff for registering textures
	 *
	 * @param item item to receive the texture
	 * @param meta perhaps the item texture can be based on its metadata
	 * @param id   string name of the variant
	 */
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), id));
	}

	@Override
	public void registerScrollModel(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("wuxiacraft:scroll", id));
	}

	@Override
	public void registerCustomModelLocation(Item item, int meta, String id, String location) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, id));
	}

	@Override
	public void preInit() {
		super.preInit();

		OBJLoader.INSTANCE.addDomain(WuxiaCraft.MOD_ID);

		ModelLoaderRegistry.registerLoader(OBJBlockModelLoader.INSTANCE);

		MinecraftForge.EVENT_BUS.register(new PreClientEvents());

		WuxiaCraftConfig.clientPreInit();

		RenderingRegistry.registerEntityRenderingHandler(WaterNeedleThrowable.class, RenderWaterNeedle::new);
		RenderingRegistry.registerEntityRenderingHandler(WaterBladeThrowable.class, RenderWaterBlade::new);
		RenderingRegistry.registerEntityRenderingHandler(SwordBeamThrowable.class, RenderSwordBeam::new);
		RenderingRegistry.registerEntityRenderingHandler(GiantAnt.class, RenderGiantAnt::new);
		RenderingRegistry.registerEntityRenderingHandler(GiantBee.class, RenderGiantBee::new);
		RenderingRegistry.registerEntityRenderingHandler(WanderingCultivator.class, RenderWanderingCultivator::new);

		MinecraftForge.EVENT_BUS.register(new RendererHandler());
	}

	@Override
	public void init() {
		super.init();

		SkillsGui.init();

		keyBindings = new KeyBinding[18];
		keyBindings[KEY_SPEED_UP] = new KeyBinding("key.wuxiacraft.speed_up", Keyboard.KEY_EQUALS, "key.wuxiacraft.category");
		keyBindings[KEY_SPEED_DOWN] = new KeyBinding("key.wuxiacraft.speed_down", Keyboard.KEY_MINUS, "key.wuxiacraft.category");
		keyBindings[KEY_CULT_GUI] = new KeyBinding("key.wuxiacraft.cult_gui", Keyboard.KEY_K, "key.wuxiacraft.category");
		keyBindings[KEY_ACTIVATE_SKILL] = new KeyBinding("key.wuxiacraft.activate_skill", Keyboard.KEY_F, "key.wuxiacraft.category");
		keyBindings[KEY_SKILLS_GUI] = new KeyBinding("key.wuxiacraft.skills_gui", Keyboard.KEY_L, "key.wuxiacraft.category");
		keyBindings[KEY_SELECT_UP] = new KeyBinding("key.wuxiacraft.select_up", Keyboard.KEY_V, "key.wuxiacraft.category");
		keyBindings[KEY_SELECT_DOWN] = new KeyBinding("key.wuxiacraft.select_down", Keyboard.KEY_C, "key.wuxiacraft.category");
		for (int i = 0; i < 10; i++) {
			int j = i == 9 ? 0 : (i + 1);
			keyBindings[KEY_SELECT_1 + i] = new KeyBinding("key.wuxiacraft.select_" + j, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, Keyboard.KEY_1 + i, "key.wuxiacraft.category");
		}
		keyBindings[KEY_UNLEASH_SPEED] = new KeyBinding("key.wuxiacraft.unleash_speed", Keyboard.KEY_U, "key.wuxiacraft.category");
		for (KeyBinding keyBinding : keyBindings) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}

		RenderGiantAnt.init();
		RenderGiantBee.init();
		SpiritStoneStackTESR.init();
	}
}
