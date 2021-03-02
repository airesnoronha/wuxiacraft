package wuxiacraft.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.glfw.GLFW;
import wuxiacraft.client.SkillValues;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.network.OpenMeditationScreenMessage;
import wuxiacraft.network.WuxiaPacketHandler;

public class InputHandler {

	public static final int KEY_CAST_SKILL = 0;
	public static final int KEY_SKILL_SELECT_UP = 1;
	public static final int KEY_SKILL_SELECT_DOWN = 2;
	public static final int KEY_CULTIVATE = 3;
	public static final int KEY_MEDITATE = 4;
	public static final int KEY_INTROSPECTION = 5;
	public static final int KEY_SKILLS = 6;

	public static KeyBinding[] keyBindings;

	public static void registerKeyBindings() {
		keyBindings = new KeyBinding[7];
		keyBindings[KEY_CAST_SKILL] = new KeyBinding("wuxiacraft.key.cast_skill", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F, "wuxiacraft.category.main");
		keyBindings[KEY_SKILL_SELECT_UP] = new KeyBinding("wuxiacraft.key.select_up", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_V, "wuxiacraft.category.main");
		keyBindings[KEY_SKILL_SELECT_DOWN] = new KeyBinding("wuxiacraft.key.select_down", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "wuxiacraft.category.main");
		keyBindings[KEY_CULTIVATE] = new KeyBinding("wuxiacraft.key.cultivate", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_X, "wuxiacraft.category.main");
		keyBindings[KEY_MEDITATE] = new KeyBinding("wuxiacraft.key.meditate", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_Z, "wuxiacraft.category.main");
		keyBindings[KEY_INTROSPECTION] = new KeyBinding("wuxiacraft.key.introspection", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_K, "wuxiacraft.category.main");
		keyBindings[KEY_SKILLS] = new KeyBinding("wuxiacraft.key.skills", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_L, "wuxiacraft.category.main");
		for (KeyBinding key : keyBindings) {
			ClientRegistry.registerKeyBinding(key);
		}
	}

	@SubscribeEvent
	public void onHandleKeyInputs(InputEvent.KeyInputEvent event) {
		SkillValues.isCastingSkill = keyBindings[KEY_CAST_SKILL].isKeyDown();
		if (keyBindings[KEY_SKILL_SELECT_UP].isPressed()) {
			if (Minecraft.getInstance().player != null) {
				ICultivation cultivation = Cultivation.get(Minecraft.getInstance().player);
				SkillValues.activeSkill = Math.min(cultivation.getAllKnownSkills().size()-1, SkillValues.activeSkill + 1);
			}
		}
		if (keyBindings[KEY_SKILL_SELECT_DOWN].isPressed()) {
			SkillValues.activeSkill = Math.max(0, SkillValues.activeSkill - 1);
		}
		if(keyBindings[KEY_MEDITATE].isPressed()) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new OpenMeditationScreenMessage());
		}
	}

}
