package wuxiacraft.cultivation.technique;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.technique.aspects.TechniqueAspect;
import wuxiacraft.init.WuxiaRegistries;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

public class AspectContainer {

	private final HashMap<ResourceLocation, BigDecimal> aspectAndProficiency = new HashMap<>();

	private int knownAspectCount = 0;

	public BigDecimal getAspectProficiency(ResourceLocation aspect) {
		return aspectAndProficiency.getOrDefault(aspect, BigDecimal.ZERO);
	}

	public void countKnownAspects() {
		this.knownAspectCount = 0;
		for (var aspect : this.aspectAndProficiency.keySet()) {
			if (this.knowsAspect(aspect)) {
				this.knownAspectCount += 1;
			} else {
				this.aspectAndProficiency.remove(aspect);
			}
		}
	}

	public Set<ResourceLocation> getKnownAspects() {
		return this.aspectAndProficiency.keySet();
	}

	public int getKnownAspectsCount() {
		return this.knownAspectCount;
	}

	/**
	 * Probably empty aspects might be added because of poor manipulation
	 * so let's add rule that it needs to be bigger than 10
	 *
	 * @param aspect the id of the aspect to be known
	 * @return true if this aspect is known by the player
	 */
	public boolean knowsAspect(ResourceLocation aspect) {
		return this.aspectAndProficiency.getOrDefault(aspect, BigDecimal.ZERO).compareTo(BigDecimal.TEN) >= 0;
	}

	public void addAspectProficiency(ICultivation cultivation, ResourceLocation aspect, BigDecimal amount) {
		if (this.aspectAndProficiency.containsKey(aspect)) {
			TechniqueAspect aspectInstance = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(aspect);
			if (aspectInstance == null) return;
			TechniqueAspect.Checkpoint before = aspectInstance.getCurrentCheckpoint(this.aspectAndProficiency.get(aspect));
			this.aspectAndProficiency.put(aspect, this.aspectAndProficiency.get(aspect).add(amount).max(BigDecimal.ZERO));
			TechniqueAspect.Checkpoint after = aspectInstance.getCurrentCheckpoint(this.aspectAndProficiency.get(aspect));
			if (!before.equals(after)) {
				after.onReached().accept(cultivation);
			}
		} else {
			this.aspectAndProficiency.put(aspect, amount);
		}
		countKnownAspects();
	}

	public void setAspectAndProficiency(ResourceLocation aspect, BigDecimal amount) {
		this.aspectAndProficiency.put(aspect, amount.max(BigDecimal.ZERO));
		countKnownAspects();
	}

	public void subtractAspectProficiency(ResourceLocation aspect, BigDecimal amount) {
		if (this.aspectAndProficiency.containsKey(aspect)) {
			this.aspectAndProficiency.put(aspect, this.aspectAndProficiency.get(aspect).subtract(amount).max(BigDecimal.ZERO));
		} else {
			this.aspectAndProficiency.put(aspect, amount.multiply(new BigDecimal("-1")).max(BigDecimal.ZERO));
		}
		countKnownAspects();
	}

	public CompoundTag serialize() {
		var tag = new CompoundTag();
		ListTag listTag = (ListTag) tag.put("aspect-list", new ListTag());
		for (var aspect : this.aspectAndProficiency.keySet()) {
			var aspectTag = new CompoundTag();
			aspectTag.putString("aspect-name", aspect.toString());
			aspectTag.putString("aspect-proficiency", aspectAndProficiency.get(aspect).toPlainString());
		}
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		var listTag = (ListTag) tag.get("aspect-list");
		if (listTag == null) return;
		for (var itemTag : listTag) {
			if (!(itemTag instanceof CompoundTag aspectTag)) continue;
			var location = aspectTag.getString("aspect-name");
			var proficiency = aspectTag.getString("aspect-proficiency");
			this.aspectAndProficiency.put(new ResourceLocation(location), new BigDecimal(proficiency));
		}
	}

}
