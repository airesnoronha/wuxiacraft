package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects;

import net.minecraft.world.entity.player.Player;

import java.util.LinkedList;

public interface ISkillHitAction {

	boolean hit(Player player, LinkedList<SkillAspect> aspectChain);

}
