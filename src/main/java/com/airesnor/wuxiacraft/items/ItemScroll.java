package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.TechniqueWeapon;
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

import javax.annotation.Nullable;
import java.util.List;

public class ItemScroll extends Item implements IHasModel {

	private Technique technique;

	public ItemScroll(Technique technique) {
		setUnlocalizedName(technique.getUName() + "_scroll");
		setRegistryName(technique.getUName() + "_scroll");
		setCreativeTab(Items.SCROLLS);
		this.technique = technique;
		this.maxStackSize = 1;
		Items.ITEMS.add(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ICultTech cultTech = playerIn.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		boolean success = false;
		if (cultTech != null) {
			success = cultTech.addTechnique(this.technique, 0);
		}
		return playerIn.isCreative() ? ActionResult.newResult(EnumActionResult.SUCCESS, new ItemStack(this)) : ActionResult.newResult(EnumActionResult.SUCCESS, success ? ItemStack.EMPTY : new ItemStack(this));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (this.technique instanceof TechniqueWeapon) {
			TechniqueWeapon.WeaponType weaponType = ((TechniqueWeapon) this.technique).getWeaponType();
			String line = TextFormatting.WHITE + weaponType.getName() + " " + I18n.format("wuxiacraft.label.technique");
			tooltip.add(line);
		}
		for (Element el : this.technique.getElements()) {
			String line = el.getColor() + el.getName();
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().armor != 0) {
			double armor = technique.getBaseModifiers().armor;
			int signals = (int) Math.abs(armor / 0.1f);
			String color = armor < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Armor ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(armor > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().attackSpeed != 0) {
			double attackSpeed = technique.getBaseModifiers().attackSpeed;
			int signals = (int) Math.abs(attackSpeed / 0.1f);
			String color = attackSpeed < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Attack Speed ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(attackSpeed > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().maxHealth != 0) {
			double maxHealth = technique.getBaseModifiers().maxHealth;
			int signals = (int) Math.abs(maxHealth / 0.1f);
			String color = maxHealth < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Max Health ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(maxHealth > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().movementSpeed != 0) {
			double movementSpeed = technique.getBaseModifiers().movementSpeed;
			int signals = (int) Math.abs(movementSpeed / 0.1f);
			String color = movementSpeed < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Speed ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(movementSpeed > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		if (technique.getBaseModifiers().strength != 0) {
			double strength = technique.getBaseModifiers().strength;
			int signals = (int) Math.abs(strength / 0.1f);
			String color = strength < 0 ? TextFormatting.RED.toString() : signals < 3 ? TextFormatting.GREEN.toString() : TextFormatting.AQUA.toString();
			String line = color + "Strength ";
			for (int i = 0; i < signals; i++) {
				line = line.concat(strength > 0 ? "+" : "-");
			}
			tooltip.add(line);
		}
		for (PotionEffect p : technique.getPerfectionCompletionEffects()) {
			String line = TextFormatting.GOLD + I18n.format(p.getEffectName());
			tooltip.add(line);
		}
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerScrollModel(this, 0, "inventory");
	}
}
