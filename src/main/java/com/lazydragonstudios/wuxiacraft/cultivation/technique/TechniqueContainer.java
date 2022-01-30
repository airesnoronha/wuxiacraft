package com.lazydragonstudios.wuxiacraft.cultivation.technique;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.nbt.CompoundTag;

public class TechniqueContainer {

	public TechniqueGrid grid = new TechniqueGrid();

	public TechniqueModifier modifier = new TechniqueModifier();

	public System system;

	public TechniqueContainer(System system) {
		this.system = system;
	}

	public CompoundTag serialize() {
		var tag = new CompoundTag();
		tag.put("grid", this.grid.serialize());
		tag.put("modifier", this.modifier.serialize());
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		CompoundTag grid = (CompoundTag) tag.get("grid");
		if(grid != null) {
			this.grid.deserialize( grid);
		}
		CompoundTag modifier =(CompoundTag) tag.get("modifier");
		if(modifier != null) {
			this.modifier.deserialize( modifier);
		}
	}
}
