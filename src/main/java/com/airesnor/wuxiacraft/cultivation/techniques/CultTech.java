package com.airesnor.wuxiacraft.cultivation.techniques;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class CultTech implements ICultTech {

	public static final String ARMOR_MOD = "wuxiacraft.technique.armor";
	public static final String ATTACK_SPEED_MOD = "wuxiacraft.technique.attack_speed";
	public static final String MAX_HEALTH_MOD = "wuxiacraft.technique.max_health";
	public static final String SPEED__MOD = "wuxiacraft.technique.speed";
	public static final String STRENGTH_MOD = "wuxiacraft.technique.strength";

	private List<KnownTechnique> knownTechniques;

	public CultTech() {
		this.knownTechniques = new ArrayList<>();
	}

	@Override
	public boolean addTechnique(Technique technique, double progress) {
		boolean contains = false;
		boolean elementalReverse = false;
		for (KnownTechnique t : getKnownTechniques()) {
			if (t.getTechnique() == technique) {
				contains = true;
			}
			for (Element e : t.getTechnique().getElements()) {
				for (Element n : technique.getElements()) {
					if (e.isCounter(n) || n.isCounter(e))
						elementalReverse = true;
				}
			}
		}
		if (!contains && !elementalReverse) {
			this.knownTechniques.add(new KnownTechnique(technique, progress));
			return true;
		}
		return false;
	}

	@Override
	public void remTechnique(Technique technique) {
		for (KnownTechnique t : this.knownTechniques) {
			if (t.getTechnique().equals(technique)) {
				this.knownTechniques.remove(t);
				break;
			}
		}
	}

	@Override
	public void updateTechniques(EntityPlayer player, ICultivation cultivation) {
		List<TechniquesModifiers> mods = new ArrayList<>();
		for (KnownTechnique t : this.knownTechniques) {
			mods.add(t.onUpdate(player, cultivation));
		}
		if (!player.world.isRemote) {
			double armor = 0, attackSpeed = 0, maxHealth = 0, movementSpeed = 0, strength = 0;
			double narmor = 0, nattackSpeed = 0, nmaxHealth = 0, nmovementSpeed = 0, nstrength = 0;
			for (TechniquesModifiers tm : mods) {
				//WuxiaCraft.logger.info(String.format("%.3f, %.3f, %.3f, %.3f, %.3f", tm.armor, tm.attackSpeed, tm.maxHealth, tm.movementSpeed, tm.strength));
				if (tm.armor > 0) armor = Math.max(armor, tm.armor);
				else if (tm.armor < 0) narmor = Math.min(narmor, tm.armor);
				if (tm.attackSpeed > 0) attackSpeed = Math.max(attackSpeed, tm.attackSpeed);
				else if (tm.attackSpeed < 0) nattackSpeed = Math.min(nattackSpeed, tm.attackSpeed);
				if (tm.maxHealth > 0) maxHealth = Math.max(maxHealth, tm.maxHealth);
				else if (tm.maxHealth < 0) nmaxHealth = Math.min(nmaxHealth, tm.maxHealth);
				if (tm.movementSpeed > 0) movementSpeed = Math.max(movementSpeed, tm.movementSpeed);
				else if (tm.movementSpeed < 0) nmovementSpeed = Math.min(nmovementSpeed, tm.movementSpeed);
				if (tm.strength > 0) strength = Math.max(strength, tm.strength);
				else if (tm.strength < 0) nstrength = Math.min(nstrength, tm.strength);
			}

			//remove any previous armor modifiers
			for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ARMOR).getModifiers()) {
				if (mod.getName().equals(ARMOR_MOD)) {
					player.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(mod);
				}
			}

			//remove any previous attack speed modifiers
			for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getModifiers()) {
				if (mod.getName().equals(ATTACK_SPEED_MOD)) {
					player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(mod);
				}
			}

			//remove any previous max health modifiers
			for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifiers()) {
				if (mod.getName().equals(MAX_HEALTH_MOD)) {
					player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(mod);
				}
			}

			//remove any previous speed modifiers
			for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers()) {
				if (mod.getName().equals(SPEED__MOD)) {
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(mod);
				}
			}

			//remove any previous strength modifiers
			for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifiers()) {
				if (mod.getName().equals(STRENGTH_MOD)) {
					player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(mod);
				}
			}

			AttributeModifier armor_mod = new AttributeModifier(ARMOR_MOD, (armor + narmor) * 2, 0);
			AttributeModifier attack_speed_mod = new AttributeModifier(ATTACK_SPEED_MOD, attackSpeed + nattackSpeed, 0);
			AttributeModifier max_health_mod = new AttributeModifier(MAX_HEALTH_MOD, (maxHealth + nmaxHealth) * 3, 0);
			double speedValue = (movementSpeed + nmovementSpeed) * 0.1f * (cultivation.getSpeedHandicap() / 100f);
			if(cultivation.getMaxSpeed() >= 0) {
				if(cultivation.getSpeedIncrease()* SharedMonsterAttributes.MOVEMENT_SPEED.getDefaultValue() * 0.2f < cultivation.getMaxSpeed()) {
					speedValue = Math.min(cultivation.getMaxSpeed()*(float)SharedMonsterAttributes.MOVEMENT_SPEED.getDefaultValue(), speedValue);
				} else {
					speedValue = 0;
				}
			}
			AttributeModifier speed_mod = new AttributeModifier(SPEED__MOD, speedValue, 0);
			AttributeModifier strength_mod = new AttributeModifier(STRENGTH_MOD, strength + nstrength, 0);

			player.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(armor_mod);
			player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).applyModifier(attack_speed_mod);
			player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(max_health_mod);
			player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speed_mod);
			player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(strength_mod);

			//WuxiaCraft.logger.info(String.format("final %.3f, %.3f",  speed_mod.getAmount(), player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
		}
	}

	@Override
	public void progress(double amount) {
		for (KnownTechnique t : this.knownTechniques) {
			t.progress(amount);
		}
	}

	@Override
	public List<KnownTechnique> getKnownTechniques() {
		return this.knownTechniques;
	}

	@Override
	public TechniquesModifiers getOverallModifiers() {
		double armor = 0, nArmor = 0;
		double attackSpped = 0, nAttackSpeed = 0;
		double maxHealth = 0, nMaxHealth = 0;
		double speed = 0, nSpeed = 0;
		double strength = 0, nStrength = 0;
		for (KnownTechnique t : this.getKnownTechniques()) {
			if (t.getTechnique().getBaseModifiers().armor > 0)
				armor = Math.max(armor, t.getTechnique().getBaseModifiers().armor);
			else nArmor = Math.min(nArmor, t.getTechnique().getBaseModifiers().armor);
			if (t.getTechnique().getBaseModifiers().attackSpeed > 0)
				attackSpped = Math.max(attackSpped, t.getTechnique().getBaseModifiers().attackSpeed);
			else nAttackSpeed = Math.min(nAttackSpeed, t.getTechnique().getBaseModifiers().attackSpeed);
			if (t.getTechnique().getBaseModifiers().maxHealth > 0)
				maxHealth = Math.max(maxHealth, t.getTechnique().getBaseModifiers().maxHealth);
			else nMaxHealth = Math.min(nMaxHealth, t.getTechnique().getBaseModifiers().maxHealth);
			if (t.getTechnique().getBaseModifiers().movementSpeed > 0)
				speed = Math.max(speed, t.getTechnique().getBaseModifiers().movementSpeed);
			else nSpeed = Math.min(nSpeed, t.getTechnique().getBaseModifiers().movementSpeed);
			if (t.getTechnique().getBaseModifiers().strength > 0)
				strength = Math.max(strength, t.getTechnique().getBaseModifiers().strength);
			else nStrength = Math.min(nStrength, t.getTechnique().getBaseModifiers().strength);
		}
		return new TechniquesModifiers(armor + nArmor, attackSpped + nAttackSpeed, maxHealth + nMaxHealth, speed + nSpeed, strength + nStrength);
	}

	@Override
	public List<Skill> getTechniqueSkills() {
		List<Skill> knownSkills = new ArrayList<>();
		if (this.knownTechniques.size() > 0) {
			knownSkills.add(Skills.CULTIVATE);
		}
		for (KnownTechnique kt : getKnownTechniques()) {
			Technique t = kt.getTechnique();
			if (kt.getProgress() >= t.getTier().smallProgress) {
				for (Skill skill : t.getSmallCompletionSkills()) {
					if (!knownSkills.contains(skill)) {
						knownSkills.add(skill);
					}
				}
			}
			if (kt.getProgress() >= t.getTier().smallProgress + t.getTier().greatProgress) {
				for (Skill skill : t.getGreatCompletionSkills()) {
					if (!knownSkills.contains(skill)) {
						knownSkills.add(skill);
					}
				}
			}
			if (kt.getProgress() >= t.getTier().smallProgress + t.getTier().greatProgress + t.getTier().perfectionProgress) {
				for (Skill skill : t.getPerfectionCompletionSkills()) {
					if (!knownSkills.contains(skill)) {
						knownSkills.add(skill);
					}
				}
			}
		}
		return knownSkills;
	}

	@Override
	public float getOverallCultivationSpeed() {
		int mortals = 0;
		float fromMortal = 0;
		int martials = 0;
		float fromMartial = 0;
		int immortals = 0;
		float fromImmortal = 0;
		int divines = 0;
		float fromDivine = 0;
		for (KnownTechnique kt : getKnownTechniques()) {
			switch (kt.getTechnique().getTier()) {
				case MORTAL:
					mortals++;
					fromMortal += kt.getTechnique().getCultivationSpeed();
					break;
				case MARTIAL:
					martials++;
					fromMartial += kt.getTechnique().getCultivationSpeed();
					break;
				case IMMORTAL:
					immortals++;
					fromImmortal += kt.getTechnique().getCultivationSpeed();
					break;
				case DIVINE:
					divines++;
					fromDivine += kt.getTechnique().getCultivationSpeed();
					break;
			}
		}
		float divider = 1 + mortals + martials * 5 + immortals * 25 + divines * 125;
		return (1 + fromMortal + fromMartial * 5 + fromImmortal * 25 + fromDivine * 125) / divider;
	}
}
