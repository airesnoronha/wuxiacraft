package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class KeyHandler {
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyPress(InputEvent.KeyInputEvent event) {
		KeyBinding[] keyBindings = ClientProxy.keyBindings;

		if(keyBindings[0].isPressed()) {
			WuxiaCraftConfig.speedHandicap = Math.min(100, WuxiaCraftConfig.speedHandicap + 5);
			WuxiaCraftConfig.syncFromFields();
			NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap));
		}
		if(keyBindings[1].isPressed()) {
			WuxiaCraftConfig.speedHandicap = Math.max(0, WuxiaCraftConfig.speedHandicap - 5);
			WuxiaCraftConfig.syncFromFields();
			NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap));
		}
		if(keyBindings[2].isPressed()) {
			BlockPos pos = Minecraft.getMinecraft().player.getPosition();
			Minecraft.getMinecraft().player.openGui(WuxiaCraft.instance, GuiHandler.CULTIVATION_GUI_ID,Minecraft.getMinecraft().player.world,pos.getX(), pos.getY(), pos.getZ());
			//NetworkWrapper.INSTANCE.sendToServer(new RequestCultGuiMessage(true));
		}
		if(keyBindings[3].isPressed()) {
			ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			skillCap.stepCastProgress(1f);
			if(skillCap.getActiveSkill() != -1) {
				Skill skill = skillCap.getSelectedSkills().get(skillCap.getActiveSkill());
				NetworkWrapper.INSTANCE.sendToServer(new ActivateSkillMessage(skill, skillCap.getCastProgress()));
				Minecraft.getMinecraft().addScheduledTask(() -> {
						activateSkill(skillCap, skill, Minecraft.getMinecraft().player);
				});
			}
		}
		if(Keyboard.getEventKey() == keyBindings[3].getKeyCode() && !Keyboard.getEventKeyState()) {
			ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			skillCap.resetCastProgress();
			NetworkWrapper.INSTANCE.sendToServer(new SkillCapMessage(skillCap));
		}
		if(keyBindings[4].isPressed()) {
			BlockPos pos = Minecraft.getMinecraft().player.getPosition();
			Minecraft.getMinecraft().player.openGui(WuxiaCraft.instance, GuiHandler.SKILLS_GUI_ID, Minecraft.getMinecraft().player.world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public static void activateSkill(ISkillCap skillCap, Skill skill, EntityPlayer actor) {
		WuxiaCraft.logger.info("Cast Progress:" + skillCap.getCastProgress());
		WuxiaCraft.logger.info("Cooldown:" + skillCap.getCooldown());
		WuxiaCraft.logger.info("Selected skill:" + skill.getName());
		if(skillCap.getCastProgress() >= skill.getCastTime() && skillCap.getCooldown() <= 0) {
			WuxiaCraft.logger.info("Casting skill: " + skill.getName());
			skill.activate(actor);
			skillCap.resetCastProgress();
			skillCap.stepCooldown(skill.getCooldown());
		}
	}

}
