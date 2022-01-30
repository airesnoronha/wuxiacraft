package com.lazydragonstudios.wuxiacraft.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class IntrospectionMenu extends AbstractContainerMenu {

	public static MenuType<IntrospectionMenu> registryType;

	public static IntrospectionMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
		return new IntrospectionMenu(registryType, id);
	}

	public IntrospectionMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
		super(p_38851_, p_38852_);
	}

	@Override
	public boolean stillValid(Player p_38874_) {
		return true;
	}
}
