package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaCheckpointsWidget;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabelBox;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
public abstract class TechniqueAspect extends ForgeRegistryEntry<TechniqueAspect> {

	/**
	 * an empty checkpoint for being the default one
	 */
	public static Checkpoint NO_CHECKPOINT = new Checkpoint("none", BigDecimal.ZERO, (c) -> {});

	//Class itself
	public final String name;

	public final ResourceLocation textureLocation;

	public final LinkedList<Checkpoint> checkpoints;

	public String description;

	public TechniqueAspect(String name, ResourceLocation textureLocation) {
		this.name = name;
		this.textureLocation = textureLocation;
		this.checkpoints = new LinkedList<>();
		this.checkpoints.add(NO_CHECKPOINT);
		this.description = "";
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
	public TechniqueAspect addCheckpoint(Checkpoint checkpoint) {
		this.checkpoints.add(checkpoint);
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
			if (proficiency.compareTo(checkpoint.proficiencyRequired) > 0) {
				currentSelected = checkpoint;
			}
		}
		return currentSelected;
	}

	/**
	 * Logic if aspect was expected
	 *
	 * @param metaData the current modifiers when accepting this
	 */
	public void accept(HashMap<String, Object> metaData) {
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

	public record Checkpoint(String name, BigDecimal proficiencyRequired, Consumer<ICultivation> onReached) {
	}

}
