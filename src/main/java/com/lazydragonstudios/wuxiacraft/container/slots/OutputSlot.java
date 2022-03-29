package com.lazydragonstudios.wuxiacraft.container.slots;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OutputSlot extends Slot {

	public OutputSlot(Container container, int id, int x, int y) {
		super(container, id, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}
