package com.lazydragonstudios.wuxiacraft.cultivation.technique;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.lazydragonstudios.wuxiacraft.init.WuxiaTechniqueAspects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TechniqueGrid {

	private final HashMap<Point, ResourceLocation> grid;
	private final HashMap<Point, BigDecimal> proficiencies;

	/**
	 * super important, if you don't have this just kill yourself
	 */
	private Point startNodePoint = null;

	public static Point[] hexagonalNeighbours = new Point[]{new Point(1, 0), //right
			new Point(0, 1), //bottom right
			new Point(-1, 1), //bottom left
			new Point(-1, 0), //left
			new Point(0, -1), //top left
			new Point(1, -1) //top right
	};

	/**
	 * Adds a new aspect to the grid
	 *
	 * @param p      the aspect position in the grid
	 * @param aspect the aspect to be added
	 * @return true if added
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean addGridNode(Point p, ResourceLocation aspect, BigDecimal proficiency) {
		ResourceLocation id = WuxiaTechniqueAspects.START.getId();
		if (aspect.equals(id)) {
			if (startNodePoint == null) startNodePoint = p;
			else return false;
		}
		grid.put(p, aspect);
		proficiencies.put(p, proficiency);
		return true;
	}

	public void removeGridNode(Point p) {
		ResourceLocation id = WuxiaTechniqueAspects.START.getId();
		var aspect = this.grid.get(p);
		if (aspect != null && aspect.equals(id)) {
			this.startNodePoint = null;
		}
		this.grid.remove(p);
	}

	/**
	 * This is a depth first priority processing in the grid for accepted aspects
	 *
	 * @param visiting         the current location in the grid we are accepting
	 * @param processHierarchy the order in which we are accepting aspects
	 * @param metaData         the data passed between nodes
	 */
	private void processAspects(Point visiting, HashMap<Point, HashSet<Point>> processHierarchy, HashMap<String, Object> metaData) {
		var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.get(visiting));
		var proficiency = this.proficiencies.getOrDefault(visiting, BigDecimal.ZERO);
		if (aspect == null) return;
		aspect.accept(metaData, proficiency);
		for (var nextPoint : processHierarchy.get(visiting)) {
			processAspects(nextPoint, processHierarchy, metaData);
		}
	}

	/**
	 * Gets a copy of the actual grid so it won't be able to be modified
	 *
	 * @return a copy of this aspects grid
	 */
	public HashMap<Point, ResourceLocation> getGrid() {
		//noinspection unchecked
		return (HashMap<Point, ResourceLocation>) this.grid.clone();
	}

	/**
	 * Turn the grid with aspects and deserialize this
	 */
	@Nonnull
	public TechniqueModifier compile() {
		if (this.startNodePoint == null) return new TechniqueModifier();
		if (!grid.get(startNodePoint).equals(WuxiaTechniqueAspects.START.getId())) return new TechniqueModifier();
		final ResourceLocation emptyId = WuxiaTechniqueAspects.EMPTY.getId();

		var processHierarchy = new HashMap<Point, HashSet<Point>>();

		// data to be passed between nodes
		HashMap<String, Object> metaData = new HashMap<>();

		traverseGridFromStart(this,
				(node) -> processHierarchy.put(node, new HashSet<>()),
				(node, neighbour) -> processHierarchy.get(node).add(neighbour), (junk, neighbour) -> {
					var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(junk, emptyId));
					if (junkAspect == null) return;
					junkAspect.reject(metaData);
				}, (disconnected) -> {
					var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(disconnected, emptyId));
					if (junkAspect == null) return;
					junkAspect.disconnect(metaData);
				});
		processAspects(startNodePoint, processHierarchy, metaData);
		return getModifiersFromMetaData(metaData);
	}

	/**
	 * this method will traverse the grid
	 * this is a way to make all traversals the same
	 * So we know that rendering and compiling are doing the same thing
	 * And possibly elsewhere as well
	 */
	public static void traverseGridFromStart(TechniqueGrid grid, Consumer<Point> visit, BiConsumer<Point, Point> onConnect, BiConsumer<Point, Point> onJunked, Consumer<Point> onDisconnected) {
		if (grid.startNodePoint == null) return;
		if (!grid.grid.get(grid.startNodePoint).equals(WuxiaTechniqueAspects.START.getId())) return;
		final ResourceLocation emptyId = WuxiaTechniqueAspects.EMPTY.getId();
		//breadth first search
		LinkedList<Point> toVisit = new LinkedList<>();
		HashSet<Point> visited = new HashSet<>();
		HashMap<Point, HashSet<Point>> connectedFrom = new HashMap<>();
		HashMap<Point, HashSet<Point>> connectedTo = new HashMap<>();
		toVisit.add(grid.startNodePoint);
		var junkNotExpected = new HashMap<Point, Point>();
		while (!toVisit.isEmpty()) { // layer wide iteration aka Breadth first
			var visiting = toVisit.removeFirst();
			if (visited.contains(visiting)) continue;
			var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(grid.grid.getOrDefault(visiting, emptyId));
			if (aspect == null) continue;
			grid.grid.get(visiting);
			visited.add(visiting);
			junkNotExpected.remove(visiting);
			visit.accept(visiting);
			var connectCandidates = new LinkedList<Point>();
			connectedTo.put(visiting, new HashSet<>());
			for (var neighbour : hexagonalNeighbours) {
				Point visitingNeighbour = new Point(visiting.x + neighbour.x, visiting.y + neighbour.y);
				var neighbourAspect = grid.grid.getOrDefault(visitingNeighbour, emptyId);
				if (visited.contains(visitingNeighbour)) continue;
				connectedFrom.putIfAbsent(visitingNeighbour, new HashSet<>());
				TechniqueAspect neighbourTechAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(neighbourAspect);
				if (neighbourTechAspect == null) continue;
				if (aspect.canConnect(neighbourTechAspect)) {
					connectCandidates.add(visitingNeighbour);
				} else if (neighbourAspect == emptyId) {
					visited.add(visitingNeighbour);
				} else {
					junkNotExpected.put(visitingNeighbour, visiting);
				}
			}
			connectCandidates.sort((p1, p2) -> {
				TechniqueAspect aspect1 = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(grid.grid.getOrDefault(p1, emptyId));
				TechniqueAspect aspect2 = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(grid.grid.getOrDefault(p2, emptyId));
				return aspect.connectPrioritySorter(aspect1, aspect2);
			});
			for (var candidate : connectCandidates) {
				var candidateLocation = grid.grid.getOrDefault(candidate, emptyId);
				TechniqueAspect candidateTechAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(candidateLocation);
				if (candidateTechAspect == null) continue;
				int cFrom = connectedFrom.get(candidate).size();
				int cTo = connectedTo.get(visiting).size();
				int allowedFrom = candidateTechAspect.canConnectFromCount();
				int allowedTo = aspect.canConnectToCount();
				if ((allowedFrom == -1 || cFrom < allowedFrom) && (allowedTo == -1 || cTo < allowedTo)) {
					connectedFrom.get(candidate).add(visiting);
					connectedTo.get(visiting).add(candidate);
					toVisit.add(candidate);
					onConnect.accept(visiting, candidate);
				} else if (cTo >= allowedTo && allowedTo != -1) {
					break;
				}
			}
		}
		for (var point : junkNotExpected.keySet()) {
			var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(grid.grid.getOrDefault(point, emptyId));
			if (junkAspect == null) continue;
			onJunked.accept(junkNotExpected.get(point), point);
			visited.add(point);
		}
		for (var key : grid.grid.keySet()) {
			if (!visited.contains(key)) {
				var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(grid.grid.getOrDefault(key, emptyId));
				if (junkAspect == null) continue;
				onDisconnected.accept(key);
			}
		}
	}

	/**
	 * Turns the technique metadata into a technique modifier
	 *
	 * @param metaData the metadata to be interpreted
	 * @return the modifier for this technique
	 */
	private static TechniqueModifier getModifiersFromMetaData(HashMap<String, Object> metaData) {
		TechniqueModifier tMod = new TechniqueModifier();
		for (var stat : PlayerStat.values()) {
			if (metaData.containsKey("stat-" + stat.name().toLowerCase())) {
				tMod.stats.put(stat, (BigDecimal) metaData.get("stat-" + stat.name().toLowerCase()));
			}
		}
		for (var system : System.values()) {
			for (var stat : PlayerSystemStat.values()) {
				String statName = system.name().toLowerCase() + "-stat-" + stat.name().toLowerCase();
				if (metaData.containsKey(statName)) {
					tMod.systemStats.get(system).put(stat, (BigDecimal) metaData.get(statName));
					if (stat == PlayerSystemStat.CULTIVATION_SPEED) {
						tMod.setValidTechnique(true);
					}
				}
			}
		}
		for (var element : WuxiaRegistries.ELEMENTS.getValues()) {
			if (metaData.containsKey("element-" + element.getName())) {
				tMod.elements.put(element.getRegistryName(), (Double) metaData.get("element-" + element.getName()));
			}
		}
		if (metaData.containsKey("skills")) {
			tMod.skills.clear();
			//noinspection unchecked
			var skillSet = (HashSet<ResourceLocation>) metaData.get("skills");
			tMod.skills.addAll(skillSet);
		}
		return tMod;
	}

	@Nonnull
	public ResourceLocation getAspectAtGrid(Point p) {
		final ResourceLocation emptyId = WuxiaTechniqueAspects.EMPTY.getId();
		return this.grid.getOrDefault(p, emptyId);
	}

	public Point getStartNodePoint() {
		return startNodePoint;
	}

	public TechniqueGrid() {
		grid = new HashMap<>();
		proficiencies = new HashMap<>();
	}

	public CompoundTag serialize() {
		var tag = new CompoundTag();
		var listTag = new ListTag();
		for (var key : this.grid.keySet()) {
			var keyValueTag = new CompoundTag();
			keyValueTag.putInt("keyX", key.x);
			keyValueTag.putInt("keyY", key.y);
			keyValueTag.putString("value", this.grid.get(key).toString());
			keyValueTag.putString("proficiency", this.proficiencies.getOrDefault(key, BigDecimal.ZERO).toPlainString());
			listTag.add(keyValueTag);
		}
		tag.put("grid", listTag);
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		startNodePoint = null;
		var listTag = (ListTag) tag.get("grid");
		if (listTag == null) return;
		this.grid.clear();
		for (var keyValueTag : listTag) {
			if (!(keyValueTag instanceof CompoundTag cTag)) continue;
			var x = cTag.getInt("keyX");
			var y = cTag.getInt("keyY");
			var value = cTag.getString("value");
			var proficiency = new BigDecimal(cTag.getString("proficiency"));
			this.addGridNode(new Point(x, y), new ResourceLocation(value), proficiency);
		}
	}

	public TechniqueGrid copy() {
		TechniqueGrid techniqueGrid = new TechniqueGrid();
		techniqueGrid.deserialize(this.serialize());
		return techniqueGrid;
	}

	public void fixProficiencies(AspectContainer container) {
		for (var p : this.grid.keySet()) {
			var proficiency = container.getAspectProficiency(this.grid.get(p));
			this.proficiencies.put(p, proficiency);
		}
	}
}
