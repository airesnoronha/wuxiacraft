package wuxiacraft.cultivation;

import com.google.common.collect.ImmutableList;
import wuxiacraft.combat.ElementalDamageSource;
import wuxiacraft.handler.CombatHandler;
import wuxiacraft.init.WuxiaElements;

import java.util.List;
import java.util.LinkedList;

public class Element {

	public final String name;

	private final List<Element> begets;
	private final List<Element> overcomes;

	public Element(String name) {
		this.name = name;
		this.begets = new LinkedList<>();
		this.overcomes = new LinkedList<>();
		WuxiaElements.ELEMENTS.add(this);
		WuxiaElements.ELEMENTAL_DAMAGE_SOURCES.add(new ElementalDamageSource("source."+name, this));
	}

	public Element begets(Element element) {
		this.begets.add(element);
		return this;
	}

	public Element overcomes(Element element) {
		this.overcomes.add(element);
		return this;
	}

	public ImmutableList<Element> getBegetsList() {
		return ImmutableList.copyOf(this.begets);
	}

	public ImmutableList<Element> getOvercomesList() {
		return ImmutableList.copyOf(this.overcomes);
	}
}
