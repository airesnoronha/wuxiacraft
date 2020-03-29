package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.utils.TranslateUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;

public class Skill {

	private String name;

	private float cost;
	private float progress;

	private float castTime;
	private float cooldown;

	private ISkillAction action;
	private ISkillAction whenCasting;

	private String authorName;

	boolean aggressive;

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
		this(name, aggressive, cost, progress, 0, 0);
	}

	public Skill(String name, boolean aggressive, float cost, float progress, float castTime, float cooldown) {
		this(name, aggressive, cost, progress, castTime, cooldown, "Aires Adures");
	}

	public Skill(String name, boolean aggressive, float cost, float progress, float castTime, float cooldown, String author) {
		this.name = name;
		this.aggressive = aggressive;
		this.cost = cost;
		this.progress = progress;
		this.castTime = castTime;
		this.cooldown = cooldown;
		this.whenCasting = new ISkillAction() {
			@Override
			public boolean activate(EntityLivingBase actor) {
				return false;
			}
		};
		this.authorName = author;
	}

}
