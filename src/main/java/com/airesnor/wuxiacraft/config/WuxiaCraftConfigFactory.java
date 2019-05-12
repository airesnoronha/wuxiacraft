package com.airesnor.wuxiacraft.config;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WuxiaCraftConfigFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new WuxiaCraftConfigGui(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	public static class WuxiaCraftConfigGui extends GuiConfig {

		public WuxiaCraftConfigGui(GuiScreen parentScreen) {
			super(parentScreen, getConfigElement(),WuxiaCraft.MODID, false, false, I18n.format("gui.config.main_title"));
		}

		private static List<IConfigElement> getConfigElement() {
			List<IConfigElement> list = new ArrayList<>();
			list.add(new DummyConfigElement.DummyCategoryElement(I18n.format("gui.config.category.gameplay"), "gui.config.category.gameplay", CategoryEntryGameplay.class));
			return list;
		}

		public static class CategoryEntryGameplay extends GuiConfigEntries.CategoryEntry {
			public CategoryEntryGameplay(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}

			@Override
			protected GuiScreen buildChildScreen() {
				Configuration config = WuxiaCraftConfig.getConfig();
				ConfigElement categoryGameplay = new ConfigElement(config.getCategory(WuxiaCraftConfig.CATEGORY_GAMEPLAY));
				List<IConfigElement> props = categoryGameplay.getChildElements();
				String title = I18n.format("gui.config.category.gameplay");
				return new GuiConfig(owningScreen, props, owningScreen.modID,
						this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
						this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, title);
			}
		}

	}
}
