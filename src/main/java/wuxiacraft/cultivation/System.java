package wuxiacraft.cultivation;

import net.minecraft.resources.ResourceLocation;
import wuxiacraft.WuxiaCraft;

public enum System {
	BODY(new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_realm"), new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_stage")),
	DIVINE(new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_realm"), new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_stage")),
	ESSENCE(new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_realm"), new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_stage"));

	public final ResourceLocation defaultRealm;
	public final ResourceLocation defaultStage;

	System(ResourceLocation defaultRealm, ResourceLocation defaultStage) {
		this.defaultRealm = defaultRealm;
		this.defaultStage = defaultStage;
	}
}
