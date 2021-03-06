package wuxiacraft.client.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import wuxiacraft.client.SkillValues;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.KnownTechnique;
import wuxiacraft.cultivation.technique.BodyTechnique;
import wuxiacraft.network.ExerciseMessage;
import wuxiacraft.network.OpenScreenMessage;
import wuxiacraft.network.WuxiaPacketHandler;

import java.util.List;

public class InputHandler {

	public static final int KEY_CAST_SKILL = 0;
	public static final int KEY_SKILL_SELECT_UP = 1;
	public static final int KEY_SKILL_SELECT_DOWN = 2;
	public static final int KEY_EXERCISE = 3;
	public static final int KEY_MEDITATE = 4;
	public static final int KEY_INTROSPECTION = 5;
	public static final int KEY_SKILLS = 6;

	public static KeyBinding[] keyBindings;

	public static void registerKeyBindings() {
		keyBindings = new KeyBinding[7];
		keyBindings[KEY_CAST_SKILL] = new KeyBinding("wuxiacraft.key.cast_skill", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F, "wuxiacraft.category.main");
		keyBindings[KEY_SKILL_SELECT_UP] = new KeyBinding("wuxiacraft.key.select_up", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_V, "wuxiacraft.category.main");
		keyBindings[KEY_SKILL_SELECT_DOWN] = new KeyBinding("wuxiacraft.key.select_down", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "wuxiacraft.category.main");
		keyBindings[KEY_EXERCISE] = new KeyBinding("wuxiacraft.key.exercise", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_X, "wuxiacraft.category.main");
		keyBindings[KEY_MEDITATE] = new KeyBinding("wuxiacraft.key.meditate", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_Z, "wuxiacraft.category.main");
		keyBindings[KEY_INTROSPECTION] = new KeyBinding("wuxiacraft.key.introspection", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_K, "wuxiacraft.category.main");
		keyBindings[KEY_SKILLS] = new KeyBinding("wuxiacraft.key.skills", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_L, "wuxiacraft.category.main");
		for (KeyBinding key : keyBindings) {
			ClientRegistry.registerKeyBinding(key);
		}
	}

	@SubscribeEvent
	public void onHandleKeyInputs(InputEvent.KeyInputEvent event) {
		if (Minecraft.getInstance().player != null) {
			ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
			if (keyBindings[KEY_SKILL_SELECT_UP].isPressed()) {
				SkillValues.activeSkill = Math.min(cultivation.getAllKnownSkills().size() - 1, SkillValues.activeSkill + 1);
			}
			if (keyBindings[KEY_SKILL_SELECT_DOWN].isPressed()) {
				SkillValues.activeSkill = Math.max(0, SkillValues.activeSkill - 1);
			}
			boolean shouldMeditate = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE) != null;
			if (keyBindings[KEY_MEDITATE].isPressed() && shouldMeditate) {
				WuxiaPacketHandler.INSTANCE.sendToServer(new OpenScreenMessage(OpenScreenMessage.TargetGui.MEDITATION));
			}
			if (keyBindings[KEY_INTROSPECTION].isPressed()) {
				WuxiaPacketHandler.INSTANCE.sendToServer(new OpenScreenMessage(OpenScreenMessage.TargetGui.INTROSPECTION));
			}
		}
	}

	public static double accumulatedEnergyToSend = 0;

	//this is more of render thing, so stuff that render might know this
	public static boolean isExercising = false;

	@SubscribeEvent(priority = EventPriority.HIGHEST) //high priority to roll before skill handling
	public void onHandlePressedKeys(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;
		if (event.side == LogicalSide.SERVER) return;
		SkillValues.isCastingSkill = keyBindings[KEY_CAST_SKILL].isKeyDown();
		PlayerEntity player = event.player;
		ICultivation cultivation = Cultivation.get(player);
		KnownTechnique bodyKT = cultivation.getTechniqueBySystem(CultivationLevel.System.BODY);
		isExercising = keyBindings[KEY_EXERCISE].isKeyDown() && bodyKT != null;
		if (isExercising) {
			List<InputMappings.Input> movementInputs = ImmutableList.of(
					InputMappings.getInputByCode(Minecraft.getInstance().gameSettings.keyBindForward.getKey().getKeyCode(), 0),
					InputMappings.getInputByCode(Minecraft.getInstance().gameSettings.keyBindBack.getKey().getKeyCode(), 0),
					InputMappings.getInputByCode(Minecraft.getInstance().gameSettings.keyBindLeft.getKey().getKeyCode(), 0),
					InputMappings.getInputByCode(Minecraft.getInstance().gameSettings.keyBindRight.getKey().getKeyCode(), 0));
			for (InputMappings.Input input : movementInputs) {
				KeyBinding.setKeyBindState(input, false);
			}
			double energy = cultivation.getMaxBodyEnergy() * 0.002;
			accumulatedEnergyToSend += energy;
			BodyTechnique bodyTech = (BodyTechnique) bodyKT.getTechnique();
			double energy_conversion = 1 + (bodyTech.getConversionRate() * (0.3f + 0.7 * bodyKT.getReleaseFactor()));
			cultivation.getStatsBySystem(CultivationLevel.System.BODY).addEnergy(-energy);
			cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(energy * energy_conversion);
		}
		if (cultivation.getTickerTime() % 20 == 0 && accumulatedEnergyToSend > 0) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new ExerciseMessage(accumulatedEnergyToSend));
			accumulatedEnergyToSend = 0;
		}
	}

}
