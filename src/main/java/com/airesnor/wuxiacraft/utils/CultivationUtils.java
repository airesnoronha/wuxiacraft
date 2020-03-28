package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.entities.mobs.EntityCultivator;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class CultivationUtils {

	@Nonnull
	public static ICultivation getCultivationFromEntity(EntityLivingBase entityIn) {
		ICultivation cultivation = null;
		if (entityIn instanceof EntityPlayer) {
			cultivation = entityIn.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		} else if (entityIn instanceof EntityCultivator) {
			cultivation = ((EntityCultivator) entityIn).getCultivation();
		}
		if (cultivation == null) {
			cultivation = new Cultivation();
		}
		return cultivation;
	}

	@Nonnull
	public static ICultTech getCultTechFromEntity(EntityLivingBase entityIn) {
		ICultTech cultTech = null;
		if (entityIn instanceof EntityPlayer) {
			cultTech = entityIn.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		}
		if (cultTech == null) {
			cultTech = new CultTech();
		}
		return cultTech;
	}

	@Nonnull
	public static ISkillCap getSkillCapFromEntity(EntityLivingBase entityIn) {
		ISkillCap skillCap = null;
		if (entityIn instanceof EntityPlayer) {
			skillCap = entityIn.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		} else if (entityIn instanceof EntityCultivator) {
			skillCap = ((EntityCultivator) entityIn).getSkillCap();
		}
		if (skillCap == null) {
			skillCap = new SkillCap();
		}
		return skillCap;
	}

	public static void cultivatorAddProgress(EntityLivingBase player, ICultivation cultivation, float amount) {
		ICultTech cultTech = getCultTechFromEntity(player);
		amount *= cultTech.getOverallCultivationSpeed();
		cultTech.progress(amount);
		if (!cultivation.getSuppress()) {
			if (cultivation.addProgress(amount)) {
				if (!player.world.isRemote) {
					if (player instanceof EntityPlayer) {
						int msgN = player.world.rand.nextInt(CONGRATS_MESSAGE_COUNT);
						((EntityPlayer) player).sendStatusMessage(new TextComponentString(TranslateUtils.translateKey("wuxiacraft.level_message.congrats_" + msgN) + " " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel())), false);
						NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int) cultivation.getCurrentProgress(), (int) cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), (EntityPlayerMP) player);
					}
				}
			}
		}
	}

	private static final int CONGRATS_MESSAGE_COUNT = 5;

}