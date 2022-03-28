package com.lazydragonstudios.wuxiacraft.container;

import com.lazydragonstudios.wuxiacraft.item.SpatialItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SpatialSlot extends SlotItemHandler {

    public SpatialSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return super.mayPlace(stack) && !(stack.getItem() instanceof SpatialItem);
    }
}
