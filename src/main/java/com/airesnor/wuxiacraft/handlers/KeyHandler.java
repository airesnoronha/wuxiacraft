package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
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
		if (keyBindings[3].isPressed() && Keyboard.getEventKeyState()) {
			ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			skillCap.setCasting(true);
			NetworkWrapper.INSTANCE.sendToServer(new CastSkillMessage(true));
		}
		if (Keyboard.getEventKey() == keyBindings[3].getKeyCode() && !Keyboard.getEventKeyState()) {
			ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			skillCap.setCasting(false);
			skillCap.setDoneCasting(true);
			NetworkWrapper.INSTANCE.sendToServer(new CastSkillMessage(false));
		}
		if(keyBindings[4].isPressed()) {
			BlockPos pos = Minecraft.getMinecraft().player.getPosition();
			Minecraft.getMinecraft().player.openGui(WuxiaCraft.instance, GuiHandler.SKILLS_GUI_ID, Minecraft.getMinecraft().player.world, pos.getX(), pos.getY(), pos.getZ());
		}
		if(keyBindings[5].isPressed()) {
			ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			int next = skillCap.getActiveSkill() + 1;
			NetworkWrapper.INSTANCE.sendToServer(new SelectSkillMessage(next));
			SelectSkillMessageHandler.selectSkill(skillCap, next);
		}
		if(keyBindings[6].isPressed()) {
			ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			int next = skillCap.getActiveSkill() - 1;
			NetworkWrapper.INSTANCE.sendToServer(new SelectSkillMessage(next));
			SelectSkillMessageHandler.selectSkill(skillCap, next);
		}
		for(int i = 0; i < 10; i ++) {
			if(keyBindings[7+i].isPressed()) {
				ISkillCap skillCap = Minecraft.getMinecraft().player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
				int next = i;
				NetworkWrapper.INSTANCE.sendToServer(new SelectSkillMessage(next));
				SelectSkillMessageHandler.selectSkill(skillCap, next);
			}
		}
	}

}