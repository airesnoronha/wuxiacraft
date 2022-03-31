package com.lazydragonstudios.wuxiacraft.client;

import com.google.common.collect.ImmutableList;
import com.lazydragonstudios.wuxiacraft.capabilities.ClientAnimationState;
import com.lazydragonstudios.wuxiacraft.client.gui.MeditateScreen;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.networking.BroadcastAnimationChangeRequestMessage;
import com.lazydragonstudios.wuxiacraft.networking.CultivationStateChangeMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import com.lazydragonstudios.wuxiacraft.networking.OpenScreenMessage;

import java.util.HashMap;
import java.util.List;

public class InputHandler {
	public static final int OPEN_INTROSPECTION = 0;
	public static final int MEDITATE = 1;
	public static final int EXERCISE = 2;
	public static final int CAST_SKILL = 3;
	public static final int COMBAT_MODE = 4;
	public static final int DIVINE_SENSE = 5;
	public static final int SKILL_WHEEL = 6;

	public static HashMap<Integer, KeyMapping> mappings;

	public static void registerKeyBindings() {
		mappings = new HashMap<>();
		mappings.put(OPEN_INTROSPECTION, new KeyMapping("wuxiacraft.key.introspection", GLFW.GLFW_KEY_K, "wuxiacraft.category.main"));
		mappings.put(MEDITATE, new KeyMapping("wuxiacraft.key.meditate", GLFW.GLFW_KEY_Z, "wuxiacraft.category.main"));
		mappings.put(EXERCISE, new KeyMapping("wuxiacraft.key.exercise", GLFW.GLFW_KEY_X, "wuxiacraft.category.main"));
		mappings.put(CAST_SKILL, new KeyMapping("wuxiacraft.key.cast_skill", GLFW.GLFW_KEY_C, "wuxiacraft.category.main"));
		mappings.put(COMBAT_MODE, new KeyMapping("wuxiacraft.key.combat_mode", GLFW.GLFW_KEY_V, "wuxiacraft.category.main"));
		mappings.put(DIVINE_SENSE, new KeyMapping("wuxiacraft.key.divine_sense", GLFW.GLFW_KEY_V, "wuxiacraft.category.main"));
		mappings.put(SKILL_WHEEL, new KeyMapping("wuxiacraft.key.skill_wheel", GLFW.GLFW_KEY_TAB, "wuxiacraft.category.main"));
		for (var mapping : mappings.values()) {
			ClientRegistry.registerKeyBinding(mapping);
		}
	}

	@SubscribeEvent
	public static void onKeyPressed(InputEvent.KeyInputEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!(event.getAction() == GLFW.GLFW_RELEASE || event.getAction() == GLFW.GLFW_PRESS)) return;
		var animationState = new ClientAnimationState();
		var cultivation = Cultivation.get(player);
		var skillData = cultivation.getSkills();
		if (mappings.get(OPEN_INTROSPECTION).consumeClick()) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new OpenScreenMessage(OpenScreenMessage.ScreenType.INTROSPECTION));
		}
		if (mappings.get(MEDITATE).consumeClick()) {
			Minecraft.getInstance().setScreen(new MeditateScreen());
			animationState.setMeditating(true);
			WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState, cultivation.isCombat()));
		}
		if (event.getKey() == mappings.get(EXERCISE).getKey().getValue()) {
			if (event.getAction() == GLFW.GLFW_PRESS) {
				animationState.setExercising(true);
				cultivation.setExercising(true);
			} else if (event.getAction() == GLFW.GLFW_RELEASE) {
				animationState.setExercising(false);
				cultivation.setExercising(false);
			}
			WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState, cultivation.isCombat()));
		}
		if (event.getKey() == mappings.get(CAST_SKILL).getKey().getValue()) {
			if (event.getAction() == GLFW.GLFW_PRESS) {
				skillData.casting = true;
			} else if (event.getAction() == GLFW.GLFW_RELEASE) {
				skillData.casting = false;
			}
			WuxiaPacketHandler.INSTANCE.sendToServer(new CultivationStateChangeMessage(skillData.selectedSkill, skillData.casting, cultivation.isDivineSense()));
		}
		if (event.getKey() == mappings.get(COMBAT_MODE).getKey().getValue()) {
			if (event.getAction() == GLFW.GLFW_RELEASE) {
				cultivation.setCombat(!cultivation.isCombat());
			}
			WuxiaPacketHandler.INSTANCE.sendToServer(new BroadcastAnimationChangeRequestMessage(animationState, cultivation.isCombat()));
		}
		if (event.getKey() == mappings.get(DIVINE_SENSE).getKey().getValue()) {
			if (event.getAction() == GLFW.GLFW_PRESS) {
				cultivation.setDivineSense(true);
			} else if (event.getAction() == GLFW.GLFW_RELEASE) {
				cultivation.setDivineSense(false);
			}
			WuxiaPacketHandler.INSTANCE.sendToServer(new CultivationStateChangeMessage(skillData.selectedSkill, skillData.casting, cultivation.isDivineSense()));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onMeditatingIgnoreInputs(TickEvent.PlayerTickEvent event) {
		if (!(event.player instanceof LocalPlayer)) return;
		if (event.phase != TickEvent.Phase.END) return;
		var animationState = ClientAnimationState.get(event.player);
		if (animationState.isExercising()) {
			List<KeyMapping> movementInputs = ImmutableList.of(
					Minecraft.getInstance().options.keyUp,
					Minecraft.getInstance().options.keyLeft,
					Minecraft.getInstance().options.keyRight,
					Minecraft.getInstance().options.keyDown
			);
			for (var input : movementInputs) {
				input.consumeClick();
			}
		}
	}

}
