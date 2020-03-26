package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemSkillPill extends ItemBase {

	private ISkillAction action;

	private float amount;

	public ItemSkillPill(String name, float amount) {
		super(name);
		this.action = null;
		this.amount = amount;
	}

	public ItemSkillPill setAction(ISkillAction acton) {
		this.action = acton;
		return this;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			if (this.action != null) {
				if(this.amount <= entityLiving.getMaxHealth() * 0.4F) {
					if (this.action.activate((EntityPlayer) entityLiving))
						stack.shrink(1);
				} else {
					stack.shrink(1);
					worldIn.createExplosion(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 3f, true);
					entityLiving.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.amount*2);
				}
			}
		}
		return stack;
	}

	@Override
	@Nonnull
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 15;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}


}
