package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.container.InscriberMenu;
import com.lazydragonstudios.wuxiacraft.container.IntrospectionMenu;
import com.lazydragonstudios.wuxiacraft.container.RunemakingMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WuxiaMenuTypes {

	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, WuxiaCraft.MOD_ID);

	public static RegistryObject<MenuType<IntrospectionMenu>> INTROSPECTION_MENU = MENU_TYPES.register("introspection_menu", () -> IForgeMenuType.create(IntrospectionMenu::create));

	public static RegistryObject<MenuType<InscriberMenu>> INSCRIBER_MENU = MENU_TYPES.register("inscriber_menu", () -> IForgeMenuType.create(InscriberMenu::create));

	public static RegistryObject<MenuType<RunemakingMenu>> RUNEMAKING_MENU = MENU_TYPES.register("runemaking_menu", () -> IForgeMenuType.create(RunemakingMenu::create));

}
