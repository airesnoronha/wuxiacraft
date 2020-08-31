package com.airesnor.wuxiacraft.aura;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public interface IAuraCap {

	List<ResourceLocation> getAuraLocations();

	void addAuraLocation(ResourceLocation location);

	void remAuraLocation(ResourceLocation location);

	void clearAuraLocations();

	List<Aura> getAuraInstances();

	void copyFrom(IAuraCap auraCap);

}
