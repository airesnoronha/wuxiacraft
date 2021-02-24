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
    }

    public Skill activate(ISkillAction action) {
        this.action = action;
        return this;
    }

    public Skill casting(ISkillAction action) {
        this.whenCasting = action;
        return this;
    }

    public void activate(LivingEntity actor) {
        this.action.activate(actor);
    }

    public void casting(LivingEntity actor) {
        this.whenCasting.activate(actor);
    }
}
