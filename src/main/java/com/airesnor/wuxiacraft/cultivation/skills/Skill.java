package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.cultivation.elements.Element;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class Skill {

    private String name;

    private float cost;
    private float progress;

    private float castTime;
    private float cooldown;

    private ISkillAction action;
    private ISkillAction whenCasting;

    private String authorName;

    public String getName() {
        return I18n.format("wuxiacraft.skills." + this.name);
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

    public float getCastTime() {
        return castTime;
    }

    public float getCooldown() {
        return cooldown;
    }

    public boolean activate(EntityPlayer actor) {
        return this.action.activate(actor);
    }

    public boolean castingEffect(EntityPlayer actor) {
        return this.whenCasting.activate(actor);
    }

    public float getCost() {
        return this.cost;
    }

    public float getProgress() {
        return this.progress;
    }

    public Skill(String name, float cost, float progress) {
        this(name, cost, progress, 0,0);
    }

    public Skill(String name, float cost, float progress, float castTime, float cooldown) {
        this(name, cost, progress, castTime, cooldown, "Aires Adures");
    }

    public Skill(String name, float cost, float progress, float castTime, float cooldown, String author) {
        this.name = name;
        this.cost = cost;
        this.progress = progress;
        this.castTime = castTime;
        this.cooldown = cooldown;
        this.whenCasting = new ISkillAction() {
            @Override
            public boolean activate(EntityPlayer actor) {
                return false;
            }
        };
        this.authorName = author;
    }

}
