package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivateSkillMessage implements IMessage {

    public int SkillIndex;

    public float castProgress;

    public ActivateSkillMessage(Skill skill, float castProgress) {
        this.SkillIndex = Skills.SKILLS.indexOf(skill);
        this.castProgress = castProgress;
    }

    public ActivateSkillMessage () {
        this.SkillIndex = -1;
        this.castProgress = 0;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.SkillIndex = buf.readInt();
        this.castProgress = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.SkillIndex);
        buf.writeFloat(this.castProgress);
    }
}
