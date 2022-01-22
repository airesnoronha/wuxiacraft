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
	 * super important, if don't have this just kill yourself
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
	 * Turn the grid with aspects and deserialize this
	 */
	public TechniqueModifier compile() {
		if (!grid.get(startNodePoint).equals(WuxiaTechniqueAspects.START.getId())) return null;
		HashSet<Point> visited = new HashSet<>();
		//breadth first search
		LinkedList<Point> toVisit = new LinkedList<>();
		toVisit.add(startNodePoint);
		final ResourceLocation emptyId = WuxiaTechniqueAspects.EMPTY.getId();

		var tMod = new TechniqueModifier();
		while(!toVisit.isEmpty()){ // layer wide iteration
			var visiting = toVisit.removeFirst();
			if(visited.contains(visiting)) continue;
			var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(visiting, emptyId));
			if (aspect == null) continue;
			var junkNotExpected = new HashSet<Point>();
			this.grid.get(visiting);
			// TODO Custom logic node
			tMod.add(aspect.modifier);
			//
			visited.add(visiting);
			for (var neighbour : hexagonalNeighbours) {
				Point visitingNeighbour = new Point(visiting.x + neighbour.x, visiting.y + neighbour.y);
				var neighbourAspect = this.grid.getOrDefault(visitingNeighbour, emptyId);
				if (visited.contains(visitingNeighbour)) continue;
				if (aspect.expectedAspects.contains(neighbourAspect)) {
					toVisit.add(visitingNeighbour);
				} else if (neighbourAspect == emptyId) {
					visited.add(visitingNeighbour);
				} else {
					junkNotExpected.add(visitingNeighbour);
				}
			}
			for (var point : junkNotExpected) {
				var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(point, emptyId));
				if (junkAspect == null) continue;
				//TODO Logic if found junk
				tMod.subtract(junkAspect.modifier);
				visited.add(point);
			}
		}
		for(var key : this.grid.keySet()) {
			if(!visited.contains(key)) {
				var junkAspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(this.grid.getOrDefault(key, emptyId));
				if (junkAspect == null) continue;
				//TODO Logic if disconnected
				tMod.subtract(junkAspect.modifier);
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
