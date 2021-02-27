package wuxiacraft.client;

import wuxiacraft.cultivation.skill.Skill;

import java.util.ArrayList;

/**
 * since most of the skill values are client side, i thought it won't be good to be transporting the everywhere
 * so i decided to store them on client side
 */
public class SkillValues {

	public static boolean isCastingSkill = false;

	public static double castProgress = 0;

	public static int activeSkill = 0;

}
