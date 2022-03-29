package com.lazydragonstudios.wuxiacraft.container.slots;

import com.lazydragonstudios.wuxiacraft.blocks.entity.RunemakingTable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RunemakingResultSlot extends OutputSlot {

	public RunemakingResultSlot(Container container, int id, int x, int y) {
		super(container, id, x, y);
	}

	@Override
	public void onTake(Player pPlayer, ItemStack pStack) {
		if (!(this.container instanceof RunemakingTable table)) return;
		table.setItem(2, table.getResultItemStack());
		super.onTake(pPlayer, pStack);
	}
}
