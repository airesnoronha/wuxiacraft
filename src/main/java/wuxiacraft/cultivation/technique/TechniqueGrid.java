package wuxiacraft.cultivation.technique;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.init.WuxiaRegistries;
import wuxiacraft.init.WuxiaTechniqueAspects;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TechniqueGrid {

	private final HashMap<Point, ResourceLocation> grid;

	/**
	 * super important, if you don't have this just kill yourself
	 */
	private Point startNodePoint = null;

	public static Point[] hexagonalNeighbours = new Point[]{
			new Point(1, 0), //right
			new Point(0, 1), //bottom right
			new Point(-1, 1), //bottom left
			new Point(-1, 0), //left
			new Point(0, -1), //top left
			new Point(1, -1) //top right
	};

	/**
	 * Adds a new aspect to the grid
	 * @param p the aspect position in the grid
	 * @param aspect the aspect to be added
	 * @return true if added
	 */
	@SuppressWarnings("UnusedReturnValue")
	public boolean addGridNode(Point p, ResourceLocation aspect) {
		ResourceLocation id = WuxiaTechniqueAspects.START.getId();
		if (aspect.equals(id)) {
			if (startNodePoint == null)
				startNodePoint = p;
			else
				return false;
		}
		grid.put(p, aspect);
		return true;
	}

	/**
	 * This is a depth first priority processing in the grid for accepted aspects
	 * @param visiting the current location in the grid we are accepting
	 * @param processHierarchy the order in which we are accepting aspects
	 * @param metaData the data passed between nodes
	 */
	public void processAspects(Point visiting, HashMap<Point, HashSet<Point>> processHierarchy, HashMap<String, Object> metaData) {
		var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.get(visiting));
		if(aspect == null) return;
		aspect.accept(metaData);
		for(var nextPoint : processHierarchy.get(visiting)) {
			processAspects(nextPoint, processHierarchy, metaData);
		}
	}

	/**
	 * Turn the grid with aspects and deserialize this
	 */
	public TechniqueModifier compile() {
		if (!grid.get(startNodePoint).equals(WuxiaTechniqueAspects.START.getId())) return null;
		HashSet<Point> visited = new HashSet<>();
		//breadth first search
		LinkedList<Point> toVisit = new LinkedList<>();
		toVisit.add(startNodePoint);
		final ResourceLocation emptyId = WuxiaTechniqueAspects.EMPTY.getId();

		var processHierarchy = new HashMap<Point, HashSet<Point>>();

		// data to be passed between nodes
		HashMap<String, Object> metaData = new HashMap<>();

		var junkNotExpected = new HashSet<Point>();

		while(!toVisit.isEmpty()){ // layer wide iteration aka Breadth first
			var visiting = toVisit.removeFirst();
			if(visited.contains(visiting)) continue;
			var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(visiting, emptyId));
			if (aspect == null) continue;
			this.grid.get(visiting);
			processHierarchy.put(visiting, new HashSet<>());
			visited.add(visiting);
			for (var neighbour : hexagonalNeighbours) {
				Point visitingNeighbour = new Point(visiting.x + neighbour.x, visiting.y + neighbour.y);
				var neighbourAspect = this.grid.getOrDefault(visitingNeighbour, emptyId);
				if (visited.contains(visitingNeighbour)) continue;
				if (aspect.canConnect(WuxiaRegistries.TECHNIQUE_ASPECT.getValue(neighbourAspect))) {
					toVisit.add(visitingNeighbour);
					processHierarchy.get(visiting).add(visitingNeighbour);
					junkNotExpected.remove(visitingNeighbour);
				} else if (neighbourAspect == emptyId) {
					visited.add(visitingNeighbour);
				} else {
					junkNotExpected.add(visitingNeighbour);
				}
			}
		}
		processAspects(startNodePoint, processHierarchy, metaData);
		for (var point : junkNotExpected) {
			var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(point, emptyId));
			if (junkAspect == null) continue;
			junkAspect.reject(metaData);
			visited.add(point);
		}
		for(var key : this.grid.keySet()) {
			if(!visited.contains(key)) {
				var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(key, emptyId));
				if (junkAspect == null) continue;
				junkAspect.disconnect(metaData);
			}
		}
		return getModifiersFromMetaData(metaData);
	}

	/**
	 * Turns the technique metadata into a technique modifier
	 * @param metaData the metadata to be interpreted
	 * @return the modifier for this technique
	 */
	private static TechniqueModifier getModifiersFromMetaData(HashMap<String, Object> metaData) {
		TechniqueModifier tMod = new TechniqueModifier();
		if(metaData.containsKey("cultivation_speed")) {
			tMod.cultivation_speed = (double) metaData.get("cultivation_speed");
		}
		if(metaData.containsKey("strength")) {
			tMod.strength = (double) metaData.get("strength");
		}
		if(metaData.containsKey("agility")) {
			tMod.agility = (double) metaData.get("agility");
		}
		if(metaData.containsKey("health")) {
			tMod.health = (double) metaData.get("health");
		}
		if(metaData.containsKey("energy")) {
			tMod.energy = (double) metaData.get("energy");
		}
		if(metaData.containsKey("energyRegen")) {
			tMod.energyRegen = (double) metaData.get("energyRegen");
		}
		for(var element : WuxiaRegistries.ELEMENTS.getValues()) {
			if(metaData.containsKey("element-"+element.getName())) {
				tMod.elements.put(element.getRegistryName(), (Double) metaData.get("element-"+element.getName()));
			}
		}
		return tMod;
	}

	public TechniqueGrid() {
		grid = new HashMap<>();
	}

	public CompoundTag serialize() {
		var tag =  new CompoundTag();
		var listTag = new ListTag();
		for(var key : this.grid.keySet()) {
			var keyValueTag = new CompoundTag();
			keyValueTag.putInt("keyX", key.x);
			keyValueTag.putInt("keyY", key.y);
			keyValueTag.putString("value", this.grid.get(key).toString());
			listTag.add(keyValueTag);
		}
		tag.put("grid", listTag);
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		startNodePoint = null;
		var listTag = (ListTag)tag.get("grid");
		if(listTag == null) return;
		for(var keyValueTag : listTag) {
			if(!(keyValueTag instanceof CompoundTag cTag)) continue;
			var x = cTag.getInt("keyX");
			var y = cTag.getInt("keyY");
			var value = cTag.getString("value");
			this.addGridNode(new Point(x,y), new ResourceLocation(value));
		}
	}

}
