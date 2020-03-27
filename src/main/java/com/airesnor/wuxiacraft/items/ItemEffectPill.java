package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class ItemEffectPill extends ItemBase {

	private List<PotionEffect> effects;

	private float amount;

	public ItemEffectPill(String name, float amount) {
		super(name);
		effects = new ArrayList<>();
		this.amount = amount;
		setCreativeTab(Items.PILLS);
	}

	public ItemEffectPill addEffect(PotionEffect effect) {
		this.effects.add(effect);
		return this;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			stack.shrink(player.isCreative() ? 0 : 1);
			if(this.amount <= player.getMaxHealth()*0.4) {
				if (stack.isEmpty())
					stack = ItemStack.EMPTY;
				for (PotionEffect effect : effects) {
					player.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), false, effect.doesShowParticles()));
				}
			} else {
				worldIn.createExplosion(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 3f, true);
				entityLiving.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.amount*2);
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
		return 20;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
