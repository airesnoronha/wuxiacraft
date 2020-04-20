package com.airesnor.wuxiacraft.entities.mobs;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class EntityCultivator extends EntityCreature implements IEntityAdditionalSpawnData {

	private static final String MOB_ARMOR_MOD_NAME = "wuxiacraft.mob.armor";
	private static final String MOB_HEALTH_MOD_NAME = "wuxiacraft.mob.max_health";
	private static final String MOB_DAMAGE_MOD_NAME = "wuxiacraft.mob.attack_damage";
	private static final String MOB_M_SPEED_MOD_NAME = "wuxiacraft.mob.movement_speed";
	private static final String MOB_A_SPEED_MOD_NAME = "wuxiacraft.mob.attack_speed";

	protected final ICultivation cultivation;
	protected final ISkillCap skillCap;

	public EntityCultivator(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		cultivation = new Cultivation();
		skillCap = new SkillCap();
		applyCultivation(worldIn);
		applyCultivationBonuses();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("level", cultivation.getCurrentLevel().levelName);
		compound.setInteger("subLevel", cultivation.getCurrentSubLevel());
		compound.setInteger("progress", (int) cultivation.getCurrentProgress());
		compound.setInteger("energy", (int) cultivation.getEnergy());
		compound.setInteger("pelletCD", cultivation.getPillCooldown());
		compound.setInteger("length", skillCap.getKnownSkills().size());
		for (int i = 0; i < skillCap.getKnownSkills().size(); i++) {
			int pos = Skills.SKILLS.indexOf(skillCap.getKnownSkills().get(i));
			compound.setInteger("skill-" + i, pos);
		}
		compound.setFloat("cooldown", skillCap.getCooldown());
		compound.setFloat("castProgress", skillCap.getCastProgress());
		compound.setInteger("ks-length", skillCap.getSelectedSkills().size());
		for (int i = 0; i < skillCap.getSelectedSkills().size(); i++) {
			compound.setInteger("ks-" + i, skillCap.getSelectedSkills().get(i));
		}
		compound.setInteger("active", skillCap.getActiveSkill());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		cultivation.setCurrentLevel(CultivationLevel.REGISTERED_LEVELS.get(compound.getString("level")));
		cultivation.setCurrentSubLevel(compound.getInteger("subLevel"));
		cultivation.setProgress(compound.getInteger("progress"));
		cultivation.addEnergy(compound.getInteger("energy"));
		cultivation.setPillCooldown(compound.getInteger("pelletCD"));
		int length = compound.getInteger("length");
		skillCap.getKnownSkills().clear();
		for (int i = 0; i < length; i++) {
			int skillId = compound.getInteger("skill-" + i);
			Skill skill = Skills.SKILLS.get(skillId);
			skillCap.addSkill(skill);
		}
		skillCap.stepCastProgress(compound.getInteger("castProgress"));
		skillCap.stepCooldown(compound.getInteger("cooldown"));
		length = compound.getInteger("ks-length");
		skillCap.getSelectedSkills().clear();
		for (int i = 0; i < length; i++) {
			skillCap.addSelectedSkill(compound.getInteger("ks-" + i));
		}
		skillCap.setActiveSkill(compound.getInteger("active"));
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean attackEntityAsMob(Entity entityIn) {
		float damageValue = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int knockBack = 0;

		if (entityIn instanceof EntityLivingBase)
		{
			damageValue += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
			knockBack += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean attacked = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), damageValue);

		if (attacked)
		{
			if (knockBack > 0)
			{
				((EntityLivingBase)entityIn).knockBack(this, (float)knockBack * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int fireMod = EnchantmentHelper.getFireAspectModifier(this);

			if (fireMod > 0)
			{
				entityIn.setFire(fireMod * 4);
			}

			if (entityIn instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemStack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemStack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemStack1, entityplayer, this) && itemStack1.getItem().isShield(itemStack1, entityplayer))
				{
					float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1)
					{
						entityplayer.getCooldownTracker().setCooldown(itemStack1.getItem(), 100);
						this.world.setEntityState(entityplayer, (byte)30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return attacked;
	}

	public boolean hasSkills() {
		return !this.skillCap.getKnownSkills().isEmpty();
	}

	@SuppressWarnings("unused")
	public List<Skill> getSkillList() {
		return this.skillCap.getKnownSkills();
	}

	public ISkillCap getSkillCap() {
		return skillCap;
	}

	public ICultivation getCultivation() {
		return cultivation;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		this.cultivation.addEnergy(cultivation.getMaxEnergy()*0.0005f);
		if (skillCap.getCooldown() >= 0) {
			skillCap.stepCooldown(-1f);
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		byte[] bytes = this.getCultivationLevel().levelName.getBytes();
		buffer.writeInt(this.cultivation.getCurrentSubLevel());
		buffer.writeDouble(this.cultivation.getCurrentProgress());
		buffer.writeDouble(this.cultivation.getEnergy());
		buffer.writeInt(this.cultivation.getPillCooldown());
		buffer.writeInt(bytes.length);
		buffer.writeBytes(bytes);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		int subLevel = additionalData.readInt();
		double progress = additionalData.readDouble();
		double energy = additionalData.readDouble();
		int pelletCooldown = additionalData.readInt();
		int length = additionalData.readInt();
		byte[] bytes = new byte[30];
		additionalData.readBytes(bytes, 0, length);
		bytes[length] = '\0';
		String cultLevelName = new String(bytes, 0, length);
		CultivationLevel level = CultivationLevel.REGISTERED_LEVELS.get(cultLevelName);
		this.cultivation.setCurrentLevel(level);
		this.cultivation.setCurrentSubLevel(subLevel);
		this.cultivation.setProgress(progress);
		this.cultivation.setEnergy(energy);
		this.cultivation.setPillCooldown(pelletCooldown);
	}



	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0D);
	}

	protected void applyCultivationBonuses() {
		double strengthMod = cultivation.getStrengthIncrease()-1f;
		double speedMod = cultivation.getSpeedIncrease()-1f;

		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)
				.applyModifier(new AttributeModifier(MOB_HEALTH_MOD_NAME, strengthMod*3f, 0));
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR)
				.applyModifier(new AttributeModifier(MOB_ARMOR_MOD_NAME, strengthMod*0.7f, 0));
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED)
				.applyModifier(new AttributeModifier(MOB_M_SPEED_MOD_NAME, speedMod*0.2f, 0));
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED)
				.applyModifier(new AttributeModifier(MOB_A_SPEED_MOD_NAME, speedMod, 1));
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE)
				.applyModifier(new AttributeModifier(MOB_DAMAGE_MOD_NAME, strengthMod, 0));
		this.heal(100000f);
	}

	public CultivationLevel getCultivationLevel() {
		return this.cultivation.getCurrentLevel();
	}

	public int getCultivationSubLevel() {
		return this.cultivation.getCurrentSubLevel();
	}

	protected abstract void applyCultivation(World worldIn);
}
