package com.lazydragonstudios.wuxiacraft.container.slots;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class SpatialItemSlot extends Slot {

    public SpatialItemSlot(Container container, int id, int x, int y) {
        super(container, id, x, y);
    }

    @Override
    public boolean mayPickup(Player p_40228_) {
        return false;
    }
}
