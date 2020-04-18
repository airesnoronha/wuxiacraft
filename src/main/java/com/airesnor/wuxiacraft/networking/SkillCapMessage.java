package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SkillCapMessage implements IMessage {

	public ISkillCap skillCap;
	public boolean shouldUpdateCPaCD;
	public String sender;

	public SkillCapMessage() {
		this.skillCap = new SkillCap();
		this.shouldUpdateCPaCD = false;
		this.sender = "";
	}

	public SkillCapMessage(ISkillCap skillCap, boolean shouldUpdateCPaCD, String sender) {
		this.skillCap = skillCap;
		this.shouldUpdateCPaCD = shouldUpdateCPaCD;
		this.sender = sender;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.shouldUpdateCPaCD = buf.readBoolean();
		this.skillCap.stepCooldown(buf.readFloat());
		this.skillCap.stepCastProgress(buf.readFloat());
		int length = buf.readInt();
		for (int i = 0; i < length; i++) {
			Skill skill = Skills.SKILLS.get(buf.readInt());
			skillCap.addSkill(skill);
		}
		length = buf.readInt();
		for (int i = 0; i < length; i++) {
			skillCap.addSelectedSkill(buf.readInt());
		}
		skillCap.setActiveSkill(buf.readInt());
		length = buf.readInt();
		byte[] bytes = new byte[length];
		buf.readBytes(bytes, 0, length);
		this.sender = new String(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.shouldUpdateCPaCD);
		buf.writeFloat(this.skillCap.getCooldown());
		buf.writeFloat(this.skillCap.getCastProgress());
		buf.writeInt(this.skillCap.getKnownSkills().size());
		for (Skill skill : skillCap.getKnownSkills()) {
			int skillId = Skills.SKILLS.indexOf(skill);
			buf.writeInt(skillId);
		}
		buf.writeInt(this.skillCap.getSelectedSkills().size());
		for (int skill : skillCap.getSelectedSkills()) {
			buf.writeInt(skill);
		}
		buf.writeInt(skillCap.getActiveSkill());
		buf.writeInt(this.sender.getBytes().length);
		buf.writeBytes(this.sender.getBytes());
	}
}
