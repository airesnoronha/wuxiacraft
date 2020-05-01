package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemFastingPill extends ItemBase {

	private static final Field foodStats = ReflectionHelper.findField(FoodStats.class, "foodSaturationLevel", "field_75125_b");

	static {
		foodStats.setAccessible(true);
	}

	private final float amount;

	public ItemFastingPill(String item_name, float amount) {
		super(item_name);
		this.amount = amount;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			IFoundation foundation = CultivationUtils.getFoundationFromEntity(entityLiving);
			if (this.amount <= 25f + (foundation.getConstitutionModifier() * 0.4f) * 3F) {
				EntityPlayer player = (EntityPlayer) entityLiving;
				player.getFoodStats().setFoodLevel(20);
				try {
					foodStats.setFloat(player.getFoodStats(), this.amount);
				} catch (Exception e) {
					WuxiaCraft.logger.error("Couldn't help with food, sorry!");
					e.printStackTrace();
				}
				stack.shrink(1);
			} else {
				stack.shrink(1);
				worldIn.createExplosion(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 3f, true);
				entityLiving.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), (float) (this.amount-foundation.getConstitutionModifier()) * 2);
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
		return 30;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
