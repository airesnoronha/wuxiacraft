package wuxiacraft.cultivation.technique;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class AspectContainer {

	HashMap<ResourceLocation, Double> aspectAndProficiency;

	public CompoundTag Serialize() {
		var tag = new CompoundTag();
		ListTag listTag = (ListTag) tag.put("aspect-list", new ListTag());
		for(var aspect : this.aspectAndProficiency.keySet()) {
			var aspectTag = new CompoundTag();
			aspectTag.putString("aspect-name", aspect.toString());
			aspectTag.putDouble("aspect-proficiency", aspectAndProficiency.get(aspect));
		}
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		var listTag = (ListTag)tag.get("aspect-list");
		if(listTag == null) return;
		for(var itemTag : listTag) {
			if(!(itemTag instanceof CompoundTag aspectTag)) continue;
			var location = aspectTag.getString("aspect-name");
			var proficiency = aspectTag.getDouble("aspect-proficiency");
			this.aspectAndProficiency.put(new ResourceLocation(location), proficiency);
		}
	}

}
