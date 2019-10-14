package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivateSkillMessage implements IMessage {

    public int SkillIndex;

    public ActivateSkillMessage(Skill skill) {
        this.SkillIndex = Skills.SKILLS.indexOf(skill);
    }

    public ActivateSkillMessage () {
        this.SkillIndex = -1;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.SkillIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.SkillIndex);
    }
}
