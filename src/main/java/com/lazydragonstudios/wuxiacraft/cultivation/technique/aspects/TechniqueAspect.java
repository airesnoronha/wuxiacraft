package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaCheckpointsWidget;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabelBox;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
public abstract class TechniqueAspect extends ForgeRegistryEntry<TechniqueAspect> {

	/**
	 * an empty checkpoint for being the default one
	 */
	public final static Checkpoint NO_CHECKPOINT = new Checkpoint("none", BigDecimal.ZERO, (c) -> {
	});

	//Class itself
	public final String name;

	public final ResourceLocation textureLocation;

	public final LinkedList<Checkpoint> checkpoints;

	public String description;

	public Predicate<AspectContainer> canLearn;

	public TechniqueAspect(String name, ResourceLocation textureLocation) {
		this.name = name;
		this.textureLocation = textureLocation;
		this.checkpoints = new LinkedList<>();
		this.checkpoints.add(NO_CHECKPOINT);
		this.description = "";
		this.canLearn = c -> true;
	}

	/**
	 * @return How many connections forward can make, -1 for infinite
	 */
	public int canConnectToCount() {
		return 1;
	}

	/**
	 * @return How many connections backwards can make, -1 for infinite
	 */
	public int canConnectFromCount() {
		return 1;
	}

	/**
	 * This is meant to sort the priority of connected aspects to be connected to
	 * Pay attention that this is a forward connection
	 * Probably sometimes you might want more of the same connected before transforming
	 *
	 * @param aspect1 the first aspect
	 * @param aspect2 the second aspect
	 * @return -1, 0, 1 like a comparator
	 */
	public int connectPrioritySorter(TechniqueAspect aspect1, TechniqueAspect aspect2) {
		return 0;
	}

	/**
	 * Sets the description of this technique aspect to display in game
	 * Probably the bot is going to use this desc as well
	 *
	 * @param desc the new description to be set
	 * @return this
	 */
	public TechniqueAspect setDescription(String desc) {
		this.description = desc;
		return this;
	}

	/**
	 * adds a checkpoint to this aspect
	 *
	 * @param checkpoint the checkpoint to be added
	 * @return this
	 */
	public TechniqueAspect addCheckpoint(@Nonnull Checkpoint checkpoint) {
		this.checkpoints.add(checkpoint);
		return this;
	}

	/**
	 * Changes the behavior to seek whether this aspect can be learned by a player
	 *
	 * @param canLearn the new behavior
	 * @return this
	 */
	public TechniqueAspect setCanLearn(@Nonnull Predicate<AspectContainer> canLearn) {
		this.canLearn = canLearn;
		return this;
	}

	/**
	 * gets the current checkpoint for the specified proficiency
	 *
	 * @param proficiency the proficiency of this aspect
	 * @return the current checkpoint
	 */
	public Checkpoint getCurrentCheckpoint(BigDecimal proficiency) {
		Checkpoint currentSelected = NO_CHECKPOINT;
		for (var checkpoint : this.checkpoints) {
			if (proficiency.compareTo(checkpoint.proficiencyRequired) < 0) break;
			currentSelected = checkpoint;
		}
		return currentSelected;
	}

	/**
	 * Logic if aspect was expected
	 *
	 * @param metaData the current modifiers when accepting this
	 */
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		var skills = this.getSkills(proficiency);
		if(skills.size() > 0) {
			if(!metaData.containsKey("skills")) metaData.put("skills", new HashSet<ResourceLocation>());
			//noinspection unchecked
			var skillSet = (HashSet<ResourceLocation>) metaData.get("skills");
			skillSet.addAll(skills);
		}
	}

	/**
	 * Logic if aspect is junk, or not expected
	 *
	 * @param metaData the current modifiers when rejecting this
	 */
	public void reject(HashMap<String, Object> metaData) {
	}

	/**
	 * Logic if aspect is disconnected from start node
	 *
	 * @param metaData the current modifiers when this is disconnected
	 */
	public void disconnect(HashMap<String, Object> metaData) {
	}

	/**
	 * this is meant to be overridden
	 * Whether the can connect to neighbour aspect
	 *
	 * @param aspect aspect to be connected to
	 * @return true if it can be connected
	 */
	public boolean canConnect(TechniqueAspect aspect) {
		return true;
	}

	public HashSet<ResourceLocation> getSkills(BigDecimal proficiency) {
		var skills = new HashSet<ResourceLocation>();
		for (var checkpoint : this.checkpoints) {
			if (proficiency.compareTo(checkpoint.proficiencyRequired) < 0) break;
			skills.addAll(checkpoint.skills);
		}
		return skills;
	}

	/**
	 * Prepares a bunch of widgets to be used in the descriptor page
	 *
	 * @param aspectRegistryName The key of this aspect in the registry
	 * @return widgets describing this aspect
	 */
	@Nonnull
	public LinkedList<AbstractWidget> getStatsSheetDescriptor(ResourceLocation aspectRegistryName) {
		WuxiaLabel nameLabel = new WuxiaLabel(5, 2, new TextComponent(this.name), 0xFFAA00);
		WuxiaCheckpointsWidget checkpointsWidget = new WuxiaCheckpointsWidget(5, 12, 190, aspectRegistryName);
		WuxiaLabelBox descriptionLabel = new WuxiaLabelBox(5, 25, 190, new TextComponent("Description: " + this.description));
		LinkedList<AbstractWidget> widgets = new LinkedList<>();
		widgets.add(nameLabel);
		widgets.add(checkpointsWidget);
		widgets.add(descriptionLabel);
		return widgets;
	}

	/**
	 * This is the checkpoints for this technique. Take care that when using onReached, make sure you don't add repeated entries
	 * On reached is going to be called a lot, use {@link AspectContainer#learnAspect(ResourceLocation)} that it's safe
	 */
	public record Checkpoint(String name, BigDecimal proficiencyRequired, Consumer<AspectContainer> onReached,
													 HashSet<ResourceLocation> skills) {
		public Checkpoint(String name, BigDecimal proficiencyRequired, Consumer<AspectContainer> onReached) {
			this(name, proficiencyRequired, onReached, new HashSet<>());
		}

		public Checkpoint addSkill(ResourceLocation skill) {
			this.skills.add(skill);
			return this;
		}
	}

}
