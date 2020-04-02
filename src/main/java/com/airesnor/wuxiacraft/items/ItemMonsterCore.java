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

public class ItemMonsterCore extends ItemBase {

	private int useDuration;
	private ISkillAction action;
	private ISkillAction whenUsing;

	public ItemMonsterCore(String item_name) {
		super(item_name);
		setMaxStackSize(1);
		setUseDuration(100);
		setCreativeTab(Items.MONSTER_CORES);
		this.action = actor -> true;
		this.whenUsing = actor -> true;
	}

	@Override
	public int getItemEnchantability() {
		return super.getItemEnchantability();
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	public ItemMonsterCore setUseDuration(int useDuration) {
		this.useDuration = useDuration;
		this.setMaxDamage(this.useDuration);
		return this;
	}

	public ItemMonsterCore setUseAction(ISkillAction action) {
		this.action = action;
		return this;
	}

	public ItemMonsterCore setWhenUsing(ISkillAction action) {
		this.whenUsing = action;
		return this;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 5;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(stack.getItemDamage() < stack.getMaxDamage()) {
			if(this.whenUsing.activate(entityLiving)) {
				stack.damageItem(1, entityLiving);
			}
		}
		else if(this.action.activate(entityLiving)) {
			stack.damageItem(1, entityLiving);
		}
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
