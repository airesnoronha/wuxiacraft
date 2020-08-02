package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
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

	public TechniqueWeapon(Cultivation.System system, String uName, TechniquesModifiers baseModifiers, WeaponType weaponType) {
		super(system, uName, baseModifiers);
		this.weaponType = weaponType;
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}
}
