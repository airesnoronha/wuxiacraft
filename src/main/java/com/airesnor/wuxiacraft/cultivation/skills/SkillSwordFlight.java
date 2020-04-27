package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.networking.EnergyMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

public class SkillSwordFlight extends Skill {

	public SkillSwordFlight(String name, float speedMultiplier, float maxSpeed, float cost, float castTime, float cooldown, String author) {
		super(name, true, false, 0, 0, castTime, cooldown, author);
		setAction( actor -> true);
		setWhenCasting(actor -> {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
			IFoundation foundation = CultivationUtils.getFoundationFromEntity(actor);
			skillCap.stepCastProgress(-(float) cultivation.getSpeedIncrease() + 1f);
			if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
				if (cultivation.hasEnergy(cost)) {
					cultivation.remEnergy(cost);
					if ((int) skillCap.getCastProgress() % 5 == 4)
						NetworkWrapper.INSTANCE.sendToServer(new EnergyMessage(1, 9 * 5, actor.getUniqueID()));
					float agility = (float)foundation.getAgilityModifier()* 0.3f;
					float dexterity = (float)foundation.getAgilityModifier() * 0.7f;
					float speed = (agility + dexterity) * speedMultiplier;
					speed = Math.min(maxSpeed, speed);
					float yaw = actor.rotationYawHead;
					float pitch = actor.rotationPitch;
					float x = speed * -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
					float y = speed * -MathHelper.sin((pitch) * 0.017453292F);
					float z = speed * MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
					actor.motionX = x;
					actor.motionY = y;
					actor.motionZ = z;
					actor.fallDistance = 0;
					//swinging
					actor.swingingHand = EnumHand.MAIN_HAND;
					actor.swingProgressInt = 2;
					actor.isSwingInProgress = true;
				}
				return true;
			}
			return false;
		});
	}
}
