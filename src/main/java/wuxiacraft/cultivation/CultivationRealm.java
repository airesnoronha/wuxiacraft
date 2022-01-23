package wuxiacraft.cultivation;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashSet;

public class CultivationRealm extends ForgeRegistryEntry<CultivationRealm> {

	/**
	 * the Name of this realm
	 */
	public final String name;

	/**
	 * The cultivation system this realm belongs to
	 */
	public final System system;

	/**
	 * A reference to the stages this realm has
	 */
	public final HashSet<ResourceLocation> stages;

	public final ResourceLocation firstStage;

	@Nullable
	public final ResourceLocation nextRealm;

	public CultivationRealm(String name, System system, ResourceLocation firstStage, @Nullable ResourceLocation nextRealm) {
		this.name = name;
		this.system = system;
		this.firstStage = firstStage;
		this.nextRealm = nextRealm;
		this.stages = new HashSet<>();
	}

	public CultivationRealm addStage(ResourceLocation stage) {
		this.stages.add(stage);
		return this;
	}
}
