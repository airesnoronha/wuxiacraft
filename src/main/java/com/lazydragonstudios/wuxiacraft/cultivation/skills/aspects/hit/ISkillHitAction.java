package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;

import java.util.LinkedList;

public interface ISkillHitAction {

	boolean hit(Player caster, LinkedList<SkillAspect> aspectChain, HitResult hitResult);

}
