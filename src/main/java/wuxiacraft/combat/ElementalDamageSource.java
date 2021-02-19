package wuxiacraft.combat;

import net.minecraft.util.DamageSource;
import wuxiacraft.cultivation.Element;

public class ElementalDamageSource extends DamageSource {

	private final Element element;

	public ElementalDamageSource(String damageTypeIn, Element element) {
		super(damageTypeIn);
		this.element = element;
	}

	public Element getElement() {
		return element;
	}
}
