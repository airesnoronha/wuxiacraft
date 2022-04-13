package com.lazydragonstudios.wuxiacraft.container;

import com.lazydragonstudios.wuxiacraft.init.WuxiaMenuTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.LinkedList;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IntrospectionMenu extends AbstractContainerMenu {

	@SuppressWarnings("unused")
	public static IntrospectionMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
		return new IntrospectionMenu(WuxiaMenuTypes.INTROSPECTION_MENU.get(), id);
	}

	public IntrospectionMenu(@Nullable MenuType<?> menuType, int id) {
		super(menuType, id);
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
