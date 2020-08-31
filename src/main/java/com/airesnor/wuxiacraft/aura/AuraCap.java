package com.airesnor.wuxiacraft.aura;

import net.minecraft.util.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class AuraCap implements IAuraCap {

	private final List<ResourceLocation> auraLocations;

	public AuraCap() {
		this.auraLocations = new LinkedList<>();
	}

	@Override
	public List<ResourceLocation> getAuraLocations() {
		return this.auraLocations;
	}

	@Override
	public void addAuraLocation(ResourceLocation location) {
		this.auraLocations.add(location);
	}

	@Override
	public void remAuraLocation(ResourceLocation location) {
		this.auraLocations.remove(location);
	}

	@Override
	public void clearAuraLocations() {
		this.auraLocations.clear();
	}

	@Override
	public List<Aura> getAuraInstances() {
		List<Aura> auras = new LinkedList<>();
		for(ResourceLocation location : this.auraLocations) {
			if(Auras.AURAS.containsKey(location)) {
				auras.add(Auras.AURAS.get(location));
			}
		}
		return auras;
	}

	@Override
	public void copyFrom(IAuraCap auraCap) {
		this.clearAuraLocations();
		for(ResourceLocation location : auraCap.getAuraLocations()) {
			if(Auras.AURAS.containsKey(location)) {
				this.auraLocations.add(location);
			}
		}
	}
}
