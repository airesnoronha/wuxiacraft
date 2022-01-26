package wuxiacraft.cultivation.technique;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.cultivation.stats.PlayerStat;

import java.math.BigDecimal;
import java.util.HashMap;

public class TechniqueModifier {

	public HashMap<PlayerStat, BigDecimal> stats = new HashMap<>();

	public HashMap<ResourceLocation, Double> elements = new HashMap<>();

	public TechniqueModifier() {
	}

	public void add(TechniqueModifier tMod) {
		for (var stat : tMod.stats.keySet()) {
			if (this.stats.containsKey(stat)) {
				this.stats.put(stat, this.stats.get(stat).add(tMod.stats.get(stat)));
			} else {
				this.stats.put(stat, tMod.stats.get(stat));
			}
		}
		for (var key : tMod.elements.keySet()) {
			if (this.elements.containsKey(key)) {
				this.elements.put(key, this.elements.get(key) + tMod.elements.get(key));
			} else {
				this.elements.put(key, tMod.elements.get(key));
			}
		}
	}

	public void subtract(TechniqueModifier tMod) {
		for (var stat : tMod.stats.keySet()) {
			if (this.stats.containsKey(stat)) {
				this.stats.put(stat, this.stats.get(stat).add(tMod.stats.get(stat)));
			} else {
				this.stats.put(stat, new BigDecimal("0").subtract(tMod.stats.get(stat)));
			}
		}
		for (var key : tMod.elements.keySet()) {
			if (this.elements.containsKey(key)) {
				this.elements.put(key, this.elements.get(key) - tMod.elements.get(key));
			} else {
				this.elements.put(key, -tMod.elements.get(key));
			}
		}
	}

	public CompoundTag serialize() {
		var tag = new CompoundTag();
		var playerStatsTag = new ListTag();
		var elementsTag = new ListTag();
		for (var stat : this.stats.keySet()) {
			var statTag = new CompoundTag();
			statTag.putString("stat-name", stat.name());
			statTag.putString("stat-value", this.stats.get(stat).toPlainString());
			playerStatsTag.add(statTag);
		}
		for (var element : this.elements.keySet()) {
			var elementTag = new CompoundTag();
			elementTag.putString("element-name", element.toString());
			elementTag.putDouble("element-value", this.elements.get(element));
			elementsTag.add(elementTag);
		}
		tag.put("stats-list", playerStatsTag);
		tag.put("elements-list", elementsTag);
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		ListTag playerStatsTag = (ListTag) tag.get("stats-list");
		ListTag elementsTag = (ListTag) tag.get("elements-list");
		if (playerStatsTag != null) {
			for (var rawStatTag : playerStatsTag) {
				if(rawStatTag instanceof CompoundTag statTag) {
					PlayerStat stat = PlayerStat.valueOf(statTag.getString("stat-name"));
					BigDecimal value = new BigDecimal(statTag.getString("stat-value"));
					this.stats.put(stat, value);
				}
			}
		}
		if(elementsTag != null) {
			for(var rawElementTag : elementsTag) {
				if(rawElementTag instanceof CompoundTag elementTag) {
					ResourceLocation element = new ResourceLocation(elementTag.getString("element-name"));
					double value = elementTag.getDouble("element-value");
					this.elements.put(element, value);
				}
			}
		}
	}

}
