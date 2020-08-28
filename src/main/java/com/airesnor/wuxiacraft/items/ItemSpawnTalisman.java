package com.airesnor.wuxiacraft.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemSpawnTalisman extends ItemBase {

	public ItemSpawnTalisman(String item_name) {
		super(item_name);
		setMaxStackSize(16);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack actualTalisman = playerIn.getHeldItem(handIn);
		NBTTagCompound tag = actualTalisman.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		boolean activated = false;
		BlockPos targetPos = new BlockPos(0, 0, 0);
		if (worldIn.provider.getDimension() == 0) { //only works at over world
			int usageStep = tag.hasKey("usageStep") ? tag.getInteger("usageStep") : 0;
			switch (usageStep) {
				case 0:
					targetPos = worldIn.getSpawnPoint();
					tag.setInteger("usageStep", 1); // to use next
					tag.setInteger("iniX", (int) playerIn.posX);
					tag.setInteger("iniY", (int) playerIn.posY);
					tag.setInteger("iniZ", (int) playerIn.posZ);
					activated = true;
					break;
				case 1:
					if (tag.hasKey("iniX") && tag.hasKey("iniY") && tag.hasKey("iniZ")) {
						targetPos = new BlockPos(tag.getInteger("iniX"), tag.getInteger("iniY"), tag.getInteger("iniZ"));
						tag.setInteger("usageStep", 0);
						tag.removeTag("iniX");//save space
						tag.removeTag("iniY");
						tag.removeTag("iniZ");
						activated = true;
						break;
					} else {
						//probably wrong usage, so tp to spawn
						targetPos = worldIn.getSpawnPoint();
						tag.setInteger("usageStep", 1);
						tag.setInteger("iniX", (int) playerIn.posX);
						tag.setInteger("iniY", (int) playerIn.posY);
						tag.setInteger("iniZ", (int) playerIn.posZ);
						activated = true;
					}
			}
		}
		actualTalisman.setTagCompound(tag);
		if (activated) {
			playerIn.setPositionAndUpdate(targetPos.getX(), targetPos.getY(), targetPos.getZ());
			if(tag.getInteger("usageStep") == 0) { // which means will go back to where player was
				actualTalisman.shrink(1);
				if (actualTalisman.getCount() <= 0) {
					actualTalisman = ItemStack.EMPTY;
				}
			}
		}
		return new ActionResult<>(activated ? EnumActionResult.SUCCESS : EnumActionResult.PASS, actualTalisman);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		int usageStep = tag.hasKey("usageStep") ? tag.getInteger("usageStep") : 0;
		if(usageStep == 0) {
			tooltip.add(TextFormatting.GOLD + "Going to world center");
		} else if (usageStep == 1){
			tooltip.add(TextFormatting.GREEN + "Returning to previous position");
		}
	}
}
