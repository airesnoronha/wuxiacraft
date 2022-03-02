package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;

public class Element extends ForgeRegistryEntry<Element> {
	/**
	 * All elements that benefit from this one
	 */
	private final HashSet<ResourceLocation> begets;

	/**
	 * All elements that are obstructed by this one
	 */
	private final HashSet<ResourceLocation> suppresses;

	/**
	 * Stats that are going to get affected by this element's foundation
	 */
	private final HashMap<PlayerStat, BigDecimal> statsModifier;

	/**
	 * Stats that are going to add to the system specific stats
	 */
	private final HashMap<PlayerSystemStat, BigDecimal> systemStatModifier;

	/**
	 * Stats that are going to get affected by this system's foundation
	 */
	private final HashMap<PlayerElementalStat, BigDecimal> elementalStatModifier;

	/**
	 * Default constructor of element
	 */
	public Element() {
		this.begets = new HashSet<>();
		this.suppresses = new HashSet<>();
		this.statsModifier = new HashMap<>();
		this.systemStatModifier = new HashMap<>();
		this.elementalStatModifier = new HashMap<>();
	}

	/**
	 * @return This element's name
	 */
	public String getName() {
		if (this.getRegistryName() == null) return null;
		return this.getRegistryName().getPath();
	}

	/**
	 * Adds a begetter element at the construction
	 *
	 * @param element The element to be added
	 * @return This element
	 */
	public Element begets(ResourceLocation element) {
		begets.add(element);
		return this;
	}

	/**
	 * Adds an obstructed element at the construction
	 *
	 * @param element The element to be added
	 * @return This element
	 */
	public Element suppresses(ResourceLocation element) {
		suppresses.add(element);
		return this;
	}

	/**
	 * Adds a stat modifier for this element's foundation
	 *
	 * @param stat  the stat to be modified
	 * @param value the modifier
	 * @return this
	 */
	public Element statModifier(PlayerStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.statsModifier.put(stat, value);
		return this;
	}

	/**
	 * Adds a system stat modifier for this element's foundation for this system
	 *
	 * @param stat  the stat of the system to be added
	 * @param value the modifier
	 * @return this
	 */
	public Element statModifier(PlayerSystemStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.systemStatModifier.put(stat, value);
		return this;
	}

	/**
	 * The elemental stats this element is going to modify in it's foundation
	 *
	 * @param stat  the elemental stat to be modified
	 * @param value the modifier
	 * @return this
	 */
	public Element statModifier(PlayerElementalStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.elementalStatModifier.put(stat, value);
		return this;
	}

	/**
	 * Returns whether element argument benefits from this one
	 *
	 * @param element the element to check against
	 * @return true if element is benefited from this one
	 */
	public boolean begetsElement(ResourceLocation element) {
		return begets.contains(element);
	}

	/**
	 * Returns whether element argument is obstructed by this one
	 *
	 * @param element the element to check against
	 * @return true if element is obstructed by this one
	 */
	public boolean suppressesElement(ResourceLocation element) {
		return suppresses.contains(element);
	}

	/**
	 * Gets the stat modifier for this element's foundation
	 *
	 * @param stat The stat to be queried
	 * @return the modifier
	 */
	public BigDecimal getStatModifier(PlayerStat stat) {
		return this.statsModifier.getOrDefault(stat, BigDecimal.ZERO);
	}

	/**
	 * Gets the system stat modifier for this element's foundation
	 *
	 * @param stat The stat to be queried
	 * @return the modifier
	 */
	public BigDecimal getStatModifier(PlayerSystemStat stat) {
		return this.systemStatModifier.getOrDefault(stat, BigDecimal.ZERO);
	}

	/**
	 * Gets the elemental stat modifier for this element's foundation
	 *
	 * @param stat The stat to be queried
	 * @return the modifier
	 */
	public BigDecimal getStatModifier(PlayerElementalStat stat) {
		return this.elementalStatModifier.getOrDefault(stat, BigDecimal.ZERO);
	}

	/**
	 * Calculates the final foundation stat value
	 *
	 * @param stat       the stat to be queried
	 * @param foundation the foundation amount
	 * @return the stat value based on the foundation
	 */
	public BigDecimal getFoundationStatValue(PlayerStat stat, BigDecimal foundation) {
		return calculateStatValue(foundation, this.getStatModifier(stat));
	}

	/**
	 * Calculates the final foundation stat value
	 *
	 * @param stat       the stat to be queried
	 * @param foundation the foundation amount
	 * @return the stat value based on the foundation
	 */
	public BigDecimal getFoundationStatValue(PlayerSystemStat stat, BigDecimal foundation) {
		return calculateStatValue(foundation, this.getStatModifier(stat));
	}

	/**
	 * Calculates the final foundation stat value
	 *
	 * @param stat       the stat to be queried
	 * @param foundation the foundation amount
	 * @return the stat value based on the foundation
	 */
	public BigDecimal getFoundationStatValue(PlayerElementalStat stat, BigDecimal foundation) {
		return calculateStatValue(foundation, this.getStatModifier(stat));
	}

	public static BigDecimal calculateStatValue(BigDecimal foundation, BigDecimal modifier) {
		var mc = new MathContext(10);
		//modifier * anything = 0 so return 0
		if (modifier.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
		// modifier * sqrt(foundation)
		BigDecimal result = modifier.multiply(foundation.sqrt(mc));
		if (modifier.compareTo(BigDecimal.ZERO) < 0) {
			//check https://www.desmos.com/calculator/qedji8z48f x=foundation
			var deceleratedFoundation = foundation.multiply(new BigDecimal("0.000009"));
			//peak = -1000 for stats values when foundation is at 191000
			//modifier * (log(deceleratedFoundation+1)*6260) * (1 / (deceleratedFoundation+1))
			result = modifier
					.multiply(MathUtil.log10(deceleratedFoundation.add(BigDecimal.ONE).multiply(new BigDecimal("6260"))))
					.multiply(BigDecimal.ONE.divide(deceleratedFoundation.add(BigDecimal.ONE), RoundingMode.HALF_UP));
		}
		return result;
	}


}
