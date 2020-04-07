package com.airesnor.wuxiacraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemPaintBrush extends ItemBase {

	public ItemPaintBrush(String item_name) {
		super(item_name);
		setMaxDamage(200);
		setMaxStackSize(1);
	}

}
