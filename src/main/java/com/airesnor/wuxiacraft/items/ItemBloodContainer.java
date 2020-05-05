package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ItemBloodContainer extends ItemBase {

	public ItemBloodContainer(String item_name) {
		super(item_name);
		this.setMaxDamage(35);
		this.setMaxStackSize(1);
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerCustomModelLocation(this, 0, "inventory", "wuxiacraft:blood_bottle");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.getTagCompound() != null) {
			NBTTagCompound tag = stack.getTagCompound();
			if(tag.hasKey("bloodLevel")) {
				CultivationLevel level = CultivationLevel.LOADED_LEVELS.get(tag.getString("bloodLevel"));
				if(level!=null)
					tooltip.add(level.getLevelName(-1));
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

}
