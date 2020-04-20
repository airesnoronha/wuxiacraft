package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

public class TechniqueWeapon extends Technique {

	public enum WeaponType {
		SWORD("sword"), AXE("axe"), PICK_AXE("pickaxe"), HOE("hoe"), SHOVEL("shovel");

		private final String uName;

		WeaponType(String uName) {
			this.uName = uName;
		}

		public String getName() {
			return I18n.format("wuxiacraft.label." + this.uName);
		}
	}

	private final WeaponType weaponType;

	public TechniqueWeapon(TechniqueTier tier, String uName, TechniquesModifiers baseModifiers, WeaponType weaponType) {
		super(tier, uName, baseModifiers);
		this.weaponType = weaponType;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	@Override
	public TechniquesModifiers updateSmallSuccess(EntityPlayer player, ICultivation cultivation) {
		boolean isWeapon = false;
		switch (this.weaponType) {
			case SWORD:
				if (player.getActiveItemStack().getItem() instanceof ItemSword) isWeapon = true;
				break;
			case AXE:
				if (player.getActiveItemStack().getItem() instanceof ItemAxe) isWeapon = true;
				break;
			case HOE:
				if (player.getActiveItemStack().getItem() instanceof ItemHoe) isWeapon = true;
				break;
			case SHOVEL:
				if (player.getActiveItemStack().getItem() instanceof ItemSpade) isWeapon = true;
				break;
			case PICK_AXE:
				if (player.getActiveItemStack().getItem() instanceof ItemPickaxe) isWeapon = true;
				break;
		}
		return isWeapon ? super.updateSmallSuccess(player, cultivation) : new TechniquesModifiers(0f, 0f, 0f, 0f, 0f);
	}

	@Override
	public TechniquesModifiers updateGreatSuccess(EntityPlayer player, ICultivation cultivation) {
		boolean isWeapon = false;
		switch (this.weaponType) {
			case SWORD:
				if (player.getActiveItemStack().getItem() instanceof ItemSword) isWeapon = true;
				break;
			case AXE:
				if (player.getActiveItemStack().getItem() instanceof ItemAxe) isWeapon = true;
				break;
			case HOE:
				if (player.getActiveItemStack().getItem() instanceof ItemHoe) isWeapon = true;
				break;
			case SHOVEL:
				if (player.getActiveItemStack().getItem() instanceof ItemSpade) isWeapon = true;
				break;
			case PICK_AXE:
				if (player.getActiveItemStack().getItem() instanceof ItemPickaxe) isWeapon = true;
				break;
		}
		return isWeapon ? super.updateGreatSuccess(player, cultivation) : new TechniquesModifiers(0f, 0f, 0f, 0f, 0f);
	}

	@Override
	TechniquesModifiers updatePerfection(EntityPlayer player, ICultivation cultivation) {
		boolean isWeapon = false;
		switch (this.weaponType) {
			case SWORD:
				if (player.getActiveItemStack().getItem() instanceof ItemSword) isWeapon = true;
				break;
			case AXE:
				if (player.getActiveItemStack().getItem() instanceof ItemAxe) isWeapon = true;
				break;
			case HOE:
				if (player.getActiveItemStack().getItem() instanceof ItemHoe) isWeapon = true;
				break;
			case SHOVEL:
				if (player.getActiveItemStack().getItem() instanceof ItemSpade) isWeapon = true;
				break;
			case PICK_AXE:
				if (player.getActiveItemStack().getItem() instanceof ItemPickaxe) isWeapon = true;
				break;
		}
		return isWeapon ? super.updatePerfection(player, cultivation) : new TechniquesModifiers(0f, 0f, 0f, 0f, 0f);
	}
}
