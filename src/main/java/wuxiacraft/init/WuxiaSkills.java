package wuxiacraft.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.util.SkillUtils;

import java.util.LinkedList;

public class WuxiaSkills {

	public static final LinkedList<Skill> SKILLS = new LinkedList<>();

	public static final Skill THUNDER_FIST_CRASH = new Skill("thunder_fist_thunder", 12.0f, 2f, 0f, false)
			.activate(actor -> {
				Entity result = SkillUtils.rayTraceEntities(actor, 2, 0);
				if (result instanceof LivingEntity) {
					LivingEntity target = (LivingEntity) result;
					ICultivation cultivation = Cultivation.get(actor);
					target.attackEntityFrom(WuxiaElements.getSourceByElement(WuxiaElements.PHYSICAL), (float) (cultivation.getBodyModifier() + cultivation.getFinalModifiers().strength));
					actor.swingArm(Hand.MAIN_HAND);
					return true;
				}
				return false;
			});

	public static Skill getSkillByName(String name) {
		for (Skill skill : SKILLS) {
			if (skill.name.equals(name)) {
				return skill;
			}
		}
		return null;
	}
}
