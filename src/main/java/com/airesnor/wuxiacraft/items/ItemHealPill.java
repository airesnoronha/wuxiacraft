package com.airesnor.wuxiacraft.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemHealPill extends ItemBase {
	private float amount;

	public ItemHealPill(String name, float amount) {
		super(name);
		this.amount = amount;
		setCreativeTab(Items.PILLS);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			stack.shrink(player.isCreative() ? 0 : 1);
			if (stack.isEmpty())
				stack = ItemStack.EMPTY;
			if(this.amount <= player.getMaxHealth()) {
				player.heal(this.amount);
			} else {
				worldIn.createExplosion(player, player.posX, player.posY, player.posZ, 3f, true);
				player.attackEntityFrom(DamageSource.causeExplosionDamage(player), this.amount);
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

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

}
