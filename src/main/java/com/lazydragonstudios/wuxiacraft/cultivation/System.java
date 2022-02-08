package com.lazydragonstudios.wuxiacraft.cultivation;

import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

public enum System {
	BODY(new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_stage")),
	DIVINE(new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_stage")),
	ESSENCE(new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_stage"));
	public final ResourceLocation defaultStage;

	System(ResourceLocation defaultStage) {
		this.defaultStage = defaultStage;
	}
}
