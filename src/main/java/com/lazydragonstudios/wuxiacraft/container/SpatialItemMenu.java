package com.lazydragonstudios.wuxiacraft.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;

public class SpatialItemMenu extends InventoryMenu {

    public static MenuType<SpatialItemMenu> registryType;

    public SpatialItemMenu(Player player) {
        super(player.getInventory(), !player.level.isClientSide(), player);
    }
}
