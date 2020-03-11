package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSkillPill extends ItemBase {

	private ISkillAction action;

	public ItemSkillPill (String name) {
		super(name);
		this.action = null;
	}

	public ItemSkillPill setAction(ISkillAction acton) {
		this.action = acton;
		return this;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(entityLiving instanceof EntityPlayer) {
			if(this.action != null) {
				if(this.action.activate((EntityPlayer) entityLiving))
					stack.shrink(1);
			}
		}
		return stack;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 15;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}


}
