package com.lazydragonstudios.wuxiacraft.container.slots;

import com.lazydragonstudios.wuxiacraft.item.RuneStencil;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RunemakingStencilSlot extends Slot {

	public RunemakingStencilSlot(Container pContainer, int pIndex, int pX, int pY) {
		super(pContainer, pIndex, pX, pY);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() instanceof RuneStencil;
	}

}
