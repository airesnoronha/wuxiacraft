package wuxiacraft.cultivation.skill;

import net.minecraft.entity.LivingEntity;
import wuxiacraft.init.WuxiaSkills;

public class Skill {

    private ISkillAction action;
    private ISkillAction whenCasting;

    public final String name;
    public final double energyCost;
    public final double castTime; //in
    public final double coolDown; //in ticks
	public final boolean castInTicks;

    public Skill(String name, double energyCost, double castTime, double coolDown, boolean castInTicks) {
        this.name = name;
        this.energyCost = energyCost;
        this.castTime = castTime;
        this.coolDown = coolDown;
        this.castInTicks = castInTicks;
        WuxiaSkills.SKILLS.add(this);
        this.action = actor -> true;
        this.whenCasting = actor -> true;
    }

    public Skill setAction(ISkillAction action) {
        this.action = action;
        return this;
    }

    public Skill setCasting(ISkillAction action) {
        this.whenCasting = action;
        return this;
    }

    public boolean activate(LivingEntity actor) {
        return this.action.activate(actor);
    }

    public boolean casting(LivingEntity actor) {
        return this.whenCasting.activate(actor);
    }
}
