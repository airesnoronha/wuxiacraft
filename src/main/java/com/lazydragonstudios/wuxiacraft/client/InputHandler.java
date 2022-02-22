package com.lazydragonstudios.wuxiacraft.client;

import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.capabilities.IClientAnimationState;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.networking.BroadcastAnimationChangeRequestMessage;
import com.lazydragonstudios.wuxiacraft.networking.SkillCastingMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import com.lazydragonstudios.wuxiacraft.networking.OpenScreenMessage;

import java.util.HashMap;

public class InputHandler {
	public static final int OPEN_INTROSPECTION = 0;
	public static final int MEDITATE = 1;
	public static final int EXERCISE = 2;
	public static final int CAST_SKILL = 3;

	public static HashMap<Integer, KeyMapping> mappings;

	public static void registerKeyBindings() {
		mappings = new HashMap<>();
		mappings.put(OPEN_INTROSPECTION, new KeyMapping("wuxiacraft.key.introspection", GLFW.GLFW_KEY_K, "wuxiacraft.category.main"));
		mappings.put(MEDITATE, new KeyMapping("wuxiacraft.key.meditate", GLFW.GLFW_KEY_Z, "wuxiacraft.category.main"));
		mappings.put(EXERCISE, new KeyMapping("wuxiacraft.key.exercise", GLFW.GLFW_KEY_X, "wuxiacraft.category.main"));
		mappings.put(CAST_SKILL, new KeyMapping("wuxiacraft.key.cast_skill", GLFW.GLFW_KEY_X, "wuxiacraft.category.main"));
		for (var mapping : mappings.values()) {
			ClientRegistry.registerKeyBinding(mapping);
		}
	}

	@SubscribeEvent
	public static void onKeyPressed(InputEvent.KeyInputEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		if (mappings.get(OPEN_INTROSPECTION).consumeClick()) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new OpenScreenMessage(OpenScreenMessage.ScreenType.INTROSPECTION));
		}
		if (event.getKey() == mappings.get(MEDITATE).getKey().getValue()) {
			var animationState = ClientAnimationState.get(player);
			if (event.getAction() == GLFW.GLFW_PRESS) {
				animationState.setMeditating(true);
				WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState));
			}
			if (event.getAction() == GLFW.GLFW_RELEASE) {
				animationState.setMeditating(false);
				WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState));
			}
		}
		if (event.getKey() == mappings.get(EXERCISE).getKey().getValue()) {
			var animationState = ClientAnimationState.get(player);
			var cultivation = Cultivation.get(player);
			if (event.getAction() == GLFW.GLFW_PRESS) {
				animationState.setExercising(true);
				cultivation.setExercising(true);
				WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState));
			}
			if (event.getAction() == GLFW.GLFW_RELEASE) {
				animationState.setExercising(false);
				cultivation.setExercising(false);
				WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState));
			}
		}
		if (event.getKey() == mappings.get(CAST_SKILL).getKey().getValue()) {
			var skillData = Cultivation.get(player).getSkills();
			if (event.getAction() == GLFW.GLFW_PRESS) {
				skillData.casting = true;
				WuxiaPacketHandler.INSTANCE.sendToServer(new SkillCastingMessage(skillData.selectedSkill, true));
			}
			if (event.getAction() == GLFW.GLFW_RELEASE) {
				skillData.casting = false;
				WuxiaPacketHandler.INSTANCE.sendToServer(new SkillCastingMessage(skillData.selectedSkill, false));
			}
		}
	}

}
