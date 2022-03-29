package com.lazydragonstudios.wuxiacraft.container.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InscriberBookSlot extends Slot {

	public InscriberBookSlot(Container container, int id, int x, int y) {
		super(container, id, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() == Items.BOOK;
	}
}
