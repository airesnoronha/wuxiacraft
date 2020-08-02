package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.dimensions.biomes.WuxiaBiomes;
import com.airesnor.wuxiacraft.networking.ActivatePartialSkillMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.ProgressMessage;
import com.airesnor.wuxiacraft.networking.SpawnParticleMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;

import static com.airesnor.wuxiacraft.cultivation.skills.Skills.ENLIGHTENMENT;

public class SkillCultivate extends Skill {

	private static long LastUseCultivateMillis = 0;
	public static boolean particleStep = false;

	public SkillCultivate(String name, Cultivation.System system) {
		super(name, true, false, 1f, 0, 300f, 0f);
		setAction(actor -> {
			if (!actor.world.isRemote) {
				int bound = 100;
				int amplifier = 0;
				PotionEffect effect = actor.getActivePotionEffect(ENLIGHTENMENT);
				if (effect != null) {
					bound = 300;
					amplifier = 1;
					if (effect.getAmplifier() == 1) {
						bound = 800;
						amplifier = 2;
					}
				}
				if (actor.getRNG().nextInt(bound) == 0) {
					effect = new PotionEffect(ENLIGHTENMENT, 20 * 60 * (3 - amplifier), amplifier, true, true);
					actor.addPotionEffect(effect);
				}
			}
			if (actor.getEntityWorld().getBiome(new BlockPos(actor.getPosition().getX(), actor.getPosition().getY(), actor.getPosition().getZ())) == WuxiaBiomes.EXTREMEQI) {
				CultivationUtils.cultivatorAddProgress(actor, system, 0.002f, true, true);
			} else {
				CultivationUtils.cultivatorAddProgress(actor, system, 0.001f, true, true);
			}
			return true;
		});
		setWhenCasting(actor -> {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
			ICultTech cultTech = CultivationUtils.getCultTechFromEntity(actor);
			double amount = cultTech.getOverallCultivationSpeed();
			float energy = cultTech.getOverallCultivationSpeed() * 1.25f * 10;
			boolean hasEnergy = cultivation.hasEnergy(energy);
			long timeDiff = System.currentTimeMillis() - LastUseCultivateMillis;
			if (timeDiff >= (particleStep ? 500 : 250)) { //4 per second
				for (KnownTechnique kt : cultTech.getKnownTechniques()) {
					int particles = 12;
					for (Element e : kt.getTechnique().getElements()) {
						for (int i = 0; i < particles; i++) {
							float randX = 2 * actor.world.rand.nextFloat() - 1;
							float randY = 2 * actor.world.rand.nextFloat() - 1;
							float randZ = 2 * actor.world.rand.nextFloat() - 1;
							float dist = (float) Math.sqrt(randX * randX + randY * randY + randZ * randZ) * 30f;
							SpawnParticleMessage spm = new SpawnParticleMessage(e.getParticle(), false, actor.posX + randX, actor.posY + 0.9f + randY, actor.posZ + randZ, -randX / dist, -randY / dist, -randZ / dist, 0);
							NetworkWrapper.INSTANCE.sendToServer(spm);
						}
					}
				}
				particleStep = timeDiff < 500;
			}
			if (timeDiff >= 500) { //2 per second
				NetworkWrapper.INSTANCE.sendToServer(new ActivatePartialSkillMessage("applySlowness", cultivation.hasEnergy(energy) ? energy : 0, actor.getUniqueID()));
				if (hasEnergy) {
					if (actor instanceof EntityPlayer) {
						if (actor.getEntityWorld().getBiome(new BlockPos(actor.getPosition().getX(), actor.getPosition().getY(), actor.getPosition().getZ())) == WuxiaBiomes.EXTREMEQI) {
							amount *= 1.5;
						}
						CultivationUtils.cultivatorAddProgress(actor, system, amount, true, false);
						cultivation.remEnergy(energy);
						NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0, system, amount, true, false, actor.getUniqueID()));
					}
				}
				LastUseCultivateMillis = System.currentTimeMillis();
			}
			return hasEnergy;
		});
	}

}