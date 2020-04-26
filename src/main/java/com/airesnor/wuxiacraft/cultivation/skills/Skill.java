package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.entity.EntityLivingBase;

public class Skill {

	private final String name;

	private final float cost;
	private final float progress;

	private final float castTime;
	private final float cooldown;

	private ISkillAction action;
	private ISkillAction whenCasting;

	private final String authorName;

	public final boolean aggressive;

	public final boolean castNotSpeedable;

	public String getName() {
		return TranslateUtils.translateKey("wuxiacraft.skills." + this.name);
	}

	public String getUName() {
		return this.name;
	}

	public Skill setAction(ISkillAction action) {
		this.action = action;
		return this;
	}

	public Skill setWhenCasting(ISkillAction action) {
		this.whenCasting = action;
		return this;
	}

	public boolean isAggressive() {
		return this.aggressive;
	}

	public float getCastTime() {
		return castTime;
	}

	public float getCooldown() {
		return cooldown;
	}

	public boolean activate(EntityLivingBase actor) {
		return this.action.activate(actor);
	}

	public boolean castingEffect(EntityLivingBase actor) {
		return this.whenCasting.activate(actor);
	}

	public float getCost() {
		return this.cost;
	}

	public float getProgress() {
		return this.progress;
	}

	public Skill(String name, boolean aggressive, float cost, float progress) {
		this(name, false, aggressive, cost, progress, 0, 0);
	}

	public Skill(String name, boolean castNotSpeedable, boolean aggressive, float cost, float progress, float castTime, float cooldown) {
		this(name, castNotSpeedable, aggressive, cost, progress, castTime, cooldown, "Aires Adures");
	}

	public Skill(String name, boolean castNotSpeedable, boolean aggressive, float cost, float progress, float castTime, float cooldown, String author) {
		this.name = name;
		this.castNotSpeedable = castNotSpeedable;
		this.aggressive = aggressive;
		this.cost = cost;
		this.progress = progress;
		this.castTime = castTime;
		this.cooldown = cooldown;
		this.whenCasting = actor -> true;
		this.authorName = author;
	}

}
