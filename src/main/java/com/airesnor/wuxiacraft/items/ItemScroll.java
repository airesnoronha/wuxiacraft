package com.airesnor.wuxiacraft.items;

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
import java.util.List;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemScroll extends Item {

	private final Technique technique;

	public ItemScroll(Technique technique) {
		setUnlocalizedName(technique.getUName() + "_scroll");
		setRegistryName(technique.getUName() + "_scroll");
		setCreativeTab(WuxiaItems.SCROLLS);
		this.technique = technique;
		this.maxStackSize = 1;
		WuxiaItems.ITEMS.add(this);
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
		for (Element el : this.technique.getElements()) {
			String line = el.getColor() + el.getName();
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().armor != 0) {
			double armor = technique.getBaseModifiers().armor;
			int signals = (int) Math.abs(armor);
			String color = armor < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Armor ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(armor > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().attackSpeed != 0) {
			double attackSpeed = technique.getBaseModifiers().attackSpeed;
			int signals = (int) Math.abs(attackSpeed);
			String color = attackSpeed < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Attack Speed ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(attackSpeed > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().maxHealth != 0) {
			double maxHealth = technique.getBaseModifiers().maxHealth;
			int signals = (int) Math.abs(maxHealth);
			String color = maxHealth < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Max Health ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(maxHealth > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().movementSpeed != 0) {
			double movementSpeed = technique.getBaseModifiers().movementSpeed;
			int signals = (int) Math.abs(movementSpeed);
			String color = movementSpeed < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Speed ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(movementSpeed > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().strength != 0) {
			double strength = technique.getBaseModifiers().strength;
			int signals = (int) Math.abs(strength);
			String color = strength < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Strength ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(strength > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		for (Pair<Double, PotionEffect> p : technique.getEffects()) {
			String line = TextFormatting.GOLD + I18n.format(p.getValue().getEffectName());
			tooltip.add(line);
		}
	}
}
