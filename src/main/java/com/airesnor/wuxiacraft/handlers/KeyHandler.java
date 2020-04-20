package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
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

		if (keyBindings[0].isPressed()) {
			WuxiaCraftConfig.speedHandicap = Math.min(100, WuxiaCraftConfig.speedHandicap + 5);
			WuxiaCraftConfig.syncFromFields();
			WuxiaCraftConfig.syncCultivationFromConfigToClient();
			NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit, Minecraft.getMinecraft().player.getUniqueID()));
		}
		if (keyBindings[1].isPressed()) {
			WuxiaCraftConfig.speedHandicap = Math.max(0, WuxiaCraftConfig.speedHandicap - 5);
			WuxiaCraftConfig.syncFromFields();
			WuxiaCraftConfig.syncCultivationFromConfigToClient();
			NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit, Minecraft.getMinecraft().player.getUniqueID()));
		}
		if (keyBindings[2].isPressed()) {
			BlockPos pos = Minecraft.getMinecraft().player.getPosition();
			Minecraft.getMinecraft().player.openGui(WuxiaCraft.instance, GuiHandler.CULTIVATION_GUI_ID, Minecraft.getMinecraft().player.world, pos.getX(), pos.getY(), pos.getZ());
			//NetworkWrapper.INSTANCE.sendToServer(new RequestCultGuiMessage(true));
		}
		if (Keyboard.getEventKey() == keyBindings[3].getKeyCode() && Keyboard.getEventKeyState()) {
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(Minecraft.getMinecraft().player);
				skillCap.setCasting(true);
		}
		if (Keyboard.getEventKey() == keyBindings[3].getKeyCode() && !Keyboard.getEventKeyState()) {
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(Minecraft.getMinecraft().player);
				skillCap.setCasting(false);
				skillCap.setDoneCasting(true);
		}
		if (keyBindings[4].isPressed()) {
			BlockPos pos = Minecraft.getMinecraft().player.getPosition();
			Minecraft.getMinecraft().player.openGui(WuxiaCraft.instance, GuiHandler.SKILLS_GUI_ID, Minecraft.getMinecraft().player.world, pos.getX(), pos.getY(), pos.getZ());
		}
		if (keyBindings[5].isPressed()) {
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(Minecraft.getMinecraft().player);
			int next = skillCap.getActiveSkill() + 1;
			NetworkWrapper.INSTANCE.sendToServer(new SelectSkillMessage(next, Minecraft.getMinecraft().player.getUniqueID()));
			SelectSkillMessageHandler.selectSkill(skillCap, next);
		}
		if (keyBindings[6].isPressed()) {
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(Minecraft.getMinecraft().player);
			int next = skillCap.getActiveSkill() - 1;
			NetworkWrapper.INSTANCE.sendToServer(new SelectSkillMessage(next, Minecraft.getMinecraft().player.getUniqueID()));
			SelectSkillMessageHandler.selectSkill(skillCap, next);
		}
		if(keyBindings[17].isPressed()) {
			WuxiaCraftConfig.maxSpeed = WuxiaCraftConfig.maxSpeed * -1;
			WuxiaCraftConfig.syncFromFields();
			WuxiaCraftConfig.syncCultivationFromConfigToClient();
			NetworkWrapper.INSTANCE.sendToServer(new SpeedHandicapMessage(WuxiaCraftConfig.speedHandicap, WuxiaCraftConfig.maxSpeed, WuxiaCraftConfig.blockBreakLimit, WuxiaCraftConfig.jumpLimit, Minecraft.getMinecraft().player.getUniqueID()));
		}
		for (int i = 0; i < 10; i++) {
			if (keyBindings[7 + i].isPressed()) {
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(Minecraft.getMinecraft().player);
				NetworkWrapper.INSTANCE.sendToServer(new SelectSkillMessage(i, Minecraft.getMinecraft().player.getUniqueID()));
				SelectSkillMessageHandler.selectSkill(skillCap, i);
			}
		}

	}

}
