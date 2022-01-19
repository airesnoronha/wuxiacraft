package wuxiacraft.init;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import org.apache.commons.lang3.tuple.Pair;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.skill.ISkillAction;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.network.ActivateActionMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class WuxiaSkills {

	public static final LinkedList<Skill> SKILLS = new LinkedList<>();

	public static final LinkedList<Pair<String, ISkillAction>> ACTIONS = new LinkedList<>();

	public static final Skill THUNDER_FIST_CRASH = new Skill("thunder_fist_crash", 7.0f, 30f, 0f, false)
			.setAction(actor -> {
				if (actor.world.isRemote) {
					RayTraceResult result = Minecraft.getInstance().objectMouseOver;
					if (result != null && result.getType() == RayTraceResult.Type.ENTITY) {
						LivingEntity target = (LivingEntity) ((EntityRayTraceResult) result).getEntity();
						WuxiaPacketHandler.INSTANCE.sendToServer(new ActivateActionMessage("thunder_fist_crash_server", WuxiaSkills.THUNDER_FIST_CRASH.energyCost, target.getUniqueID()));
						actor.swingArm(Hand.MAIN_HAND);
						return true;
					}
				}
				return false;
			});

	static {
		ACTIONS.add(Pair.of("thunder_fist_crash_server", actor -> {
			LivingEntity target = ActivateActionMessage.targeted;
			if (target != null) {
				ICultivation cultivation = Cultivation.get(actor);
				float damage = (float) (cultivation.getBodyModifier() + cultivation.getFinalModifiers().strength) * 2;
				target.attackEntityFrom(WuxiaElements.getSourceByElement(WuxiaElements.PHYSICAL), damage);
				actor.world.playSound(null, actor.getPosition(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, MathUtils.clamp(damage, 10, 200), 300f);
			}
			return true;
		}));
	}

	public static final Skill MURDEROUS_FIRE_DOMAIN = new Skill("murderous_fire_domain", 135.0f, 30f, 0f, false);

	@Nullable
	public static Skill getSkillByName(String name) {
		for (Skill skill : SKILLS) {
			if (skill.name.equals(name)) {
				return skill;
			}
		}
		return null;
	}

	@Nullable
	public static ISkillAction getActionByName(String name) {
		for (Pair<String, ISkillAction> pair : ACTIONS) {
			if (pair.getKey().equals(name)) {
				return pair.getValue();
			}
		}
		return null;
	}
}
