package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SkillCapMessage implements IMessage {

	public final ISkillCap skillCap;
	public boolean shouldUpdateCPaCD;
	public UUID senderUUID;

	public SkillCapMessage() {
		this.skillCap = new SkillCap();
		this.shouldUpdateCPaCD = false;
		this.senderUUID = null;
	}

	public SkillCapMessage(ISkillCap skillCap, boolean shouldUpdateCPaCD, UUID senderUUID) {
		this.skillCap = skillCap;
		this.shouldUpdateCPaCD = shouldUpdateCPaCD;
		this.senderUUID = senderUUID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
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
		this.senderUUID = packetBuffer.readUniqueId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packetBuffer = new PacketBuffer(buf);
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
		packetBuffer.writeUniqueId(this.senderUUID);
	}
}
