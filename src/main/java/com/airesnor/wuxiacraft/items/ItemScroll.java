package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.BaseSystemLevel;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemScroll extends Item {

	private final Technique technique;
	private final String techniqueTier;

	private static final List<String> tiers = new ArrayList<>();
	{
		tiers.add(TextFormatting.GRAY.toString() + "Tricks");
		tiers.add(TextFormatting.GRAY.toString() + "Low Human");
		tiers.add(TextFormatting.GREEN.toString() + "Average Human");
		tiers.add(TextFormatting.GOLD.toString() + "High Human");
		tiers.add(TextFormatting.BLUE.toString() + "Low Earth");
		tiers.add(TextFormatting.RED.toString() + "Average Earth");
		tiers.add(TextFormatting.AQUA.toString() + "High Earth");
		tiers.add(TextFormatting.BOLD.toString() + TextFormatting.YELLOW.toString() + "Low Heaven");
		tiers.add(TextFormatting.BOLD.toString() + TextFormatting.WHITE.toString() + "Average Heaven");
		tiers.add(TextFormatting.BOLD.toString() + TextFormatting.LIGHT_PURPLE.toString() + "High Heaven");
	}


	public ItemScroll(Technique technique) {
		setUnlocalizedName(technique.getUName() + ".scroll");
		setRegistryName(technique.getUName() + ".scroll");
		setCreativeTab(WuxiaItems.SCROLLS);
		this.technique = technique;
		this.maxStackSize = 1;
		WuxiaItems.ITEMS.add(this);
		final double modifier = this.technique.getEfficientTillModifier();
		BaseSystemLevel aux = BaseSystemLevel.DEFAULT_ESSENCE_LEVEL;
		int i = 0;
		while(aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS) != aux) {
			i++;
			aux = aux.nextLevel(BaseSystemLevel.ESSENCE_LEVELS);
			if(aux.baseModifier > modifier) {
				i--;
				break;
			}
		}
		this.techniqueTier = tiers.get(i);

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ICultTech cultTech = CultivationUtils.getCultTechFromEntity(playerIn);
		boolean success = cultTech.addTechnique(this.technique);
		EnumActionResult result = success ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		return playerIn.isCreative() ? ActionResult.newResult(result, new ItemStack(this)) : ActionResult.newResult(result, success ? ItemStack.EMPTY : new ItemStack(this));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String system = "Essence Cultivation";
		if(this.technique.getSystem() == Cultivation.System.BODY) {
			system = "Body Cultivation";
		}
		else if(this.technique.getSystem() == Cultivation.System.DIVINE) {
			system = "Divine Cultivation";
		}
		tooltip.add(system);
		tooltip.add(this.techniqueTier);
		for (Element el : this.technique.getElements()) {
			String line = el.getColor() + el.getName();
			tooltip.add(line);
		}
	}
}
